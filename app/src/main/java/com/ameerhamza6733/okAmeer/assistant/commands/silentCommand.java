package com.ameerhamza6733.okAmeer.assistant.commands;

import android.Manifest;
import android.content.Context;
import android.media.AudioManager;
import android.support.v4.app.ActivityCompat;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.sendSmsActivity;

/**
 * Created by AmeerHamza on 7/3/2017.
 */

public class silentCommand implements Command {
    @Override
    public void execute(Context context, String predicate) {

        try{
            final AudioManager mode = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            mode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getDefaultPhrase() {
        return "خاموش,نماز وقت,si";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return context.getResources().getString(R.string.Silent_Phone_Kar_Dea_Gaya_Ha);
    }
}
