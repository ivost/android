package com.example.mylibrary;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "MM/dd hh:mm:ss", Locale.US);
    private static final SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat(
            "hh:mm:ss", Locale.US);

    private long mStart;

    public TimeUtil() {
        reset();
    }

    public void reset() {
        mStart = System.currentTimeMillis();
    }

    public int elapsedMsec() {
        return (int) (System.currentTimeMillis() - mStart);
    }

    public float elapsedSec() {
        return (float) (0.001 * elapsedMsec());
    }

    public boolean hasSecElapsed(int sec) {
        return elapsedMsec() > 1000 * sec;
    }

    public boolean hasMsecElapsed(int msec) {
        return elapsedMsec() > msec;
    }

    public static String now() {
        return DATE_FORMAT.format(new Date());
    }

    public static String nowShort() {
        return SHORT_DATE_FORMAT.format(new Date());
    }

    public static boolean delayMsec(int msec) {
        try {
            Thread.sleep(msec);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static boolean delaySec(int sec) {
        try {
            Thread.sleep(1000*sec);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

}
