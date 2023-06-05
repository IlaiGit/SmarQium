package com.example.beta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * @author		Ilai Shimoni ilaigithub@gmail.com
 * @version	    3.0
 * @since		12/10/22
 * this class shows the user summary of his results and allows to delete
 * an exising aquarium
 */
public class Analysis extends AppCompatActivity {

    /**
     * xml elements declaration as well as java local variables
     */

    FragmentContainerView fragment_graph, fragment_error, fragment_feeder;
    private DatabaseReference aq_ref, test_ref;
    AlertDialog.Builder adb;
    String MAC;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        fragment_graph = (FragmentContainerView) findViewById(R.id.fragment_graph);
        fragment_error = (FragmentContainerView) findViewById(R.id.fragment_error);
        fragment_feeder = (FragmentContainerView) findViewById(R.id.fragment_feeder);

    }

    /**
     * performs a FragmentView switch according to the user's button press
     * while staying on the same activity
     */

    public void ReplaceGraph(View view) {
        fragment_error.setVisibility(View.GONE);
        fragment_feeder.setVisibility(View.GONE);
        fragment_graph.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_graph, GraphFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("graph")
                .commit();

    }

    /**
     * performs a FragmentView switch according to the user's button press
     * while staying on the same activity
     */
    public void ReplaceError(View view) {
        fragment_graph.setVisibility(View.GONE);
        fragment_feeder.setVisibility(View.GONE);
        fragment_error.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_error, ErrorFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("error")
                .commit();

    }
    /**
     * performs a FragmentView switch according to the user's button press
     * while staying on the same activity
     */
    public void ReplaceFeeder(View view) {
        fragment_graph.setVisibility(View.GONE);
        fragment_error.setVisibility(View.GONE);
        fragment_feeder.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_feeder, FeederFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("feeder")
                .commit();

    }

    /**
     * this method deletes an existing aquarium belongs to the user and all data samples that are related to it
     */
    public void DeleteAq(View view) {
        adb = new AlertDialog.Builder(this);

        adb.setTitle("Enter your mac address");
        adb.setMessage("verify the mac address for the aquarium you want to delete");
        final EditText eT = new EditText(this);
        adb.setView(eT);
        adb.setCancelable(false);
        adb.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(eT.getText().toString().isEmpty()){
                    Intent intent = new Intent(Analysis.this, AquariumPicker.class);
                    startActivity(intent);
                }
                else{
                    // remove aquarium
                    MAC = eT.getText().toString();
                    aq_ref = FirebaseDatabase.getInstance().getReference("Aquariums").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+ " " + MAC);
                    aq_ref.removeValue();

                    //cancels background task
                    stopService(new Intent(getApplication(), TestService.class));

                    //remove tests area/s
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    userID = user.getUid();
                    reference = FirebaseDatabase.getInstance().getReference("TestsArea");


                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Tests testTmp = dataSnapshot.getValue(Tests.class);

                                String[] partsOfId = dataSnapshot.getKey().split(" ");
                                if (MAC.equals(partsOfId[2])) {
                                    reference.child(dataSnapshot.getKey()).removeValue();
                                }
                            }


                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Intent intent = new Intent(Analysis.this, AquariumPicker.class);
                    startActivity(intent);
                    dialogInterface.dismiss();


                }

            }
        });
        adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Analysis.this, AquariumPicker.class);
                startActivity(intent);
            }
        });
        AlertDialog ad = adb.create();
        ad.show();

    }
}