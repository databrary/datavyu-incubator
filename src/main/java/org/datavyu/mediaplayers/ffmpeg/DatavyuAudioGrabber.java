package org.datavyu.mediaplayers.ffmpeg;

import org.datavyu.madias.DatavyuMedia;

public class DatavyuAudioGrabber {

    private DatavyuAudioGrabber() {
    }

    public static DatavyuAudioGrabber createAudioGrabber(DatavyuMedia media){
        return new DatavyuAudioGrabber();
    }
}
