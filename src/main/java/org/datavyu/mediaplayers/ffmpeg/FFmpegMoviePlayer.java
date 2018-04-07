package org.datavyu.mediaplayers.ffmpeg;

public class FFmpegMoviePlayer {
    private long currentTime;
    private float frameRate;
    private int currentFrame;
    private int volume;

    public void play() {
        
    }

    public void pause() {

    }

    public void stop() {

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
}
