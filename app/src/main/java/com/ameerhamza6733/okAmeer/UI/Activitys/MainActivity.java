package com.ameerhamza6733.okAmeer.UI.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.UI.fragment.voiceRecgonizationFragment;
import com.github.clans.fab.FloatingActionMenu;

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

        mSpeakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                newIntance = voiceRecgonizationFragment.newInstance("hi", true, true);
                newIntance.setStyle(1, R.style.AppTheme);
                newIntance.show(fragmentManager, "fragment_voice_input");
            }
        });
      com.github.clans.fab.FloatingActionButton feb2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab12);
       FloatingActionButton feb= (FloatingActionButton) findViewById(R.id.show_Command_hint);
        feb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, votingActivity.class);
                startActivity(intent);
            }
        });
        feb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"dsfdf",Toast.LENGTH_SHORT).show();
            }
        });


    }
}
