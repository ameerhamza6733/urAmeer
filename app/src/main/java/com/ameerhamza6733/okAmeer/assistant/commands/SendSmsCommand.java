package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.CommandModel;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.SendSmsActivity;

/**
 * Created by AmeerHamza on 6/27/2017.
 */

public class SendSmsCommand implements Command{
    @Override
    public void execute(CommandModel commandModel) {
        Log.d("SendSmsCommand","excute" + "commandModel.getPredicate()"+commandModel.getPredicate()+"commandModel.getCommandExtraPhrase()"+commandModel.getCommandExtraPhrase());
        Intent intent =  new Intent(commandModel.getContext(), SendSmsActivity.class);
        intent.putExtra("EXTRA_SMS_OR_WHATS_APP","sms");
        if(commandModel.getCommandExtraPhrase().length()>1)
        intent.putExtra(SendSmsActivity.EXTRA_RECIPIENT_NAME,commandModel.getCommandExtraPhrase());
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
