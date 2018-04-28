package com.ameerhamza6733.okAmeer.UI.Activitys;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.UI.fragment.VoiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech;

public class HandsetReceiverActivty extends AppCompatActivity{

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
        VoiceRecgonizationFragment newIntance = VoiceRecgonizationFragment.newInstance("en-IN", false, true);
        newIntance.show(fragmentManager, "HandsetReceiverActivty");
        newIntance.setStyle(1, R.style.AppTheme);
    }

    @Override
    protected void onResume() {
        showVoiceDilogeFragment();
        super.onResume();


    }
}
