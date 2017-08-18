package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.sendSmsActivity;

/**
 * Created by AmeerHamza on 6/27/2017.
 */

public class sendSmsCommand implements Command{
    @Override
    public void execute(Context context, String predicate) {
        Log.d("sendSmsCommand","excute");
        Intent intent =  new Intent(context, sendSmsActivity.class);

        intent.putExtra("EXTRA_SMS_OR_WHATS_APP","sms");
        intent.putExtra(sendSmsActivity.EXTRA_RECIPIENT_NAME,predicate);
        context.startActivity(intent);

    }

    @Override
    public String getDefaultPhrase() {
        return "میسج بھیجو,میسج لکھیں,message karo,SMS send karo,message send karo,message likho,SMS bhejo,message bhejo";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }
}
