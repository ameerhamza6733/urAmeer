package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.util.Log;

import com.ameerhamza6733.okAmeer.assistant.Command;

/**
 * Created by AmeerHamza on 6/17/2017.
 */

public class FlashLightOffCommand implements Command {
    @Override
    public void execute(Context context, String predicate) {
        Log.d("Flashlight","turing of flash light");
    }

    @Override
    public String getDefaultPhrase() {
        return "فلیش روشنی بند,لائٹ بند,لائٹ آف";
    }
}
