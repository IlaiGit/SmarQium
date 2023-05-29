package com.example.beta;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * public class backgroundService extends Service {
 *
 *     private static final int NOTIF_ID = 1;
 *     private static final String NOTIF_CHANNEL_ID = "Channel_Id";
 *
 *     @Nullable
 *     @Override
 *     public IBinder onBind(Intent intent) {
 *         return null;
 *     }
 *
 *     @Override
 *     public int onStartCommand(Intent intent, int flags, int startId){
 *
 *     Thread.sleep(30 *   // minutes to sleep
 *              60 *   // seconds to a minute
 *              1000); // milliseconds to a second
 *
 *         boolean bool = false;
 *         double tempC = 0;
 *         double tempF = 0;
 *         double WaterClarity = 0;
 *         double WaterHeight = 0;
 *         double WaterOpacity = 0;
 *         boolean ActivateFeeder = false;
 *
 *
 *         Tests Test = new Tests(bool, tempC, tempF, WaterClarity, WaterHeight, WaterOpacity, ActivateFeeder);
 *         FirebaseDatabase.getInstance().getReference("TestsArea").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+  " " +strDate + " " + MAC).setValue(Test);
 *
 *
 *         startForeground();
 *
 *         return super.onStartCommand(intent, flags, startId);
 *     }
 *
 *     private void startForeground() {
 *         Intent notificationIntent = new Intent(this, AddAquarium.class);
 *
 *         PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
 *
 *         startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
 *                 NOTIF_CHANNEL_ID)
 *                 .setOngoing(true)
 *                 .setSmallIcon(androidx.constraintlayout.widget.R.drawable.abc_ic_star_black_48dp)
 *                 .setContentTitle(getString(R.string.app_name))
 *                 .setContentText("Another test is sampled and recieved")
 *                 .setContentIntent(pendingIntent)
 *                 .build());
 *     }
 * }
 */

