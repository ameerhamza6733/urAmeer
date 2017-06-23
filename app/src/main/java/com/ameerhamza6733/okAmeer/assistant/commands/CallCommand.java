package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;

import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.CallingActivity;

/**
 * Created by AmeerHamza on 6/23/2017.
 */

public class CallCommand implements Command {
    @Override
    public void execute(Context context, String predicate) {

        context.startActivity(new Intent(context, CallingActivity.class));
    }

    @Override
    public String getDefaultPhrase() {
        return "کال,فون";
    }

    @Override
    public String getTtsPhrase() {
       return null;
    }
}
