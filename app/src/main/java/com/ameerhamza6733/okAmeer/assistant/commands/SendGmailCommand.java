package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;

import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.CommandModel;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.SendEmailActivity;

/**
 * Created by AmeerHamza on 7/9/2017.
 */

public class SendGmailCommand implements Command {
    @Override
    public void execute(CommandModel commandModel) {

        Intent  intent = new Intent(commandModel.getContext(),SendEmailActivity.class);
        intent.putExtra("EmailExtra",commandModel.getPredicate());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        commandModel.getContext().startActivity(intent);

    }


    @Override
    public String getDefaultPhrase() {
        return "ای میل لکھیں,Gmail اپ,Gmail لکھیں,email send karo,email likho,Gmail likho";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }
}
