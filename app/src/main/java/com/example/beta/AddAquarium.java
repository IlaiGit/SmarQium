package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Adding and aquarium and attach it to user
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
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(aqq);

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

        FirebaseDatabase.getInstance().getReference("TestsArea")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(Test);


    }
}