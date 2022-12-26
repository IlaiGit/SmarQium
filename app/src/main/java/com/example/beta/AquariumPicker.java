package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AquariumPicker extends AppCompatActivity {

    TextView show_name;
    Button add;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquarium_picker);

        add = (Button) findViewById(R.id.add);
        add.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2E3545")));

        show_name = (TextView) findViewById(R.id.show_name);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo UserName = snapshot.getValue(UserInfo.class);

                if(UserName != null){
                    String FirstName = UserName.FirstName;
                    show_name.setText("Hello " + FirstName + " ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AquariumPicker.this, "something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void redirect_logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, Login.class));
    }

    public void AddAquarium(View view) {
        startActivity(new Intent(this, AddAquarium.class));
    }
}