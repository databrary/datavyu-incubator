package org.datavyu.util;

public enum Platform {
    //TODO: add any properties related to the Platform
    //ex: Plugins, list of hot key ...

    MAC,
    WINDOWS,
    LINUX,
    UNKNOWN;

    public static Platform getPlatform(){
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
