package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;

import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.CommandModel;

/**
 * Created by AmeerHamza on 12/21/2017.
 */

public class openCamra implements Command {
    @Override
    public void execute(CommandModel commandModel) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        commandModel.getContext().startActivity(intent);
    }

    @Override
    public String getDefaultPhrase() {
        return "walk,walk";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }
}
