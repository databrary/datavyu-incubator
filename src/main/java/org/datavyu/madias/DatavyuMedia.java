package org.datavyu.madias;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableMap;
import javafx.scene.media.Media;
import javafx.util.Duration;

public interface DatavyuMedia {

    //TODO: handle Media Errors

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
}
