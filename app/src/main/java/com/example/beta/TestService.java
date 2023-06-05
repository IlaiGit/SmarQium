package com.example.beta;

import static android.content.Intent.getIntent;
import static android.content.Intent.getIntentOld;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
/**
 * @author		Ilai Shimoni ilaigithub@gmail.com
 * @version	    3.0
 * @since		12/10/22
 * this class performs a background/foreground service which
 * operates with the database and is checking time, writing to the database and alerts users accordingly
 */
public class TestService extends Service {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    ArrayList<Double> clarityList = new ArrayList<Double>();
    ArrayList<Double> PHlist = new ArrayList<Double>();
    ArrayList<Double> tempCList = new ArrayList<Double>();
    ArrayList<Double> tempFList = new ArrayList<Double>();
    ArrayList<Double> heightList = new ArrayList<Double>();
    int numberOfTests;

    private static final String TAG = "TestService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("TestsArea");

        /**
         * data pull for all data related to the current user
         * data is stored on a list
         */

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numberOfTests = 0;
                clarityList.clear();
                PHlist.clear();
                heightList.clear();
                tempCList.clear();
                tempFList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Tests testTmp = dataSnapshot.getValue(Tests.class);

                    String[] partsOfId = dataSnapshot.getKey().split(" ");
                    if (userID.equals(partsOfId[0])) {
                        clarityList.add(testTmp.getWaterClarity());
                        PHlist.add(testTmp.getWaterPH());
                        heightList.add(testTmp.getWaterHeight());
                        tempCList.add(testTmp.getTempC());
                        tempFList.add(testTmp.getTempF());

                        numberOfTests++;
                    }
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        /**
        * creation of a timer - a data upload to the database occurs every 30 minutes = 1000 ms * 60 seconds * 30 minutes
        */

        CountDownTimer countDownTimer = new CountDownTimer(1000 * 60 * 30, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e(TAG, "onTick:" + millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                /**
                 * creating test field
                 * boolean bool = false;
                 *         double tempC = 0;
                 *         double tempF = 0;
                 *         double WaterClarity = 0;
                 *         double WaterHeight = 0;
                 *         double WaterOpacity = 0;
                 *         boolean ActivateFeeder = false;
                 *
                 *
                 *         Tests Test = new Tests(bool, tempC, tempF, WaterClarity, WaterHeight, WaterOpacity, ActivateFeeder);
                 *         FirebaseDatabase.getInstance().getReference("TestsArea").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+  " " +System.currentTimeMillis() + " " + MAC).setValue(Test);
                 *
                 *         this code doesn't run as connection with sensors isn't available
                 */

                /**
                 * creating a notification for user, to let know that a data has been received
                 */

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    NotificationChannel channel = new NotificationChannel("CheckApp", "dataSample", NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager manager = getSystemService(NotificationManager.class);
                    manager.createNotificationChannel(channel);
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(TestService.this, "CheckApp");
                builder.setContentTitle("Smarqium");
                builder.setContentText("You received a new data sample");
                builder.setSmallIcon(R.drawable.ic_water);
                builder.setAutoCancel(true);

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(6, builder.build());
            }
        }.start();

        final String CHANNEL_ID = "Foreground Service";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_LOW);


        /**
         * preparing and running background tasks, even when app is closed
         */
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentText("Foreground service APP")
                .setContentTitle("Smarqium");
        startForeground(1001, notification.build());

        return START_STICKY;
    }
}
