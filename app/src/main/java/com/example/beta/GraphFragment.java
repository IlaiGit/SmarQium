package com.example.beta;

import static android.R.layout.simple_spinner_dropdown_item;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.content.Context;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GraphFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    String[] spinnerData = {"Please select a data pull", "Water Clarity", "Water Height", "Water PH", "temperature (Celsius)", "temperature(Fahrenheit)"};
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    ArrayList<Double> clarityList = new ArrayList<Double>();
    ArrayList<Double> PHlist = new ArrayList<Double>();
    ArrayList<Double> tempCList = new ArrayList<Double>();
    ArrayList<Double> tempFList = new ArrayList<Double>();
    ArrayList<Double> heightList = new ArrayList<Double>();
    int numberOfTests;
    GraphView graphView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        graphView = (GraphView) getView().findViewById(R.id.graphView);
        spinner = (Spinner) getView().findViewById(R.id.GraphSpinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getContext(), simple_spinner_dropdown_item, spinnerData);
        spinner.setAdapter(adp);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //water clarity
        if (position == 1) {
            graphView.removeAllSeries();

            if (clarityList.size() < 10) {
                for (int i = numberOfTests; i < 10; i++) {
                    clarityList.add(0.0);
                }
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                    // on below line we are adding
                    // each point on our x and y axis.
                    new DataPoint(1, clarityList.get(0)),
                    new DataPoint(2, clarityList.get(1)),
                    new DataPoint(3, clarityList.get(2)),
                    new DataPoint(4, clarityList.get(3)),
                    new DataPoint(5, clarityList.get(4)),
                    new DataPoint(6, clarityList.get(5)),
                    new DataPoint(7, clarityList.get(6)),
                    new DataPoint(8, clarityList.get(7)),
                    new DataPoint(9, clarityList.get(8)),
                    new DataPoint(10, clarityList.get(9)),


            });

            graphView.addSeries(series);


        } else if (position == 2) {
            graphView.removeAllSeries();


            // water height
            if (heightList.size() < 10) {
                for (int i = numberOfTests; i < 10; i++) {
                    heightList.add(0.0);
                }
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                    // on below line we are adding
                    // each point on our x and y axis.
                    new DataPoint(1, heightList.get(0)),
                    new DataPoint(2, heightList.get(1)),
                    new DataPoint(3, heightList.get(2)),
                    new DataPoint(4, heightList.get(3)),
                    new DataPoint(5, heightList.get(4)),
                    new DataPoint(6, heightList.get(5)),
                    new DataPoint(7, heightList.get(6)),
                    new DataPoint(8, heightList.get(7)),
                    new DataPoint(9, heightList.get(8)),
                    new DataPoint(10, heightList.get(9)),


            });

            graphView.addSeries(series);


        } else if (position == 3) {
            graphView.removeAllSeries();


            // water opacity
            if (PHlist.size() < 10) {
                for (int i = numberOfTests; i < 10; i++) {
                    PHlist.add(0.0);
                }
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                    // on below line we are adding
                    // each point on our x and y axis.
                    new DataPoint(1, PHlist.get(0)),
                    new DataPoint(2, PHlist.get(1)),
                    new DataPoint(3, PHlist.get(2)),
                    new DataPoint(4, PHlist.get(3)),
                    new DataPoint(5, PHlist.get(4)),
                    new DataPoint(6, PHlist.get(5)),
                    new DataPoint(7, PHlist.get(6)),
                    new DataPoint(8, PHlist.get(7)),
                    new DataPoint(9, PHlist.get(8)),
                    new DataPoint(10, PHlist.get(9)),


            });

            graphView.addSeries(series);


        } else if (position == 4) {
            graphView.removeAllSeries();


            // water temp(c)
            if (tempCList.size() < 10) {
                for (int i = numberOfTests; i < 10; i++) {
                    tempCList.add(0.0);
                }
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                    // on below line we are adding
                    // each point on our x and y axis.
                    new DataPoint(1, tempCList.get(0)),
                    new DataPoint(2, tempCList.get(1)),
                    new DataPoint(3, tempCList.get(2)),
                    new DataPoint(4, tempCList.get(3)),
                    new DataPoint(5, tempCList.get(4)),
                    new DataPoint(6, tempCList.get(5)),
                    new DataPoint(7, tempCList.get(6)),
                    new DataPoint(8, tempCList.get(7)),
                    new DataPoint(9, tempCList.get(8)),
                    new DataPoint(10, tempCList.get(9)),


            });

            graphView.addSeries(series);


        } else if (position == 5) {
            graphView.removeAllSeries();


            // water temp(f)
            if (tempFList.size() < 10) {
                for (int i = numberOfTests; i < 10; i++) {
                    tempFList.add(0.0);
                }
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                    // on below line we are adding
                    // each point on our x and y axis.
                    new DataPoint(1, tempFList.get(0)),
                    new DataPoint(2, tempFList.get(1)),
                    new DataPoint(3, tempFList.get(2)),
                    new DataPoint(4, tempFList.get(3)),
                    new DataPoint(5, tempFList.get(4)),
                    new DataPoint(6, tempFList.get(5)),
                    new DataPoint(7, tempFList.get(6)),
                    new DataPoint(8, tempFList.get(7)),
                    new DataPoint(9, tempFList.get(8)),
                    new DataPoint(10, tempFList.get(9)),


            });

            graphView.addSeries(series);


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getContext(), "please select a data pull", Toast.LENGTH_SHORT).show();
    }
}