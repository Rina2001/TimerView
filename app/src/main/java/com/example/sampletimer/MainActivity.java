package com.example.sampletimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.maxwellforest.blogtimer.TimerView;

public class MainActivity extends AppCompatActivity {

    private static final int TIMER_LENGTH = 15;

    private com.maxwellforest.blogtimer.TimerView mTimerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimerView = (TimerView) findViewById(R.id.timer);
        mTimerView.start(TIMER_LENGTH);
        final Button timerStartButton = (Button) findViewById(R.id.btn_timer_start);
        timerStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerView.start(TIMER_LENGTH);
            }
        });
    }

    @Override
    protected void onPause() {
        mTimerView.stop();

        super.onPause();
    }

}