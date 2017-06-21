package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.utial.myTextToSpeech;

import github.nisrulz.lantern.Lantern;

/**
 * Created by AmeerHamza on 6/17/2017.
 */

public class FlashLightActivtyReceiver extends Activity {
    public static final String EXTRA_ON_OF = "onORof";
    public static final String EXTRA_STRING ="stringextra" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FlashLightActivty", "oncreate");
        Intent intent = getIntent();
        boolean flashLight = intent.getBooleanExtra(EXTRA_ON_OF, false);
        String activatingParts = intent.getStringExtra(EXTRA_STRING);


   }
}
