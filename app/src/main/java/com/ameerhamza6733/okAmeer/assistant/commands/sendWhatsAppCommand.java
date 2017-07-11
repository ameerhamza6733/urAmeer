package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.Command;

import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.sendSmsActivity;

/**
 * Created by AmeerHamza on 6/30/2017.
 */

public class sendWhatsAppCommand implements Command {
    @Override
    public void execute(Context context, String predicate) {

        Intent intent =  new Intent(context, sendSmsActivity.class);
        intent.putExtra("EXTRA_SMS_OR_WHATS_APP","WhatsApp");

        context.startActivity(intent);


    }



    @Override
    public String getDefaultPhrase() {
        return "WhatsApp";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return context.getString(R.string.Aap_kiss_ko_whats_app_messge_send_karna_chaahate_hain);
    }
}
