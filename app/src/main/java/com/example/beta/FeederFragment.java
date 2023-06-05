package com.example.beta;

import android.app.AlarmManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

/**
 * @author		Ilai Shimoni ilaigithub@gmail.com
 * @version	    3.0
 * @since		12/10/22
 * this class allows for manual feeding of the aquarium
 */
public class FeederFragment extends Fragment {

    Button continue_btn,timeBtn;
    TextView showTime;
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        showTime = (TextView) getView().findViewById(R.id.showTime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feeder, container, false);
    }

    public void ShowTimePicker(View view) {
    }
}