package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AquariumPicker extends AppCompatActivity implements AdapterView.OnItemClickListener {

    TextView show_name;
    Button add;
    ListView lv;

    Intent intent;


    private FirebaseUser user;
    private DatabaseReference reference, Aqref;
    private String userID;

    ArrayList<String> aqList = new ArrayList<String>();
    ArrayList<String> aqValues = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquarium_picker);

        add = (Button) findViewById(R.id.add);
        add.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2E3545")));

        lv = (ListView) findViewById(R.id.lv);
        show_name = (TextView) findViewById(R.id.show_name);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        Aqref = FirebaseDatabase.getInstance().getReference("Aquariums");
        userID = user.getUid();

        lv.setOnItemClickListener(this);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo UserName = snapshot.getValue(UserInfo.class);

                if (UserName != null) {
                    String FirstName = UserName.FirstName;
                    show_name.setText("Hello " + FirstName + " ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AquariumPicker.this, "something went wrong", Toast.LENGTH_LONG).show();
            }
        });

        ValueEventListener AqListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                aqList.clear();
                aqValues.clear();

                for(DataSnapshot data : snapshot.getChildren()) {

                    String ukey = user.getUid();
                    String dat = data.getKey();

                    if(ukey.equals(dat)){
                        aqList.add(ukey);
                        Aquarium AQtmp = data.getValue(Aquarium.class);
                        String AQname = AQtmp.NickName;
                        aqValues.add(AQname);
                    }

                    /*
                    System.out.println(aqList);
                    System.out.println(aqValues);
                     */

                    String[] arr = new String[aqValues.size()];
                    for(int i=0; i<arr.length; i++){
                        arr[i] = aqValues.get(i);
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AquariumPicker.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arr);
                    lv.setAdapter(arrayAdapter);

                }



                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        Aqref.addValueEventListener(AqListener);
    }

        public void redirect_logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, Login.class));
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        intent = new Intent(this, Analysis.class);
        intent.putExtra("key", aqValues.get(pos));
        startActivity(intent);

    }

    public void AddAquarium(View view) {
        startActivity(new Intent(this, AddAquarium.class));
    }

}