package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AddAquarium extends AppCompatActivity {
    EditText nickname;
    EditText amount;
    EditText width;
    EditText height;
    EditText depth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_aquarium);

        nickname = (EditText) findViewById(R.id.nickname);
        amount = (EditText) findViewById(R.id.amount);
        width = (EditText) findViewById(R.id.width);
        height = (EditText) findViewById(R.id.height);
        depth = (EditText) findViewById(R.id.depth);

    }

    public void ConnectAndCreate(View view) {
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


        Aquarium aqq =  new Aquarium(NICKNAME, AMOUNT, WIDTH, HEIGHT, DEPTH);

        FirebaseDatabase.getInstance().getReference("Aquariums")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(aqq);




    }
}