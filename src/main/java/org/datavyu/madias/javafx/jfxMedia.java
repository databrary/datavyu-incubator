package org.datavyu.madias.javafx;

import javafx.scene.media.Media;
import org.datavyu.madias.DatavyuMedia;

import java.io.File;

public class jfxMedia implements DatavyuMedia {

    private Media media;

    public jfxMedia(File sourceFile){
        this.media = new Media(sourceFile.toURI().toString());
    }

    public Media getMedia() { return media; }
}
