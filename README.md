# 📋 LogViewer Library

A simple and lightweight Android library that displays logs in a floating overlay window — great for **debugging**, **testing**, or **monitoring app behavior** in real-time.

---

## ✨ Features

- 📦 Easy integration with any Android project
- 🪵 Real-time display of logs inside the app
- 📤 Option to print custom logs directly to the view
- 🧼 One-click clear all logs
- 📱 Overlay window that floats above your app
- 💡 Minimal design with support for dark/light themes

---

## 📲 Demo App

A sample app is included in the repository to demonstrate:

- Showing and hiding the log overlay
- Writing log messages
- Clearing all logs

---

## 🚀 Getting Started

### 1. Add the library module

Clone this repository and add the `logviewerlibrary` module to your project.

In your **`settings.gradle`**:

```kotlin
include ':logviewerlibrary'
project(':logviewerlibrary').projectDir = new File(settingsDir, 'logviewerlibrary')

In your app/build.gradle:

dependencies {
    implementation project(":logviewerlibrary")
}
```

### 2. Required Permissions
In your **`AndroidManifest.xml`** 

```
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_SPECIAL_USE"/>
```
### 3. Start the Log Viewer Overlay
In your **`activity or service`**:

```
Intent intent = new Intent(context, LogOverlayService.class);
ContextCompat.startForegroundService(context, intent);
```

<img width="405" height="760" alt="Logo" src="https://github.com/user-attachments/assets/10a4cfe2-c734-417b-9ae9-991b4d0ca47a" />
<img width="388" height="786" alt="home page" src="https://github.com/user-attachments/assets/08697fab-a10d-44c6-9862-06756363dca0" />
<img width="401" height="785" alt="logs" src="https://github.com/user-attachments/assets/1cb3285f-2e4b-40a8-bd0e-c32c44a1f65f" />

