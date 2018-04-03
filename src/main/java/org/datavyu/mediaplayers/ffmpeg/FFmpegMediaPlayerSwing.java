package org.datavyu.mediaplayers.ffmpeg;


import javafx.util.Duration;
import org.datavyu.madias.DatavyuMedia;
import org.datavyu.mediaplayers.StreamViewer;
import org.datavyu.util.Identifier;
import org.datavyu.util.Rate;

import javax.swing.*;


/**
 * This is an FFmpeg Media Playerd displayed in a Swing Frame
 */
public class FFmpegMediaPlayerSwing implements StreamViewer{
    private final Identifier identifier;
    private DatavyuMedia media;

    //TODO: add a frame grabber and an audio grabber and a MovieStreamer to sync the two streams
    // FFmpeg Media Player will talk to the MovieStreamer


    public FFmpegMediaPlayerSwing(Identifier identifier, DatavyuMedia media) {
        this.identifier = identifier;
        this.media = media;
    }

    @Override
    public Identifier getIdentifier() {
        return null;
    }

    @Override
    public void play() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void seek(long time) {

    }

    @Override
    public void shuttle(Rate rate) {

    }

    @Override
    public void jog(int direction, Duration time) {

    }

    @Override
    public long getCurrentTime() {
        return 0;
    }

    @Override
    public float getFrameRate() {
        return 0;
    }

    @Override
    public Rate getRate() {
        return null;
    }

    @Override
    public void setRate(Rate rate) {

    }

    @Override
    public void add(StreamViewer stream) {

    }
}
