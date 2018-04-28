package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;

import com.ameerhamza6733.okAmeer.assistant.Command;

import com.ameerhamza6733.okAmeer.assistant.CommandModel;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.SendSmsActivity;

/**
 * Created by AmeerHamza on 6/30/2017.
 */

public class SendWhatsAppCommand implements Command {
    @Override
    public void execute(CommandModel commandModel) {

        Intent intent =  new Intent(commandModel.getContext(), SendSmsActivity.class);
        intent.putExtra("EXTRA_SMS_OR_WHATS_APP","WhatsApp");
        intent.putExtra(SendSmsActivity.EXTRA_RECIPIENT_NAME,commandModel.getPredicate());
        commandModel.getContext().startActivity(intent);


    }



    @Override
    public String getDefaultPhrase() {
        return "WhatsApp message bhejo,WhatsApp message likho,WhatsApp message send karo";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;//context.getString(R.string.Aap_kiss_ko_whats_app_messge_send_karna_chaahate_hain);
    }
}
