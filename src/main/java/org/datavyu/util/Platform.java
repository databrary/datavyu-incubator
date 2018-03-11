package org.datavyu.util;

public enum Platform {
    MAC,
    WINDOWS,
    LINUX,
    UNKNOWN;

    public Platform getPlatform(){
        String os = System.getProperty("os.name");
        if (os.contains("Mac")) {
            return Platform.MAC;
        }
        if (os.contains("Win")) {
            return Platform.WINDOWS;
        }
        if (os.contains("Linux")) {
            return Platform.LINUX;
        }
        return Platform.UNKNOWN;
    }
}
