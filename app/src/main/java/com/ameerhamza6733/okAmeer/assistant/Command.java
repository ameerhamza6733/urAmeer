package com.ameerhamza6733.okAmeer.assistant;

import android.content.Context;

/**
 * Created by AmeerHamza on 6/17/2017.
 */

public interface Command {
     void execute(CommandModel commandModel);
    String getDefaultPhrase ();
    String getTtsPhrase(Context context);
}
