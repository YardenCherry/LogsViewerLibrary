package com.example.logviewerlibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class LogViewer {
    private static Context context;

    public static void init(Context ctx) {
        context = ctx.getApplicationContext();
    }

    public static void showOverlay() {
        Intent intent = new Intent(context, LogOverlayService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void stopOverlay() {
        context.stopService(new Intent(context, LogOverlayService.class));
    }

    public static void clearLogs() {
        Intent intent = new Intent("com.example.logviewerlibrary.CLEAR_LOGS");
        context.sendBroadcast(intent);
    }
}