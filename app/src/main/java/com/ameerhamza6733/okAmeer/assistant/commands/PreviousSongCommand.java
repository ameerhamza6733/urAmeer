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

public class PreviousSongCommand implements Command {
    @Override
    public void execute(CommandModel commandModel) {
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "previous");
       commandModel.getContext(). sendBroadcast(i);
        Log.d("Music","PreviousSongCommand");

    }

    @Override
    public String getDefaultPhrase() {
        return "پچھلا گانا لگاؤ,pichla gana lagao";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return context.getResources().getString(R.string.Previois_Song_lagya_ja_Raha);
    }
}
