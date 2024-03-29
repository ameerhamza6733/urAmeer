package com.ameerhamza6733.okAmeer.utial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by AmeerHamza on 6/28/2017.
 */

public class RemoteControlReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            /* handle media button intent here by reading contents */
            /* of EXTRA_KEY_EVENT to know which key was pressed    */
            Toast.makeText(context,"RemoteControlReceiver",Toast.LENGTH_SHORT).show();
        }
    }
}