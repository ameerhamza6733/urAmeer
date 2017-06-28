package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.Command;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AmeerHamza on 6/28/2017.
 */

public class pauseMuusicCommand implements Command {
    @Override
    public void execute(Context context, String predicate) {
        pauseMusicNow(context);
    }

    private void pauseMusicNow(final Context context) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent("com.android.music.musicservicecommand");
                i.putExtra("command", "pause");
                context.sendBroadcast(i);
            }
        }, 1000);

    }

    @Override
    public String getDefaultPhrase() {
        return "گانا بند کرو,میوزک بند کرو";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return context.getResources().getString(R.string.Aap_ka_Gaana_band_Kea_Ja_Raha_ha);
    }
}
