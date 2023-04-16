package com.example.beta;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author		Ilai Shimoni <ilaigithub@gmail.com>
 * @version	    2.2
 * @since		6/11/22
 *  this class allows user to look at data samples collected during his use of the application and sensors
 */

public class Analysis extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
    }
}