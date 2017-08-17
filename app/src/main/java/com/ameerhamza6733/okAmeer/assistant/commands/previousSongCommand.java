package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.Command;

/**
 * Created by AmeerHamza on 6/28/2017.
 */

public class previousSongCommand implements Command {
    @Override
    public void execute(Context context, String predicate) {
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "previous");
       context. sendBroadcast(i);
        Log.d("Music","previousSongCommand");

    }

    @Override
    public String getDefaultPhrase() {
        return "پچھلا گانا لگاؤ,null";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return context.getResources().getString(R.string.Previois_Song_lagya_ja_Raha);
    }
}
