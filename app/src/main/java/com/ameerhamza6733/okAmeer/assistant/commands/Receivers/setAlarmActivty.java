package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ameerhamza6733.okAmeer.R;

public class setAlarmActivty extends AppCompatActivity {

    public static final String EXTRA_TIME="extraTime";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm_activty);
    }
}
