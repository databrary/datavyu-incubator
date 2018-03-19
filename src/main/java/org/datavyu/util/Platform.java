package org.datavyu.util;

public enum Platform {
    //TODO: add any properties related to the Platform
    //ex: Plugins, list of hot key ...

    MAC,
    WINDOWS,
    LINUX,
    UNKNOWN;

    public static final Platform current;

    static {
        String os = System.getProperty("os.name", "generic").toLowerCase();

        if ((os.contains("mac")) || (os.contains("darwin")))
            current = MAC;
        else if (os.contains("win"))
            current = WINDOWS;
        else if (os.contains("nux"))
            current = LINUX;
        else
            current = UNKNOWN;
    }
}
