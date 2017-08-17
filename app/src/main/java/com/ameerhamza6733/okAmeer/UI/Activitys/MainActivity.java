package com.ameerhamza6733.okAmeer.UI.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.UI.fragment.voiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.CallingActivity;
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by AmeerHamza on 7/17/2017.
 */

public class MainActivity extends AppCompatActivity {
    private voiceRecgonizationFragment newIntance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton mSpeakButton = (ImageButton) findViewById(R.id.speakButton);
        try {
            myTextToSpeech.intiTextToSpeech(this, "hi", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSpeakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                newIntance = voiceRecgonizationFragment.newInstance("hi", true, true);
                newIntance.setStyle(1, R.style.AppTheme);
                newIntance.show(fragmentManager, "fragment_voice_input");
            }
        });
        FloatingActionButton FebRequestNewCommand = (FloatingActionButton) findViewById(R.id.fab_request_new_command);
        FloatingActionButton FebHelp= (FloatingActionButton) findViewById(R.id.fab_help);
        FebRequestNewCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, votingActivity.class);
                startActivity(intent);
            }
        });
        FebHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, show_sample_commands_list.class);
                startActivity(intent);
            }
        });


    }
}
