package org.datavyu.madias;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableMap;
import javafx.scene.media.Media;
import javafx.util.Duration;

import java.io.File;

//TODO: Remove ffmpeg and javafx Media and have one common media for all the Media Players
public interface DatavyuMedia {

    //TODO: handle Media Errors

    //TODO: need elaborate and extend this class

    int getWidth();

    ReadOnlyIntegerProperty widthProperty();

    int getHeight();

    ReadOnlyIntegerProperty heightProperty();

    Duration getAbsoluteDuration();

    Duration getRelativeDuration();

    ReadOnlyObjectProperty<Duration> durationProperty();

    ObservableMap<String, Duration> getAbsoluteMarkers();

    ObservableMap<String, Duration> getRelativeMarkers();

    String getSource();

    Media getMedia();

    File getSourceFile();
}
