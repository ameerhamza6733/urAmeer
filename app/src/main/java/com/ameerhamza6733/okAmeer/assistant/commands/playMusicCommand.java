package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.Command;

/**
 * Created by AmeerHamza on 6/28/2017.
 */

public class playMusicCommand implements Command {
    @Override
    public void execute(final Context context, String predicate) {

        try {
            Log.d("Music","playMusicCommand");
            openGoogleMusicApp(context);
            nowPlayMusic(context);
        } catch (Exception e) {
            e.printStackTrace();

        }


    }


    @Override
    public String getDefaultPhrase() {
        return "گانا لگاؤ,میوزک لگاو";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return context.getResources().getString(R.string.Aap_Ka__Gaana_Lagaaya_Ja_Raha_Ha);
    }

    private void nowPlayMusic(final Context context)  throws Exception{
        new CountDownTimer(3000, 500) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Intent i = new Intent("com.android.music.musicservicecommand");
                i.putExtra("command", "play");
                context.sendBroadcast(i);
            }


        }.start();
    }

    private void openGoogleMusicApp(Context context) throws Exception {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.google.android.music");
        context.startActivity(intent);
    }
}
