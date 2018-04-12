package org.datavyu.mediaplayers.ffmpeg;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.datavyu.madias.DatavyuMedia;
import org.datavyu.mediaplayers.StreamViewer;
import org.datavyu.util.Converter;
import org.datavyu.util.Identifier;
import org.datavyu.util.Rate;

import java.util.concurrent.TimeUnit;


/**
 * This the JavaFX version of the FFmpeg Media Player
 */
public class FFmpegMediaPlayer extends Stage implements StreamViewer {

    private final Identifier identifier;
    private DatavyuMedia media;
    private FFmpegMoviePlayer mp; //FFmpegMoviePlayer extends StackPane
    private Slider timeSlider;
    private Label timeStampTextField;


    private FFmpegMediaPlayer(Identifier identifier, DatavyuMedia media) {
        this.identifier = identifier;
        this.media = media;



        this.mp = new FFmpegMoviePlayer(media);

        Scene scene = new Scene(this.mp, this.mp.getImageWidth(), this.mp.getImageHeight());

        System.out.println(" Time: " + this.mp.getDuration() + " TimeStamp: "+ Converter.convertMStoTimestamp(this.mp.getDuration()/1000));
        this.timeSlider =  new Slider(0, (this.getDuration()/1000),0);
        this.timeSlider.setMajorTickUnit(100000);
        this.timeSlider.setShowTickMarks(true);
        this.timeSlider.valueProperty().bind(this.currentTimeProperty());
        this.timeStampTextField =  new Label();
        this.timeStampTextField.textProperty().bind(currentTimeProperty().asString());

//        this.timeSlider.valueProperty().bindBidirectional(mp.currentTimeProperty());

        this.setTitle(media.getSource());
        this.setScene(scene);
        this.setOnCloseRequest(event -> Platform.exit());
        this.show();
    }

    public static FFmpegMediaPlayer createFFmpegMediaPlayer(Identifier identifier, DatavyuMedia media){
        return new FFmpegMediaPlayer(identifier,media);
    }

    @Override
    public Slider getStreamTimeSlider() { return this.timeSlider; }

    public Label getTimeStampTextField() { return this.timeStampTextField; }

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
    public LongProperty currentTimeProperty() { return this.mp.currentTimeProperty(); }

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

    @Override
    public long getDuration() { return this.mp.getDuration(); }

}
