package com.example.beta;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author		Ilai Shimoni ilaigithub@gmail.com
 * @version	    3.0
 * @since		12/10/22
 * this class shows the user's connected aquariums
 */
public class AquariumPicker extends AppCompatActivity implements AdapterView.OnItemClickListener {

    /**
     * xml elements declaration as well as java local variables
     */
    TextView show_name;
    Button add;
    ListView lv;
    Intent intent;
    AlertDialog.Builder adb;
    ImageButton imageButton;
    BroadcastReceiver broadcastReceiver;



    private FirebaseUser user;
    private DatabaseReference reference, Aqref;
    private String userID;

    ArrayList<String> aqList = new ArrayList<String>();
    ArrayList<String> aqValues = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquarium_picker);

        imageButton = (ImageButton) findViewById(R.id.imageButton);

        add = (Button) findViewById(R.id.add);
        add.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2E3545")));

        lv = (ListView) findViewById(R.id.lv);
        show_name = (TextView) findViewById(R.id.show_name);
        lv.setOnItemClickListener(this);
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

    /**
     * this method shows all aquariums found on the database that belong to the user
     */

    @Override
    protected void onStart(){
        super.onStart();


        // set user name at start
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        Aqref = FirebaseDatabase.getInstance().getReference("Aquariums");


        Log.i("User value", String.valueOf(reference.child(userID)));

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

        //show owned aquariums
        ValueEventListener AqListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                aqList.clear();
                aqValues.clear();

                for(DataSnapshot data : snapshot.getChildren()) {

                    String ukey = user.getUid();
                    String dat = data.getKey();

                    if(dat.contains(ukey)){
                        aqList.add(ukey);
                        Aquarium AQtmp = data.getValue(Aquarium.class);
                        String AQname = AQtmp.NickName;
                        aqValues.add(AQname);
                    }

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

        /**
         * this method loads the user's profile picture from the database and shows it
         */

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference("images/").child(userID);

        final long FIVE_MEGABYTE = 5 * 1024 * 1024;
        storageReference.getBytes(FIVE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                int widthLight = bitmap.getWidth();
                int heightLight = bitmap.getHeight();

                Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                        Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(output);
                Paint paintColor = new Paint();
                paintColor.setFlags(Paint.ANTI_ALIAS_FLAG);

                RectF rectF = new RectF(new Rect(0, 0, widthLight, heightLight));

                canvas.drawRoundRect(rectF, widthLight*2, heightLight, paintColor);

                Paint paintImage = new Paint();
                paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
                canvas.drawBitmap(bitmap, 0, 0, paintImage);
                imageButton.setImageBitmap(output);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


    }

        public void redirect_logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, Login.class));
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

        //check internet connection before proceeding
        broadcastReceiver = new NetworkConnectionReciever();
        registerNetworkBrodcastReciever();

        intent = new Intent(this, Analysis.class);
        intent.putExtra("key", aqValues.get(pos));
        startActivity(intent);

    }

    public void AddAquarium(View view) {
        startActivity(new Intent(this, AddAquarium.class));
    }

    public void ChoosePic(View view) {
        /**
         * alert dialog for selecting a profile pic or animation
         */

        adb = new AlertDialog.Builder(this);

        adb.setTitle("before we continue");
        adb.setMessage("we would like to create a profile picture for your aquarium, please select one of the options below");

        adb.setPositiveButton("take a picture", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(AquariumPicker.this, storage.class);
                startActivity(intent);

            }
        });
        adb.setNegativeButton("no thanks", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog ad = adb.create();
        ad.show();
    }
}