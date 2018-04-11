package org.datavyu.mediaplayers.ffmpeg;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.datavyu.madias.DatavyuMedia;
import org.datavyu.mediaplayers.StreamViewer;
import org.datavyu.util.Converter;
import org.datavyu.util.Identifier;
import org.datavyu.util.Rate;


/**
 * This the JavaFX version of the FFmpeg Media Player
 */
public class FFmpegMediaPlayer extends Stage implements StreamViewer {

    private final Identifier identifier;
    private DatavyuMedia media;
    private FFmpegMoviePlayer mp; //FFmpegMoviePlayer extends StackPane
    private Slider timeSlider;


    private FFmpegMediaPlayer(Identifier identifier, DatavyuMedia media) {
        this.identifier = identifier;
        this.media = media;

        mp = new FFmpegMoviePlayer(media);

        Scene scene = new Scene(mp, mp.getImageWidth(), mp.getImageHeight());

        System.out.println(" Time: " + mp.getDuration() + " TimeStamp: "+ Converter.convertMStoTimestamp(mp.getDuration()/1000));
        timeSlider =  new Slider(0, mp.getDuration(),0);
        timeSlider.setMajorTickUnit(100000);
        timeSlider.setShowTickMarks(true);

//        timeSlider.valueProperty().bindBidirectional(mp.currentTimeProperty());

        this.setTitle(media.getSource());
        this.setScene(scene);
        this.setOnCloseRequest(event -> Platform.exit());
        this.show();
    }

    public static FFmpegMediaPlayer createFFmpegMediaPlayer(Identifier identifier, DatavyuMedia media){
        return new FFmpegMediaPlayer(identifier,media);
    }

    @Override
    public Slider getStreamTimeSlider() { return timeSlider; }

    @Override
    public Identifier getIdentifier() { return this.identifier; }

    @Override
    public void play() { this.mp.play(); }

    @Override
    public void pause() { this.mp.pause(); }

    @Override
    public void stop() { this.mp.stop(); }

    @Override
    public void shuttle(Rate rate) {
        this.setRate(rate);
        this.mp.shuttle(rate);
    }

    @Override
    public void jog(int direction, Duration time) { this.mp.jog(direction,time); }

    @Override
    public long getCurrentTime() { return this.mp.currentTimeProperty().getValue(); }

    @Override
    public Rate getRate() { return this.mp.getRate(); }

    @Override
    public void setRate(Rate rate) { this.mp.setRate(rate); }

    @Override
    public int getVolume() { return this.mp.getVolume(); }

    @Override
    public void setVolume(int volume) { this.mp.setVolume(volume); }

    @Override
    public void visible() { super.show(); }

    @Override
    public void hide() { super.hide(); }

    @Override
    public void close() { }

    @Override
    public void back(Duration time) { }

    @Override
    public void seekTime(long timeStamp) { }

    @Override
    public float getFrameRate() { return this.mp.getFrameRate(); }

    @Override
    public void setFrameRate(float fps) {  }

    @Override
    public int getCurrentFrame() { return this.mp.getCurrentFrame(); }
}
