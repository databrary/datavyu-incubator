package org.datavyu.mediaplayers;

import javafx.scene.control.Slider;
import javafx.util.Duration;
import org.datavyu.util.Identifier;
import org.datavyu.util.Rate;

import java.util.HashMap;
import java.util.Map;

/**
 * This StreamViewer of the VideoController, control and keep track of
 * the StreamViewers.
 */
public class DatavyuStream implements StreamViewer {

    private Map<Identifier, StreamViewer> streams = new HashMap<>();
    private Identifier identifier;

    private DatavyuStream(){ this.identifier = Identifier.generateIdentifier(); }

    public static DatavyuStream createDatavyuStream(){ return new DatavyuStream(); }

    @Override
    public Slider getStreamTimeSlider() {
        return null;
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
        streams.entrySet().parallelStream().forEach((stream) -> {
            stream.getValue().seekTime(time);
        });
    }

    @Override
    public void jog(final int direction, final Duration time) {
        streams.entrySet().parallelStream().forEach((stream) -> {
            stream.getValue().jog(direction,time);
        });
    }

    /**
     * The DatavyuStream Class, must return te Video Controller
     * Master clock.
     * //TODO: Write a private clock for the VideoController, and check the streams Sync
     * @return
     */
    @Override
    public long getCurrentTime() {
        return 0;
    }

    /**
     *
     * @return
     */
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
        streams.entrySet().parallelStream().forEach((stream) -> {
            stream.getValue().setVolume(volume);
        });
    }

    @Override
    public void visible() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void close() {

    }

    @Override
    public void back(Duration time) {

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

    public Map<Identifier, StreamViewer> getStreams() {
        return streams;
    }
}
