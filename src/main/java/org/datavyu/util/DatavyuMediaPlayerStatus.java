package org.datavyu.util;

public class DatavyuMediaPlayerStatus {
    public enum Status {
        UNKNOWN,
        READY,
        PAUSED,
        PLAYING,
        STOPPED,
        LOCKED, // When we lock the streams
        //SEEKPLAYBACK, //May need it
        //BACKWARDPLAYBACK
    }
}
