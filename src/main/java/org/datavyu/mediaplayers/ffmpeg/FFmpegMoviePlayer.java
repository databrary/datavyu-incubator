package org.datavyu.mediaplayers.ffmpeg;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.bytedeco.javacv.FrameGrabber;
import org.datavyu.madias.DatavyuMedia;
import org.datavyu.util.DatavyuStatus;
import org.datavyu.util.Rate;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import static org.bytedeco.javacpp.avutil.AV_LOG_ERROR;
import static org.bytedeco.javacpp.avutil.av_log_set_level;


//TODO: Stack already played images for backward playback
//TODO: Destroy the executor
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
    private DatavyuStatus streamStatus;

    /**
     * everything should go through the internal clock
     * @param media
     */
    public FFmpegMoviePlayer(DatavyuMedia media) {
        streamStatus = DatavyuStatus.UNKNOWN;
        try {
            //Start buffering the Audio and Images and wait for the consumer to start
            audioGrabber = DatavyuAudioGrabber.createAudioGrabber(media);
            imageGrabber = DatavyuImageGrabber.createImageGrabber(media);
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
            streamStatus = DatavyuStatus.STALLED;
        }

        streamStatus = DatavyuStatus.READY;
        imageView = new ImageView();
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());
        this.getChildren().add(imageView);

        clock = new Clock();

        av_log_set_level(AV_LOG_ERROR);
    }

    public void play() {
        if (streamStatus != DatavyuStatus.PLAYING
                && streamStatus != DatavyuStatus.UNKNOWN
                && streamStatus != DatavyuStatus.STALLED) {
            play = () -> {
                streamStatus = DatavyuStatus.PLAYING;
//                try {
                    if (streamStatus == DatavyuStatus.PLAYING){
//                        System.out.println("Stream Clock: " + this.currentTime);
//                        Image grabbedImage = imageGrabber.grabImage();
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
//                        if (grabbedImage != null) {
//                            Platform.runLater(() -> {
//                                imageView.setImage(grabbedImage);
//                            });
//                        }
//
                    }
//                } catch (FrameGrabber.Exception e) {
//                    e.printStackTrace();
//                }
            };

            this.imageTimer = Executors.newSingleThreadScheduledExecutor();
            //This at 1X for the
            this.imageTimer.scheduleAtFixedRate(play, 0, (long) (1000/(imageGrabber.getFrameRate())), TimeUnit.MILLISECONDS);
//          this.timer.scheduleAtFixedRate(playThread, 0, (long) ((grabber.getFrameRate()*grabber.getLengthInFrames())/grabber.getLengthInAudioFrames()), TimeUnit.MILLISECONDS);

        }
        
    }

    public void pause() {
        streamStatus = DatavyuStatus.PAUSED;
        this.imageTimer.shutdown();
    }

    public void stop() {
        try {
            streamStatus = DatavyuStatus.STOPPED;
            this.imageTimer.shutdownNow();
            //TODO the image grabber should listen to the clock
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
        private static final long CLOCK_SYNC_INTERVAL = 10L;

        /** Clock initial delay in milliseconds */
        private static final long CLOCK_SYNC_DELAY = 0L;

        /** Convert nanoseconds to milliseconds */
        private static final long NANO_IN_MILLI = 1000000L;
        private final ScheduledExecutorService clockTimer;

        /** Last time in nanoseconds; it is used to calculate the elapsed */
        private long lastTime;
        int cpt = 0;

        public Clock(){
            currentTime = 0;

            clockTimer =  Executors.newScheduledThreadPool(2);

            //Execute Concurrently Status update update elapsed time

            clockTimer.scheduleAtFixedRate(() -> {
                updateElapsedTime();} ,CLOCK_SYNC_DELAY,CLOCK_SYNC_INTERVAL,TimeUnit.MILLISECONDS);
            clockTimer.scheduleAtFixedRate(() -> {
                updateClockStatus();} ,CLOCK_SYNC_DELAY,CLOCK_SYNC_INTERVAL,TimeUnit.MILLISECONDS);
        }

        /**
         * change the state of the clock according to the StreamViewer Status
         * get executed simultaneously with update elapsed time
         */
        private synchronized void updateClockStatus() {
            if(streamStatus == DatavyuStatus.PLAYING){
                this.start();
            }
            if(streamStatus == DatavyuStatus.PAUSED){
                this.pause();
            }
            if(streamStatus == DatavyuStatus.STOPPED){
                this.stop();
            }
        }

        public synchronized void start() {
                lastTime = System.nanoTime();
        }

        public synchronized void stop() {
                currentTime = 0;
        }

        public synchronized void pause() {
        }

        public synchronized void setRate(Rate rate){
        }

        /**
         * Update the clock time with the elapsed time since the last update
         * The rate will affect the elapsed time jumps and we can use the jumps to make the play thread according to the rate
         * TODO: Affect the rate and change the executor fixed rate or make the thread sleep
         */
        private synchronized void updateElapsedTime() {
            long newTime = System.nanoTime();
            currentTime = streamStatus != DatavyuStatus.PLAYING ? currentTime : (long) (currentTime + rate.getValue() * (newTime - lastTime) / NANO_IN_MILLI);
            lastTime = newTime;
        }

    }
}
