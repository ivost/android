package com.example.mylibrary;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class Logger {

    private static final String TAG = "<<<IVO>>>";
    private String mPrefix = TAG;
    private boolean mDebug = true;
    private BufferedWriter mWriter;
    private Context mContext;
    private File logFile = new File(Environment.getExternalStorageDirectory().getPath() + "/log");

    public Logger() {
        init();
    }
    public Logger(String prefix, Context context) {
        mPrefix = prefix;
        mContext = context;
        init();
    }

    private boolean init() {
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            mWriter = new BufferedWriter(new FileWriter(logFile, true));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        return true;
    }

    public boolean clear() {
        try {
            if (logFile.exists()) {
                logFile.delete();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        return true;
    }

    public void debug(String text) {
        if (text == null)
            return;
        try {
            Log.w(mPrefix, text);
            if (mDebug) {
                write("DEBUG", text);
            }
        } catch (Exception ex) {
            // ignore exception
            Log.e(TAG, ex.getMessage());
        }
    }

    /**
     * Displays toast for 1 sec
     * @param text
     */
    public void toast(String text) {
        if (text == null || mContext == null) {
            return;
        }
        final Toast toast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        toast.show();

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                toast.cancel();
//            }
//        }, 1000);
    }

    public void setDebug(boolean is_debug) {
        mDebug = is_debug;
    }

    public boolean isDebug() {
        return mDebug;
    }

    public static void LOG(String text) {
        Log.w("", text);
    }

    public void error(String text) {
        Log.e(mPrefix, text);
        write("ERROR", text);
    }

    public void error(Exception e) {
        if (e != null)
            error(e.getMessage());
        else error("EXCEPTION");
    }

    public void write(String level, String text) {
        if (mWriter != null)
            try {
                mWriter.write(String.format("%s %s %s: %s\n", level, TimeUtil.now(), mPrefix, text));
                mWriter.flush();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
    }
}