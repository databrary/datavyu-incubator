package org.datavyu.mediaplayers.ffmpeg;



import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.bytedeco.javacv.*;
import org.datavyu.madias.DatavyuMedia;
import org.datavyu.mediaplayers.StreamViewer;
import org.datavyu.util.Identifier;
import org.datavyu.util.Rate;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.bytedeco.javacpp.avutil.AV_LOG_ERROR;
import static org.bytedeco.javacpp.avutil.av_log_set_level;

public class FFmpegMediaPlayerFX extends Stage implements StreamViewer {

    private final Identifier identifier;
    private DatavyuMedia media;
//    private FrameGrabber grabber;
    private FFmpegFrameGrabber grabber;
    private FFmpegFrameRecorder recorder;
    private Java2DFrameConverter converter;
    private ImageView imageView;
    private Image image;

    private Runnable playThread;

    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    //StartTime will be used to keep of the master and stream time to help diplay a frame and sample
    //at the correct time
    private long systemStartTime;
    private long streamStartTime;

    private int cpt = 0;

    private boolean isPlaying = false;

    //TODO: add a frame grabber and an audio grabber and a MovieStreamer to sync the two streams
    // FFmpeg Media Player will talk to the MovieStreamer

    //TODO: could use memoryStorage to cache the frames available in openCV
    //CvMemStorage storage = CvMemStorage.create();

    public FFmpegMediaPlayerFX(Identifier identifier, DatavyuMedia media){
        this.identifier = identifier;
        this.media = media;
        converter = new Java2DFrameConverter();
        // Calculate the time BEFORE we start playing.
        systemStartTime = System.nanoTime();
        streamStartTime = 0;

        try {
//            grabber = FFmpegFrameGrabber.createDefault(new File("/Users/redanezzar/Databrary/datavyu-incubator/src/main/resources/DatavyuSampleVideo.mp4"));
            grabber = FFmpegFrameGrabber.createDefault(new File("/Users/redanezzar/Databrary/datavyu-incubator/src/main/resources/hd2.mov"));
            grabber.start();
            System.out.println("FPS: " + grabber.getFrameRate() + " Number of frames: " + grabber.getLengthInFrames());
            av_log_set_level(AV_LOG_ERROR);
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }


        StackPane root = new StackPane();
        imageView = new ImageView();

        root.getChildren().add(imageView);
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());

        Scene scene = new Scene(root, grabber.getImageWidth(), grabber.getImageHeight());

        this.setTitle("Video: "+ media.getSource());
        this.setScene(scene);
        this.setOnCloseRequest(event -> {
            this.close();
        });
        this.show();

    }

    @Override
    public Identifier getIdentifier() {
        return this.identifier;
    }

    @Override
    public void play() {
        playThread = () -> {
            try {
                Frame grabbedImage = grabber.grabImage();
                System.out.println("Index: " + cpt + " Frame Number: " + grabber.getFrameNumber() + " Grabber Timestamp: " + grabbedImage.timestamp);
                cpt++;
                if (grabbedImage.image != null) {
                    image = SwingFXUtils.toFXImage(converter.convert(grabbedImage), null);
                    Platform.runLater(() -> {
                        imageView.setImage(image);
                    });
                }
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        };

        this.timer = Executors.newSingleThreadScheduledExecutor();
        this.timer.scheduleAtFixedRate(playThread, 0, (long) (1000/grabber.getFrameRate()), TimeUnit.MILLISECONDS);
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
    }

    @Override
    public void stop() {
        try {
            timer.shutdown();
            grabber.setTimestamp(0L);
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void seek(long time) {

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
    public Rate getRate() {
        return null;
    }

    @Override
    public void setRate(Rate rate) {

    }

    @Override
    public void add(StreamViewer stream) {

    }
}
