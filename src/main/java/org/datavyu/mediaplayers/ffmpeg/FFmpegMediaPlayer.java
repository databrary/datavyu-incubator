package org.datavyu.mediaplayers.ffmpeg;

import javafx.util.Duration;
import org.datavyu.madias.DatavyuMedia;
import org.datavyu.mediaplayers.StreamViewer;
import org.datavyu.util.Identifier;
import org.datavyu.util.Rate;


/**
 * This the JavaFX version of the FFmpeg Media Player
 */
public class FFmpegMediaPlayer implements StreamViewer {

    private final Identifier identifier;
    private final DatavyuMedia media;
    private FFmpegMoviePlayer mp;

    private FFmpegMediaPlayer(Identifier identifier, DatavyuMedia media) {
        this.identifier = identifier;
        this.media = media;
        mp = new FFmpegMoviePlayer();
    }

    @Override
    public Identifier getIdentifier() { return this.identifier; }

    @Override
    public void play() { this.mp.play(); }

    @Override
    public void pause() { this.mp.pause(); }

    @Override
    public void stop() { this.mp.stop(); }

    @Override
    public void seekTime(long timeStamp) {

    }

    @Override
    public void shuttle(Rate rate) {

    }

    @Override
    public void jog(int direction, Duration time) {

    }

    @Override
    public long getCurrentTime() { return this.mp.getCurrentTime(); }

    @Override
    public float getFrameRate() { return this.mp.getFrameRate(); }

    @Override
    public void setFrameRate(float fps) {

    }

    @Override
    public int getCurrentFrame() {
        return this.mp.getCurrentFrame();
    }

    @Override
    public Rate getRate() {
        return null;
    }

    @Override
    public void setRate(Rate rate) {

    }

    @Override
    public int getVolume() { return this.mp.getVolume(); }

    @Override
    public void setVolume(int volume) { this.mp.setVolume(volume); }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }
}
