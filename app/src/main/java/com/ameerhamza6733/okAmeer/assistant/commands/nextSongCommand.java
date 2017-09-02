package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.CommandModel;

/**
 * Created by AmeerHamza on 6/28/2017.
 */

public class nextSongCommand implements Command {
    @Override
    public void execute(CommandModel commandModel) {
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "next");
        commandModel.getContext().sendBroadcast(i);
        Log.d("Music","nextSongCommand");

    }

    @Override
    public String getDefaultPhrase() {
        return "اگلا گانا,Aag Lagana lagao";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return context.getResources().getString(R.string.Agala_Gaana_Lagaaya_Ja_Raha_Ha);
    }
}
