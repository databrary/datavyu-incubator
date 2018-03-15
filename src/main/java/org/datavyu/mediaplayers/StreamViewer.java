package org.datavyu.mediaplayers;

import org.datavyu.views.VideoController.Rate;

public interface StreamViewer {

    void play();

    void pause();

    void stop();

    void seek(long time);

    long getCurrentTime();

    float getFrameRate();

    Rate getRate(); //Should return the rate of the

    void setRate(Rate rate); //Should check the state of the video controller

    void shuttle(Rate value);
}
