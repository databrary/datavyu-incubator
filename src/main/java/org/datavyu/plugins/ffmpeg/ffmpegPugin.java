package org.datavyu.plugins.ffmpeg;

import javafx.scene.image.Image;
import org.datavyu.madias.DatavyuMedia;
import org.datavyu.mediaplayers.StreamViewer;
import org.datavyu.plugins.Plugin;
import org.datavyu.util.Filter;
import org.datavyu.util.Identifier;
import org.datavyu.util.Platform;

import java.util.List;
import java.util.UUID;

public class ffmpegPugin implements Plugin{
    @Override
    public StreamViewer getNewStreamViewer(Identifier identifier, DatavyuMedia sourceMedia) {
        return null;
    }

    @Override
    public Class<? extends StreamViewer> getViewerClass() {
        return null;
    }

    @Override
    public Image getTypeIcon() {
        return null;
    }

    @Override
    public String getPluginName() {
        return null;
    }

    @Override
    public UUID getPluginUUID() {
        return null;
    }

    @Override
    public String getNamespace() {
        return null;
    }

    @Override
    public Filter[] getFilters() {
        return new Filter[0];
    }

    @Override
    public List<Platform> getValidPlatforms() {
        return null;
    }
}
