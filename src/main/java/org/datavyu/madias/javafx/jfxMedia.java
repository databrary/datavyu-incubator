package org.datavyu.madias.javafx;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableMap;
import javafx.scene.media.Media;
import javafx.util.Duration;
import org.datavyu.madias.DatavyuMedia;

import java.io.File;

public class jfxMedia implements DatavyuMedia {

    private Media media;

    private jfxMedia(File sourceFile){
        this.media = new Media(sourceFile.toURI().toString());
    }

    @Override
    public int getWidth() { return this.media.getWidth(); }

    @Override
    public ReadOnlyIntegerProperty widthProperty() { return this.media.widthProperty(); }

    @Override
    public int getHeight() { return this.media.getHeight(); }

    @Override
    public ReadOnlyIntegerProperty heightProperty() { return this.media.heightProperty(); }

    @Override
    public Duration getAbsoluteDuration() { return this.media.getDuration();}

    @Override
    public Duration getRelativeDuration() { return null; }

    @Override
    public ReadOnlyObjectProperty<Duration> durationProperty() { return this.media.durationProperty(); }

    @Override
    public ObservableMap<String, Duration> getAbsoluteMarkers() { return this.media.getMarkers(); }

    @Override
    public ObservableMap<String, Duration> getRelativeMarkers() {
        return null;
    }

    @Override
    public String getSource() { return this.media.getSource(); }

    public static DatavyuMedia getMedia(File sourceFile) { return new jfxMedia(sourceFile); }

    @Override
    public Media getMedia(){return this.media; }
}
