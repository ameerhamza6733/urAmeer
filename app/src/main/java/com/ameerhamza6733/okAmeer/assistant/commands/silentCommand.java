package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;

import com.ameerhamza6733.okAmeer.assistant.Command;

/**
 * Created by AmeerHamza on 7/3/2017.
 */

public class silentCommand implements Command {
    @Override
    public void execute(Context context, String predicate) {
        
    }

    @Override
    public String getDefaultPhrase() {
        return null;
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }
}
