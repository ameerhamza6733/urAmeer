package com.ameerhamza6733.okAmeer.assistant;

import android.content.Context;

/**
 * Created by AmeerHamza on 8/27/2017.
 */

public class CommandModel {
    private  String ExtraPhrase;
    private String Predicate;
    private Context context;

    public CommandModel(String ExtraPhrase, String predicate, Context context) {
        this.ExtraPhrase = ExtraPhrase;
        Predicate = predicate;
        this.context = context;
    }
    public CommandModel(String predicate, Context context) {

        Predicate = predicate;
        this.context = context;
    }

    public String getCommandExtraPhrase() {
        return ExtraPhrase;
    }

    public String getPredicate() {
        return Predicate;
    }

    public Context getContext() {
        return context;
    }
}
