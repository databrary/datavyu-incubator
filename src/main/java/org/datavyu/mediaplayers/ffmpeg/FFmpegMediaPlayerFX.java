package org.datavyu.mediaplayers.ffmpeg;



import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.bytedeco.javacv.*;
import org.datavyu.madias.DatavyuMedia;
import org.datavyu.mediaplayers.StreamViewer;
import org.datavyu.util.Identifier;
import org.datavyu.util.Rate;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.*;


public class FFmpegMediaPlayerFX extends Stage implements StreamViewer {

    private final Identifier identifier;
    private DatavyuMedia media;
//    private FrameGrabber grabber;
    private FFmpegFrameGrabber grabber;
    private FFmpegFrameRecorder recorder;
    private Java2DFrameConverter converter;
    private ImageView imageView;
    private Image image;

    private AudioClip audio;

    private Runnable playThread;

    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    //StartTime will be used to keep of the master and stream time to help diplay a frame and sample
    //at the correct time
    private long systemStartTime;
    private long streamStartTime;

    private int cpt = 0;

    private boolean isPlaying;

    //TODO: add a frame grabber and an audio grabber and a MovieStreamer to sync the two streams
    // FFmpeg Media Player will talk to the MovieStreamer

    //TODO: could use memoryStorage to cache the frames available in openCV
    //CvMemStorage storage = CvMemStorage.create();

    public FFmpegMediaPlayerFX(Identifier identifier, DatavyuMedia media){
        this.identifier = identifier;
        this.media = media;

        StackPane root = new StackPane();
        imageView = new ImageView();

        root.getChildren().add(imageView);
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());

        Scene scene = new Scene(root, grabber.getImageWidth(), grabber.getImageHeight());

        this.setTitle("Video: "+ media.getSource());
        this.setScene(scene);
        this.setOnCloseRequest(event -> this.close());
        this.show();
    }

    @Override
    public Slider getStreamTimeSlider() {
        return null;
    }

    @Override
    public Identifier getIdentifier() {
        return this.identifier;
    }

    @Override
    public void play() {
        if (!isPlaying) {
            isPlaying = true;
            playThread = () -> {
                try {
                    Frame grabbedImage = grabber.grabImage();
                    System.out.println("Index: " + cpt + " Frame Number: " + grabber.getFrameNumber() + " Grabber Timestamp: " + grabbedImage.timestamp);
                    cpt++;
                    if (grabbedImage.samples != null) {
                        AudioFormat audioFormat = new AudioFormat(grabber.getSampleRate(), 16, grabber.getAudioChannels(), true, true);
                        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                        SourceDataLine soundLine = (SourceDataLine) AudioSystem.getLine(info);
                        soundLine.open(audioFormat);
                        soundLine.start();

                        ExecutorService executor = Executors.newSingleThreadExecutor();

                        ShortBuffer channelSamplesShortBuffer = (ShortBuffer) grabbedImage.samples[0];
                        channelSamplesShortBuffer.rewind();

                        ByteBuffer outBuffer = ByteBuffer.allocate(channelSamplesShortBuffer.capacity() * 2);

                        for (int i = 0; i < channelSamplesShortBuffer.capacity(); i++) {
                            short val = channelSamplesShortBuffer.get(i);
                            outBuffer.putShort(val);
                        }
                        try {
                            executor.submit(() -> {
                                soundLine.write(outBuffer.array(), 0, outBuffer.capacity());
                                outBuffer.clear();
                            }).get();
                        } catch (InterruptedException interruptedException) {
                            Thread.currentThread().interrupt();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    if (grabbedImage.image != null) {
                        image = SwingFXUtils.toFXImage(converter.convert(grabbedImage), null);
                        Platform.runLater(() -> {
                            imageView.setImage(image);
                        });
                    }
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            };
            this.timer = Executors.newSingleThreadScheduledExecutor();
//          this.timer.scheduleAtFixedRate(playThread, 0, (long) ((grabber.getFrameRate()*grabber.getLengthInFrames())/grabber.getLengthInAudioFrames()), TimeUnit.MILLISECONDS);
            this.timer.scheduleAtFixedRate(playThread, 0, (long) (1000/(grabber.getFrameRate())), TimeUnit.MILLISECONDS);
        }
    }

    /** Takes the video picture and displays it at the right time.
     */
//        private static void displayVideoAtCorrectTime(long streamStartTime,
//        Frame image, long systemStartTime)
//      throws InterruptedException {
//            long streamTimestamp = image.getTimeStamp();
//            // convert streamTimestamp into system units (i.e. nano-seconds)
//            streamTimestamp = systemTimeBase.rescale(streamTimestamp-streamStartTime, streamTimebase);
//            // get the current clock time, with our most accurate clock
//            long systemTimestamp = System.nanoTime();
//            // loop in a sleeping loop until we're within 1 ms of the time for that video frame.
//            // a real video player needs to be much more sophisticated than this.
//            while (streamTimestamp > (systemTimestamp - systemStartTime + 1000000)) {
//                Thread.sleep(1);
//                systemTimestamp = System.nanoTime();
//            }
//            // finally, convert the image from Humble format into Java images.
//            image = converter.toImage(image, picture);
//            // And ask the UI thread to repaint with the new image.
//            window.setImage(image);
//            return image;
////        }

    @Override
    public void pause() {
        this.timer.shutdownNow();
        isPlaying = false;
    }

    @Override
    public void stop() {
        try {
            timer.shutdown();
            isPlaying = false;
            grabber.setTimestamp(0L);
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void seekTime(long time) {

    }

    @Override
    public void shuttle(Rate rate) {

    }

    @Override
    public void jog(int direction, Duration time) {

    }

    @Override
    public long getCurrentTime() {
        return 0;
    }

    @Override
    public float getFrameRate() {
        return 0;
    }

    @Override
    public void setFrameRate(float fps) {

    }

    @Override
    public int getCurrentFrame() {
        return 0;
    }

    @Override
    public LongProperty currentTimeProperty() {
        return null;
    }

    @Override
    public Rate getRate() {
        return null;
    }

    @Override
    public void setRate(Rate rate) {

    }

    @Override
    public int getVolume() { return 0; }

    @Override
    public void setVolume(int volume) {

    }

    @Override
    public void visible() {

    }

    @Override
    public void back(Duration time) {

    }

    @Override
    public long getDuration() {
        return 0;
    }
}
