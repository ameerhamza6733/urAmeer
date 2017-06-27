package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.smsActivity;

/**
 * Created by AmeerHamza on 6/27/2017.
 */

public class sendSmsCommand implements Command{
    @Override
    public void execute(Context context, String predicate) {
        Log.d("sendSmsCommand","excute");
        context.startActivity(new Intent(context, smsActivity.class));

    }

    @Override
    public String getDefaultPhrase() {
        return "SMS,میسج";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return context.getString(R.string.Kis_Ko_message_likha_na);
    }
}
