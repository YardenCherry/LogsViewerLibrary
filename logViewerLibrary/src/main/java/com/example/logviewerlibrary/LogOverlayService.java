package com.example.logviewerlibrary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LogOverlayService extends Service {

    private WindowManager windowManager;
    private View overlayView;
    private List<String> logList = new ArrayList<>();
    private LogAdapter logAdapter;
    private RecyclerView logRecyclerView;
    private Process logcatProcess;

    private final BroadcastReceiver clearLogsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (logcatProcess != null) {
                logcatProcess.destroy();
                logcatProcess = null;
            }
            logList.clear();
            logAdapter.notifyDataSetChanged();
            startReadingLogs();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundNotification();
        registerReceiver(clearLogsReceiver, new IntentFilter("com.example.logviewerlibrary.CLEAR_LOGS"), Context.RECEIVER_NOT_EXPORTED);
        createOverlay();
    }

    private void startForegroundNotification() {
        String channelId = "log_viewer_channel";
        String channelName = "Log Viewer Overlay";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("LogViewer רץ ברקע")
                .setContentText("הצגת לוגים בזמן אמת")
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .build();

        startForeground(1, notification);
    }

    private void createOverlay() {
        overlayView = LayoutInflater.from(this).inflate(R.layout.view_log_overlay, null);

        logRecyclerView = overlayView.findViewById(R.id.logRecyclerView);
        logRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        logAdapter = new LogAdapter(logList);
        logRecyclerView.setAdapter(logAdapter);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.END;
        params.x = 20;
        params.y = 100;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(overlayView, params);

        Button btnClose = overlayView.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> stopSelf());

        startReadingLogs();
    }

    private void startReadingLogs() {
        new Thread(() -> {
            try {
                logcatProcess = Runtime.getRuntime().exec("logcat");
                BufferedReader reader = new BufferedReader(new InputStreamReader(logcatProcess.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    String finalLine = line;
                    new Handler(Looper.getMainLooper()).post(() -> {
                        logList.add(finalLine);
                        logAdapter.notifyItemInserted(logList.size() - 1);
                        logRecyclerView.scrollToPosition(logList.size() - 1);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (windowManager != null && overlayView != null) {
            windowManager.removeView(overlayView);
        }
        if (logcatProcess != null) {
            logcatProcess.destroy();
        }
        unregisterReceiver(clearLogsReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
