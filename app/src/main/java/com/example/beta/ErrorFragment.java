package com.example.beta;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ErrorFragment extends Fragment {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    ArrayList<Double> clarityList = new ArrayList<Double>();
    ArrayList<Double> PHList = new ArrayList<Double>();
    ArrayList<Double> tempCList = new ArrayList<Double>();
    ArrayList<Double> tempFList = new ArrayList<Double>();
    ArrayList<Double> heightList = new ArrayList<Double>();
    int numberOfTests;

    TextView TV_clarity, TV_height, TV_opacity, TV_temperature;

    public static final double idealTemp_c = 26.0;
    public static final double ideal_water_clarity = 3;
    public static final double ideal_water_height = 60;
    public static final double ideal_water_PH = 3;

    WebView clarityWEB, heightWEB, opacityWEB, temperatureWEB;







    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        TV_clarity = (TextView) getView().findViewById(R.id.TV_clarity);
        TV_height = (TextView) getView().findViewById(R.id.TV_height);
        TV_opacity = (TextView) getView().findViewById(R.id.TV_PH);
        TV_temperature = (TextView) getView().findViewById(R.id.TV_temperature);

        clarityWEB = (WebView) getView().findViewById(R.id.clarityWEB);
        heightWEB = (WebView) getView().findViewById(R.id.heightWEB);
        opacityWEB = (WebView) getView().findViewById(R.id.opacityWEB);
        temperatureWEB = (WebView) getView().findViewById(R.id.temperatureWEB);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // set user name at start
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("TestsArea");




        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numberOfTests = 0;
                clarityList.clear();
                PHList.clear();
                heightList.clear();
                tempCList.clear();
                tempFList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Tests testTmp = dataSnapshot.getValue(Tests.class);

                    String[] partsOfId = dataSnapshot.getKey().split(" ");
                    if(userID.equals(partsOfId[0])){
                        clarityList.add(testTmp.getWaterClarity());
                        PHList.add(testTmp.getWaterPH());
                        heightList.add(testTmp.getWaterHeight());
                        tempCList.add(testTmp.getTempC());
                        tempFList.add(testTmp.getTempF());

                        numberOfTests++;
                    }
                }


                for (int i=0; i<numberOfTests; i++){
                    if(tempCList.get(i) > idealTemp_c){
                        TV_temperature.setText("A problem with your temperature was detected");

                        temperatureWEB.loadUrl("https://www.thesprucepets.com/how-do-i-lower-high-water-temps-1378741");
                        temperatureWEB.setVisibility(View.VISIBLE);

                        clarityWEB.loadUrl("https://www.thesprucepets.com/cloudy-aquarium-water-1378803");
                        clarityWEB.setVisibility(View.VISIBLE);
                    }
                }

                for (int i=0; i<numberOfTests; i++){
                    if(clarityList.get(i) > ideal_water_clarity){
                        TV_clarity.setText("A problem with your water clarity");

                    }
                }
                for (int i=0; i<numberOfTests; i++){
                    if(heightList.get(i) > ideal_water_height){
                        TV_height.setText("A problem with your water height");

                    }
                }
                for (int i=0; i<numberOfTests; i++){
                    if(PHList.get(i) > ideal_water_PH){
                        TV_opacity.setText("A problem with your water height");

                    }
                }





            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








    }
}