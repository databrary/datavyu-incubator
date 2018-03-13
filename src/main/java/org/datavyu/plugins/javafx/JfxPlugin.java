package org.datavyu.plugins.javafx;

import com.google.common.collect.Lists;
import javafx.scene.image.Image;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.datavyu.madias.DatavyuMedia;
import org.datavyu.mediaplayers.StreamViewer;
import org.datavyu.mediaplayers.javafx.JfxMediaPlayer;
import org.datavyu.plugins.Plugin;
import org.datavyu.util.Filter;
import org.datavyu.util.FilterNames;
import org.datavyu.util.Identifier;
import org.datavyu.util.Platform;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.UUID;


public class JfxPlugin implements Plugin {

    private static final List<Platform> VALID_OPERATING_SYSTEMS = Lists.newArrayList(
            Platform.WINDOWS, Platform.MAC, Platform.LINUX);

    private static final UUID pluginUUID = UUID.nameUUIDFromBytes("plugin.jfx".getBytes());

    private static final Filter VIDEO_FILTER = new Filter() {
        final SuffixFileFilter ff;
        final List<String> ext;

        {
            ext = Lists.newArrayList(".mp4", ".m4v");
            ff = new SuffixFileFilter(ext, IOCase.INSENSITIVE);
        }

        @Override
        public FileFilter getFileFilter() {
            return ff;
        }

        @Override
        public String getName() { return FilterNames.MP4.getFilterName(); }

        @Override
        public Iterable<String> getExtensions() {
            return ext;
        }
    };

    private static final Filter[] FILTERS = new Filter[]{VIDEO_FILTER};

    @Override
    public String getNamespace() {
        return "datavyu.video";
    }

    @Override
    public UUID getPluginUUID() {return pluginUUID; }

    @Override
    public Filter[] getFilters() {
        return FILTERS;
    }

    @Override
    public StreamViewer getNewStreamViewer(final Identifier identifier, final DatavyuMedia sourceMedia) {
        return new JfxMediaPlayer(identifier, sourceMedia);
    }

    @Override
    public String getPluginName() {
        return "JavaFX Video";
    }

    @Override
    public Image getTypeIcon() {
        return new Image(new File("/icons/vlc_cone.png").toURI().toString());
    }

    @Override
    public Class<? extends StreamViewer> getViewerClass() {
        return JfxMediaPlayer.class;
    }

    @Override
    public List<Platform> getValidPlatforms() {
        return VALID_OPERATING_SYSTEMS;
    }
}
