package com.example.logviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logviewerlibrary.LogViewer;

import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnShowLogs, btnClearLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogViewer.init(this);

        btnShowLogs = findViewById(R.id.btnShowLogs);
        btnClearLogs = findViewById(R.id.btnClearLogs);

        btnShowLogs.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } else {
                LogViewer.showOverlay();
            }
        });

        btnClearLogs.setOnClickListener(v -> LogViewer.clearLogs());
    }
}