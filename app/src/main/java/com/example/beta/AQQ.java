package com.example.beta;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author		Ilai Shimoni <ilaigithub@gmail.com>
 * @version	    2.2
 * @since		6/11/22
 *  this class stores the information (and constants) for the aquarium's animation (optional)
 */

public class AQQ extends AppCompatActivity {
    private AquariumView mAquariumView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqq);

        mAquariumView = findViewById(R.id.aquarium_view);

        // Set aquarium dimensions
        int aquariumWidth = 500;
        int aquariumHeight = 500;
        int aquariumDepth = 500;
        mAquariumView.setAquariumDimensions(aquariumWidth, aquariumHeight, aquariumDepth);

        // Set number of fish
        int numberOfFish = 3;
        mAquariumView.setNumberOfFish(numberOfFish);

        // Start animation
        mAquariumView.startAnimation();
    }
}