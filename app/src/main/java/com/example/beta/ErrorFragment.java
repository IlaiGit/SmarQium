package com.example.beta;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

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
 * this class pulls data from the firebase, compares it to the
 * acceptable values and acts accordingly
 */
public class ErrorFragment extends Fragment {

    /**
     * xml elements declaration as well as java local variables
     */
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    ArrayList<Double> clarityList = new ArrayList<Double>();
    ArrayList<Double> PHList = new ArrayList<Double>();
    ArrayList<Double> tempCList = new ArrayList<Double>();
    ArrayList<Double> tempFList = new ArrayList<Double>();
    ArrayList<Double> heightList = new ArrayList<Double>();
    int numberOfTests;

    Button clarityBTN, phBTN, tempBTN, heightBTN;
    TextView TV_clarity, TV_height, TV_opacity, TV_temperature;
    public static final double idealTemp_c = 26.0;
    public static final double ideal_water_clarity = 3;
    public static final double ideal_water_height = 60;
    public static final double ideal_water_PH = 3;
    WebView clarityWEB, heightWEB, phWEB, temperatureWEB;







    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        TV_clarity = (TextView) getView().findViewById(R.id.TV_clarity);
        TV_height = (TextView) getView().findViewById(R.id.TV_height);
        TV_opacity = (TextView) getView().findViewById(R.id.TV_PH);
        TV_temperature = (TextView) getView().findViewById(R.id.TV_temperature);

        clarityWEB = (WebView) getView().findViewById(R.id.clarityWEB);
        heightWEB = (WebView) getView().findViewById(R.id.heightWEB);
        phWEB = (WebView) getView().findViewById(R.id.phWEB);
        temperatureWEB = (WebView) getView().findViewById(R.id.temperatureWEB);

        clarityBTN = (Button) getView().findViewById(R.id.clarityBTN);
        phBTN = (Button) getView().findViewById(R.id.phBTN);
        tempBTN = (Button) getView().findViewById(R.id.tempBTN);
        heightBTN = (Button) getView().findViewById(R.id.heightBTN);

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

    /**
     * this method performs a data pull and according to constants
     * compares received data and the constants to warn user of
     * unusual results as well as showing ways to improve
     */

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
                        tempBTN.setVisibility(View.VISIBLE);
                        TV_temperature.setText("A problem with your temperature was detected");
                    }
                }

                for (int i=0; i<numberOfTests; i++){
                    if(clarityList.get(i) > ideal_water_clarity){
                        clarityBTN.setVisibility(View.VISIBLE);
                        TV_clarity.setText("A problem with your water clarity");

                    }
                }
                for (int i=0; i<numberOfTests; i++){
                    heightBTN.setVisibility(View.VISIBLE);
                    if(heightList.get(i) > ideal_water_height){
                        TV_height.setText("A problem with your water height");

                    }
                }
                for (int i=0; i<numberOfTests; i++){
                    phBTN.setVisibility(View.VISIBLE);
                    if(PHList.get(i) > ideal_water_PH){
                        TV_height.setText("A problem with your water height");

                    }
                }





            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








    }
    public void TempSolution(View view) {

        phWEB.setVisibility(View.GONE);
        heightWEB.setVisibility(View.GONE);
        clarityWEB.setVisibility(View.GONE);

        temperatureWEB.loadUrl("https://www.thesprucepets.com/how-do-i-lower-high-water-temps-1378741");
        temperatureWEB.setVisibility(View.VISIBLE);
    }

    public void PhSolution(View view) {

        temperatureWEB.setVisibility(View.GONE);
        heightWEB.setVisibility(View.GONE);
        clarityWEB.setVisibility(View.GONE);


        phWEB.loadUrl("https://atlas-scientific.com/blog/how-to-lower-ph-in-freshwater-aquarium/#:~:text=The%20preferred%20way%20to%20lower,osmosis%20are%20also%20commonly%20used.");
        phWEB.setVisibility(View.VISIBLE);
    }

    public void HeightSolution(View view) {

        temperatureWEB.setVisibility(View.GONE);
        phWEB.setVisibility(View.GONE);
        clarityWEB.setVisibility(View.GONE);

        heightWEB.loadUrl("https://blog.aquaticwarehouse.com/water-level-in-fish-tank/#:~:text=You%20should%20generally%20fill%20the,splash%20outside%20of%20the%20tank.&text=Jumping%20behavior%20isn't%20unusual%20among%20aquarium%20species.");
        heightWEB.setVisibility(View.VISIBLE);

    }

    public void ClaritySolution(View view) {

        temperatureWEB.setVisibility(View.GONE);
        phWEB.setVisibility(View.GONE);
        heightWEB.setVisibility(View.GONE);


        clarityWEB.loadUrl("https://www.thesprucepets.com/cloudy-aquarium-water-1378803");
        clarityWEB.setVisibility(View.VISIBLE);
    }
}