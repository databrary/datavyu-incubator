package org.datavyu.util;

public class Converter {

    public static String convertMStoTimestamp(long time) {
        long hours = Math.round(Math.floor((time / 1000.0 / 60.0 / 60.0)));
        long minutes = Math.round(Math.floor(time / 1000.0 / 60.0 - (hours * 60)));
        long seconds = Math.round(Math.floor(time / 1000.0 - (hours * 60 * 60) - (minutes * 60)));
        long mseconds = Math.round(Math.floor(time - (hours * 60 * 60 * 1000) - (minutes * 60 * 1000) - (seconds * 1000)));

        return String.format("%02d:%02d:%02d:%03d", hours, minutes, seconds, mseconds);
    }

    public static long convertTimestampToMS(String timestamp) {

        String[] s = timestamp.split(":");
        if(s.length == 1){
            return Long.valueOf(timestamp);
        }
        long hours = Long.valueOf(s[0]) * 60 * 60 * 1000;
        long minutes = Long.valueOf(s[1]) * 60 * 1000;
        long seconds = Long.valueOf(s[2]) * 1000;
        long mseconds = Long.valueOf(s[3]);

        return hours + minutes + seconds + mseconds;
    }
}
