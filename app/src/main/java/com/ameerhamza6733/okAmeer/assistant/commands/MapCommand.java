package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;

import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.CommandModel;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.FlashLightActivtyReceiver;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.mapActivty;

/**
 * Created by AmeerHamza on 12/21/2017.
 */

public class MapCommand implements Command {
    @Override
    public void execute(CommandModel commandModel) {
        Intent i = new Intent(commandModel.getContext(), mapActivty.class);
        i.putExtra("onOrOff", false);
        commandModel.getContext().startActivity(i);
    }

    @Override
    public String getDefaultPhrase() {
        return "map";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }
}
