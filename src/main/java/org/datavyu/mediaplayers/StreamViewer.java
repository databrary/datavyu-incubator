package org.datavyu.mediaplayers;

public interface StreamViewer {

    void play();

    void pause();

    void stop();

    void seek(long time);

    long getCurrentTime();

    float getFrameRate();

    float getRate();

    void setRate(float rate);
}
