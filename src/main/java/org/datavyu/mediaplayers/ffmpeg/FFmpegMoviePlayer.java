package org.datavyu.mediaplayers.ffmpeg;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.bytedeco.javacv.FrameGrabber;
import org.datavyu.madias.DatavyuMedia;
import org.datavyu.util.Rate;
import java.util.concurrent.*;

import static org.bytedeco.javacpp.avutil.AV_LOG_ERROR;
import static org.bytedeco.javacpp.avutil.av_log_set_level;


//TODO: Stack already played images for backward playback
//TODO: Share the same media player status for both the stream and clock
public class FFmpegMoviePlayer extends StackPane {
    private DatavyuAudioGrabber audioGrabber;
    private DatavyuImageGrabber imageGrabber;
    private long currentTime;
    private int volume;
    private boolean isPlaying;
    private Runnable play;
    private ImageView imageView;
    private Rate rate = Rate.playRate();
    private Clock clock;
    private ScheduledExecutorService imageTimer;

    /**
     * everything should go through the internal clock
     * @param media
     */
    public FFmpegMoviePlayer(DatavyuMedia media) {
        try {
            //Start buffering the Audio and Images and wait for the consumer to start
            audioGrabber = DatavyuAudioGrabber.createAudioGrabber(media);
            imageGrabber = DatavyuImageGrabber.createImageGrabber(media);
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }

        imageView = new ImageView();
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());
        this.getChildren().add(imageView);

        isPlaying = false;

        clock = new Clock();

        av_log_set_level(AV_LOG_ERROR);
    }

    public void play() {
        if (!isPlaying) {
            isPlaying = true;
            clock.start();
            play = () -> {
                System.out.println("Stream Clock: " + this.currentTime);
                try {
                    Image grabbedImage = imageGrabber.grabImage();
//                    System.out.println("Index: " + cpt + " Frame Number: " + grabber.getFrameNumber() + " Grabber Timestamp: " + grabbedImage.timestamp);
//                    cpt++;
//                    if (grabbedImage.samples != null) {
//                        AudioFormat audioFormat = new AudioFormat(grabber.getSampleRate(), 16, grabber.getAudioChannels(), true, true);
//                        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
//                        SourceDataLine soundLine = (SourceDataLine) AudioSystem.getLine(info);
//                        soundLine.open(audioFormat);
//                        soundLine.start();
//
//                        ExecutorService executor = Executors.newSingleThreadExecutor();
//
//                        ShortBuffer channelSamplesShortBuffer = (ShortBuffer) grabbedImage.samples[0];
//                        channelSamplesShortBuffer.rewind();
//
//                        ByteBuffer outBuffer = ByteBuffer.allocate(channelSamplesShortBuffer.capacity() * 2);
//
//                        for (int i = 0; i < channelSamplesShortBuffer.capacity(); i++) {
//                            short val = channelSamplesShortBuffer.get(i);
//                            outBuffer.putShort(val);
//                        }
//                        try {
//                            executor.submit(() -> {
//                                soundLine.write(outBuffer.array(), 0, outBuffer.capacity());
//                                outBuffer.clear();
//                            }).get();
//                        } catch (InterruptedException interruptedException) {
//                            Thread.currentThread().interrupt();
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    if (grabbedImage != null) {
                        Platform.runLater(() -> {
                            imageView.setImage(grabbedImage);
                        });
                    }
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            };
            this.imageTimer = Executors.newSingleThreadScheduledExecutor();
//          this.timer.scheduleAtFixedRate(playThread, 0, (long) ((grabber.getFrameRate()*grabber.getLengthInFrames())/grabber.getLengthInAudioFrames()), TimeUnit.MILLISECONDS);
            this.imageTimer.scheduleAtFixedRate(play, 0, (long) (1000/(imageGrabber.getFrameRate())), TimeUnit.MILLISECONDS);
        }
        
    }

    public void pause() {
        isPlaying = false;
        clock.pause();
    }

    public void stop() {
        try {
            this.isPlaying = false;
            clock.stop();
            this.imageGrabber.setTimestamp(0L);
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }

    public long getCurrentTime() {
        return this.currentTime;
    }

    public float getFrameRate() {
        return 0;
    }

    public int getCurrentFrame() {
        return 0;
    }

    public int getVolume() {
        return this.volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getImageWidth() {
        return this.imageGrabber.getImageWidth();
    }

    public double getImageHeight() {
        return this.imageGrabber.getImageHeight();
    }

    public void shuttle(Rate rate) {
        this.rate = rate;
        clock.setRate(rate);
    }

    private class Clock{
        /** Clock tick period in milliseconds */
        private static final long CLOCK_SYNC_INTERVAL = 100L;

        /** Clock initial delay in milliseconds */
        private static final long CLOCK_SYNC_DELAY = 0L;

        /** Convert nanoseconds to milliseconds */
        private static final long NANO_IN_MILLI = 1000000L;
        private final ScheduledExecutorService clockTimer;

        private Rate rate =  Rate.playRate();

        /** Minimum time for the clock in milliseconds */
        private long minTime;

        /** Maximum time for the clock in milliseconds */
        private long maxTime;

        /** Current time of the clock in milliseconds */
        private long clockTime;

        /** Last time in nanoseconds; it is used to calculate the elapsed */
        private double lastTime;

        private boolean isPlaying;


        public Clock(){
            this.isPlaying = false;
            this.clockTime = 0;

            clockTimer =  Executors.newSingleThreadScheduledExecutor();

            // Sync timer at lower frequency
            clockTimer.scheduleAtFixedRate(() -> updateElapsedTime()
                    ,CLOCK_SYNC_DELAY,CLOCK_SYNC_INTERVAL,TimeUnit.MILLISECONDS);
        }

        public synchronized void start() {
            if (!isPlaying) {
                isPlaying = true;
                lastTime = System.nanoTime();
            }
        }

        public synchronized void stop() {
            if (isPlaying) {
                isPlaying = false;
            }
        }

        public synchronized void pause() {
            if (isPlaying) {
                isPlaying = false;
            }
        }

        public synchronized void setRate(Rate rate){
            this.rate = rate;
        }

        /**
         * Update the clock time with the elapsed time since the last update
         */
        private synchronized void updateElapsedTime() {
            long newTime = System.nanoTime();
            clockTime = !isPlaying ? clockTime : (long) (clockTime + this.rate.getValue() * (newTime - lastTime) / NANO_IN_MILLI);
            lastTime = newTime;
            currentTime = clockTime;
        }

    }
}
