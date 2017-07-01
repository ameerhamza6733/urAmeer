package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.assistant.Command;

import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.smsActivity;

/**
 * Created by AmeerHamza on 6/30/2017.
 */

public class sendWhatsAppCommand implements Command {
    @Override
    public void execute(Context context, String predicate) {

        Intent intent =  new Intent(context, smsActivity.class);
        intent.putExtra("EXTRA_SMS_OR_WHATS_APP","WhatsApp");

        context.startActivity(intent);


    }



    @Override
    public String getDefaultPhrase() {
        return "WhatsApp";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }
}
