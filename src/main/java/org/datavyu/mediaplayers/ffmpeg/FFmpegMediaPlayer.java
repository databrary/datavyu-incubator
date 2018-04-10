package org.datavyu.mediaplayers.ffmpeg;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.datavyu.madias.DatavyuMedia;
import org.datavyu.mediaplayers.StreamViewer;
import org.datavyu.util.Identifier;
import org.datavyu.util.Rate;


/**
 * This the JavaFX version of the FFmpeg Media Player
 */
public class FFmpegMediaPlayer extends Stage implements StreamViewer {

    private final Identifier identifier;
    private final DatavyuMedia media;
    private FFmpegMoviePlayer mp; //FFmpegMoviePlayer extends StackPane
    private Rate rate = Rate.stopRate(); // Start at 0X


    public FFmpegMediaPlayer(Identifier identifier, DatavyuMedia media) {
        this.identifier = identifier;
        this.media = media;

        mp = new FFmpegMoviePlayer(media);

        Scene scene = new Scene(mp, mp.getImageWidth(), mp.getImageHeight());

        this.setTitle(media.getSource());
        this.setScene(scene);
        this.setOnCloseRequest(event -> this.close());
        this.show();
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
        this.setRate(rate);
        this.mp.shuttle(rate);
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
    public int getCurrentFrame() { return this.mp.getCurrentFrame(); }

    @Override
    public Rate getRate() { return this.rate; }

    @Override
    public void setRate(Rate rate) { this.rate = rate; }

    @Override
    public int getVolume() { return this.mp.getVolume(); }

    @Override
    public void setVolume(int volume) { this.mp.setVolume(volume); }

    @Override
    public void visible() { super.show(); }

    @Override
    public void hide() { super.hide(); }

    @Override
    public void close() {
    }

    @Override
    public void back(Duration time) {

    }
}
