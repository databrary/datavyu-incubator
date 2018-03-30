package org.datavyu.mediaplayers;


import javafx.util.Duration;
import org.datavyu.mediaplayers.javafx.JfxMediaPlayer;
import org.datavyu.util.Identifier;
import org.datavyu.util.Rate;

public interface StreamViewer {

    Identifier getIdentifier();

    void play();

    void pause();

    void stop();

    void seek(long time);

    void shuttle(Rate rate);

    void jog(int direction, Duration time);

    long getCurrentTime();

    float getFrameRate();

    Rate getRate(); //Should return the rate of the

    void setRate(Rate rate); //Should check the state of the video controller

    void add(StreamViewer stream);
}
