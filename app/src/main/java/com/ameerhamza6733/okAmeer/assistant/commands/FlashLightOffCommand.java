package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.CommandModel;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.FlashLightActivtyReceiver;

/**
 * Created by AmeerHamza on 6/17/2017.
 */

public class FlashLightOffCommand implements Command {
    @Override
    public void execute(CommandModel commandModel) {
        Log.d("Flashlight","turing of flash light");
        Intent i = new Intent(commandModel.getContext(), FlashLightActivtyReceiver.class);
        i.putExtra("onOrOff", false);
        commandModel.getContext().startActivity(i);
    }

    @Override
    public String getDefaultPhrase() {
        return "فلیش روشنی بند,لائٹ بند,لائٹ آف,flashlight off karo,flashlight band karo,light band karo,light off karo";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }
}
