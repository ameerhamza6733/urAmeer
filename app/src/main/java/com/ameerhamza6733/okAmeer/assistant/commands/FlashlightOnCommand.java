package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.FlashLightActivtyReceiver;

/**
 * Created by AmeerHamza on 6/17/2017.
 */


public class FlashlightOnCommand implements Command {
    @Override
    public void execute(final Context context, final String predicate) {
        Log.d("Flashlight","turing on flash light");
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                Intent i = new Intent(context, FlashLightActivtyReceiver.class);
                i.putExtra("onOrOff", true);

                context.startActivity(i);
                return true;
            }
        });
        handler.sendEmptyMessageDelayed(0, 500);



    }

    @Override
    public String getDefaultPhrase() {
        return "فلیش روشنی آن,لائٹ آن,روشنی جلاؤ,لائٹ جلاؤ,بتی جلاؤ,flashlight on karo,light chalao,light on karo";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }
}
