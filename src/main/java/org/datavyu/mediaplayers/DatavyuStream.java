package org.datavyu.mediaplayers;

import javafx.util.Duration;
import org.datavyu.util.Identifier;
import org.datavyu.util.Rate;

import java.util.HashMap;
import java.util.Map;


public class DatavyuStream implements StreamViewer {

    private Map<Identifier, StreamViewer> streams = new HashMap<>();
    private Identifier identifier;

    private DatavyuStream(){
        this.identifier = Identifier.generateIdentifier();

    }

    public static DatavyuStream getDatavyuStream(){
        return new DatavyuStream();
    }

    @Override
    public Identifier getIdentifier() {
        //DatavyuStream Identifier is always 0
        return this.identifier;
    }

    @Override
    public void play() {
        streams.entrySet().parallelStream().forEach((stream) -> {
            stream.getValue().play();
        });
    }

    @Override
    public void pause() {
        streams.entrySet().parallelStream().forEach((stream) -> {
            stream.getValue().pause();
        });
    }

    @Override
    public void stop() {
        streams.entrySet().parallelStream().forEach((stream) -> {
            stream.getValue().stop();
        });
    }

    @Override
    public void seekTime(long time) {

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
    public void setFrameRate(float fps) {

    }

    @Override
    public int getCurrentFrame() {
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
    public int getVolume() { return 0; }

    @Override
    public void setVolume(int volume) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void shuttle(Rate rate) {
        streams.entrySet().parallelStream().forEach((stream) -> {
            stream.getValue().shuttle(rate);
        });
    }

    /**
     * Add a stream, this method is specific to DatavyuStream
     * @param stream
     */
    public void add(StreamViewer stream){
        this.streams.put(stream.getIdentifier(),stream);
    }

}
