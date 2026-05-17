package com.example.music;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {
   public static MediaPlayer mediaPlayer;


   @Override
   public void onCreate() {
       super.onCreate();
       mediaPlayer = MediaPlayer.create(this, R.raw.vi_sao_toi_la_gay);
       mediaPlayer.setLooping(true); // Lặp lại nhạc
   }


   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
       // 1. Tạo Notification Channel
       NotificationChannel channel = null;
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           channel = new NotificationChannel("noti_channel_id", "My Service Channel", NotificationManager.IMPORTANCE_LOW);
       }
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           NotificationManager manager = getSystemService(NotificationManager.class);
           if (manager != null) {
               manager.createNotificationChannel(channel);
           }
       }

       // 2. Tạo Notification
       Notification notification = new NotificationCompat.Builder(this, "noti_channel_id")
                .setContentTitle("Dang chay")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();


       // 3. Chỉ định là Foreground Service
       startForeground(1, notification);


       if (mediaPlayer != null) {
           mediaPlayer.start(); // Bắt đầu phát nhạc
       }


       return START_STICKY;
   }


   @Override
   public void onDestroy() {
       super.onDestroy();
       if (mediaPlayer != null) {
           mediaPlayer.stop(); // Dừng nhạc khi Service bị hủy
           mediaPlayer.release();
           mediaPlayer = null;
       }
   }


   @Nullable
   @Override
   public IBinder onBind(Intent intent) {
       return null;
   }
}
