package com.example.beta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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

    AlertDialog.Builder adb;



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

    public void ConnectAndCreate(View view) {

        /**
         * check for errors
         */

        if(nickname.getText().toString().isEmpty()){
            nickname.setError("Please enter a nickname");
            nickname.requestFocus();
            return;
        }
        if(amount.getText().toString().isEmpty()){
            amount.setError("please enter the amount if fish");
            amount.requestFocus();
            return;
        }
        if(width.getText().toString().isEmpty()){
            width.setError("please enter Aquarium's width");
            width.requestFocus();
            return;
        }
        if(height.getText().toString().isEmpty()){
            height.setError("please enter Aquarium's width");
            height.requestFocus();
            return;
        }
        if(depth.getText().toString().isEmpty()){
            depth.setError("please enter Aquarium's width");
            depth.requestFocus();
            return;
        }

        String NICKNAME = nickname.getText().toString();
        String AMOUNT = amount.getText().toString();
        String WIDTH = width.getText().toString();
        String HEIGHT = height.getText().toString();
        String DEPTH = depth.getText().toString();


        /**
         * Upload data to firebase
         */
        Aquarium aqq =  new Aquarium(NICKNAME, AMOUNT, WIDTH, HEIGHT, DEPTH);

        FirebaseDatabase.getInstance().getReference("Aquariums")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()+" " +System.currentTimeMillis()).setValue(aqq);

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
        float tempC = 0;
        float tempF = 0;

        Tests Test = new Tests(bool, tempC, tempF);
        FirebaseDatabase.getInstance().getReference("TestsArea").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(Test);

        /**
         * alert dialog for selecting a profile pic or animation
         */

        adb = new AlertDialog.Builder(this);

        adb.setTitle("before we continue");
        adb.setMessage("we would like to create a profile picture for your aquarium, please select any of the options below");

        adb.setPositiveButton("take a picture", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(AddAquarium.this, storage.class);
                startActivity(intent);

            }
        });
        adb.setNegativeButton("no thanks", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                startActivity(new Intent(AddAquarium.this, AquariumPicker.class));

            }
        });
        adb.setNeutralButton("create animation", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Toast.makeText(AddAquarium.this, "create animation", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog ad = adb.create();
        ad.show();





    }
}