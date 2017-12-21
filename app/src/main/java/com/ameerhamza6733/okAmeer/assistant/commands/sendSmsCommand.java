package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.CommandModel;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.sendSmsActivity;

/**
 * Created by AmeerHamza on 6/27/2017.
 */

public class sendSmsCommand implements Command{
    @Override
    public void execute(CommandModel commandModel) {
        Log.d("sendSmsCommand","excute" + "commandModel.getPredicate()"+commandModel.getPredicate()+"commandModel.getCommandExtraPhrase()"+commandModel.getCommandExtraPhrase());
        Intent intent =  new Intent(commandModel.getContext(), sendSmsActivity.class);
        intent.putExtra("EXTRA_SMS_OR_WHATS_APP","sms");
        if(commandModel.getCommandExtraPhrase().length()>1)
        intent.putExtra(sendSmsActivity.EXTRA_RECIPIENT_NAME,commandModel.getCommandExtraPhrase());
        commandModel.getContext().startActivity(intent);

    }

    @Override
    public String getDefaultPhrase() {
        return "message,aaa";
    }
    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }
}
