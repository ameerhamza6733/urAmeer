package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.CommandModel;

/**
 * Created by AmeerHamza on 6/28/2017.
 */

public class increaseVolumeCommand implements Command {
    @Override
    public void execute(CommandModel commandModel) {
        AudioManager audio = (AudioManager) commandModel.getContext().getSystemService(Context.AUDIO_SERVICE);
        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
        Log.d("Volume","increase the volume..");

    }

    @Override
    public String getDefaultPhrase() {
        return "حجم زیادہ کرو,آواز زیادہ کرو,آواز بلند کرو,volume Jyada karo,volume Zyada karo,volume jayada karo,Awaz Jyada karo,volume Uncha karo, Awaz Unchi karo";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return context.getResources().getString(R.string.Volume_Zayada_ke_Ja_rahi_ha);
    }
}
