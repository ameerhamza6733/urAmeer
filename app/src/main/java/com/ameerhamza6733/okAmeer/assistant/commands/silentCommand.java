package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.media.AudioManager;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.CommandModel;

/**
 * Created by AmeerHamza on 7/3/2017.
 */

public class silentCommand implements Command {
    @Override
    public void execute(CommandModel commandModel) {

        try{
            final AudioManager mode = (AudioManager) commandModel.getContext().getSystemService(Context.AUDIO_SERVICE);
            mode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getDefaultPhrase() {
        return "خاموش,نماز وقت,silent lagao,silent karo";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return context.getResources().getString(R.string.Silent_Phone_Kar_Dea_Gaya_Ha);
    }
}
