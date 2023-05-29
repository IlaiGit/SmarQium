package com.example.beta;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;


/**
 * @author		Ilai Shimoni <ilaigithub@gmail.com>
 * @version	    2.2
 * @since		6/11/22
 *  this class provides user the ability to register his aquarium into the software
 */

public class AddAquarium extends AppCompatActivity {

    /**
     * xml variables definition
     */
    EditText nickname;
    EditText amount;
    EditText width;
    EditText height;
    EditText depth;
    BroadcastReceiver broadcastReceiver;
    AlertDialog.Builder adb;
    String MAC;


    @Override
    protected void onStart() {
        super.onStart();

        adb = new AlertDialog.Builder(this);

        adb.setTitle("Enter your mac address");
        adb.setMessage("please enter the device's id found on your box");
        final EditText eT = new EditText(this);
        adb.setView(eT);
        adb.setCancelable(false);
        adb.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(eT.getText().toString().isEmpty()){
                    Intent intent = new Intent(AddAquarium.this, AquariumPicker.class);
                    startActivity(intent);
                }
                else{
                    MAC = eT.getText().toString();
                    dialogInterface.dismiss();
                }

            }
        });
        adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(AddAquarium.this, AquariumPicker.class);
                startActivity(intent);
            }
        });
        AlertDialog ad = adb.create();
        ad.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_aquarium);

        /**
         * connecting xml components to java variables
         */

        nickname = (EditText) findViewById(R.id.nickname);
        amount = (EditText) findViewById(R.id.amount);
        width = (EditText) findViewById(R.id.width);
        height = (EditText) findViewById(R.id.height);
        depth = (EditText) findViewById(R.id.depth);

    }

    protected void unregisterNetwork(){
        try {
            unregisterReceiver(broadcastReceiver);

        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetwork();
    }

    protected void registerNetworkBrodcastReciever(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    public void ConnectAndCreate(View view) {

        //check internet connection before proceeding
        broadcastReceiver = new NetworkConnectionReciever();
        registerNetworkBrodcastReciever();

        if(nickname.getText().toString().isEmpty()){
            nickname.setError("Please enter a nickname");
            nickname.requestFocus();
            return;
        }
        if(amount.getText().toString().isEmpty()){
            amount.setError("please enter the amount of fish");
            amount.requestFocus();
            return;
        }
        if(width.getText().toString().isEmpty()){
            width.setError("please enter Aquarium's width");
            width.requestFocus();
            return;
        }
        if(height.getText().toString().isEmpty()){
            height.setError("please enter Aquarium's height");
            height.requestFocus();
            return;
        }
        if(depth.getText().toString().isEmpty()){
            depth.setError("please enter Aquarium's depth");
            depth.requestFocus();
            return;
        }

        String NICKNAME = nickname.getText().toString();
        String AMOUNT = amount.getText().toString();
        String WIDTH = width.getText().toString();
        String HEIGHT = height.getText().toString();
        String DEPTH = depth.getText().toString();
        // String Mac is asked for already

        //get date
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);




        /**
         * Upload data to firebase
         */
        Aquarium aqq =  new Aquarium(NICKNAME, AMOUNT, WIDTH, HEIGHT, DEPTH, MAC);

        FirebaseDatabase.getInstance().getReference("Aquariums")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()+ " " + MAC).setValue(aqq);

        Toast.makeText(this, "your aquarium was created!", Toast.LENGTH_SHORT).show();
        nickname.setText("");
        amount.setText("");
        width.setText("");
        height.setText("");
        depth.setText("");


        /**
         * creating test field
         */
        boolean bool = false;
        double tempC = 0;
        double tempF = 0;
        double WaterClarity = 0;
        double WaterHeight = 0;
        double WaterOpacity = 0;
        boolean ActivateFeeder = false;


        Tests Test = new Tests(bool, tempC, tempF, WaterClarity, WaterHeight, WaterOpacity, ActivateFeeder);
        FirebaseDatabase.getInstance().getReference("TestsArea").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+  " " +System.currentTimeMillis() + " " + MAC).setValue(Test);

        Intent intent = new Intent(AddAquarium.this, AquariumPicker.class);
        startActivity(intent);


    }
}