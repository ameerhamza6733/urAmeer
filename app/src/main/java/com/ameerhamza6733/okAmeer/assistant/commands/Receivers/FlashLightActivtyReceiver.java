package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by AmeerHamza on 6/17/2017.
 */

public class FlashLightActivtyReceiver extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this,"FlashLightActivtyReceiver",Toast.LENGTH_LONG).show();
    }
}
