package com.ameerhamza6733.okAmeer.UI.Activitys;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.UI.fragment.voiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech;

public class handsetReceiverActivty extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showVoiceDilogeFragment();
        try {
            myTextToSpeech.intiTextToSpeech(this, "hi", "");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void showVoiceDilogeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        voiceRecgonizationFragment newIntance = voiceRecgonizationFragment.newInstance("hi", true,true);

        newIntance.show(fragmentManager, "handsetReceiverActivty");
        newIntance.setStyle(1, R.style.AppTheme);
    }

    @Override
    protected void onResume() {
        showVoiceDilogeFragment();
        super.onResume();


    }
}
