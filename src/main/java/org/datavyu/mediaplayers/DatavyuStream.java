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
    public void seek(long time) {

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
    public void shuttle(Rate rate) {
        streams.entrySet().parallelStream().forEach((stream) -> {
            stream.getValue().shuttle(rate);
        });
    }

    @Override
    public void add(StreamViewer stream){
        this.streams.put(stream.getIdentifier(),stream);
    }

}
