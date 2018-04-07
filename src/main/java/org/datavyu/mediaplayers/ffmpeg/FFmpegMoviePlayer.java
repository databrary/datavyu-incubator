package org.datavyu.mediaplayers.ffmpeg;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.sound.sampled.*;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.*;

import static org.bytedeco.javacpp.avutil.AV_LOG_ERROR;
import static org.bytedeco.javacpp.avutil.av_log_set_level;

public class FFmpegMoviePlayer extends StackPane {
    private final long systemStartTime;
    private final int streamStartTime;
    private final Java2DFrameConverter converter;
    private FFmpegFrameGrabber grabber;
    private long currentTime;
    private float frameRate;
    private int currentFrame;
    private int volume;
    private boolean isPlaying;
    private Runnable playThread;
    private int cpt = 0;
    private Image image;
    private ImageView imageView; //TODO: Need to access the Image View
    private ScheduledExecutorService timer;

    public FFmpegMoviePlayer() {

        imageView = new ImageView();

        this.getChildren().add(imageView);
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());


        // Calculate the time BEFORE we start playing.
        converter = new Java2DFrameConverter();
        systemStartTime = System.nanoTime();
        streamStartTime = 0;
        try {
            grabber = FFmpegFrameGrabber.createDefault(new File("/Users/redanezzar/Databrary/datavyu-incubator/src/main/resources/DatavyuSampleVideo.mp4"));
//            grabber = FFmpegFrameGrabber.createDefault(new File("/Users/redanezzar/Databrary/datavyu-incubator/src/main/resources/hd2.mov"));
            grabber.start();
            System.out.println("FPS: " + grabber.getFrameRate() + " Number of Frame: " + grabber.getLengthInFrames() + " Number of Sample: " + grabber.getLengthInAudioFrames());
            av_log_set_level(AV_LOG_ERROR);
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }

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

    public void pause() {
        this.timer.shutdownNow();
        isPlaying = false;
    }

    public void stop() {
        try {
            timer.shutdown();
            isPlaying = false;
            grabber.setTimestamp(0L);
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public float getFrameRate() {
        return frameRate;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getImageWidth() {
        return this.grabber.getImageWidth();
    }

    public double getImageHeight() {
        return this.grabber.getImageHeight();
    }
}
