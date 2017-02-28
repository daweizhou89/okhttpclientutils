package com.github.daweizhou89.okhttpclientutils;

/**
 * Created by daweizhou89 on 16/8/4.
 */

public class Config {

    protected static boolean sAssertMainThread = false;

    protected static boolean sDebugLog = false;

    protected static String sUserAgent;

    protected Config() {}

    public Config setAssertMainThread(boolean assertMainThread) {
        sAssertMainThread = assertMainThread;
        return this;
    }

    public Config setDebugLog(boolean debugLog) {
        sDebugLog = debugLog;
        return this;
    }

    public Config setUserAgent(String userAgent) {
        Config.sUserAgent = userAgent;
        return this;
    }

    public static String getUserAgent() {
        return Config.sUserAgent;
    }

}
