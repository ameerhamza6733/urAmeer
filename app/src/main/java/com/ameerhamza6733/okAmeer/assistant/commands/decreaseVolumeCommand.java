package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.media.AudioManager;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.Command;

/**
 * Created by AmeerHamza on 6/28/2017.
 */

public class decreaseVolumeCommand implements Command {
    @Override
    public void execute(Context context, String predicate) {
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);

    }

    @Override
    public String getDefaultPhrase() {
        return "والیوم کم کرو,آواز کم کرو,آواز تھوڑی کرو,volume thoda karo,Volume kam karo,Awaz kam karo";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return context.getResources().getString(R.string.Volume_kam_kiya_ja_raha_ha);
    }
}
