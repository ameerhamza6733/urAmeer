package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.ComponentName;
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
        Intent intent = new Intent("org.tensorflow.demo");
        commandModel.getContext().startActivity(intent);
        launchComponent("org.tensorflow.demo","ClassifierActivity",commandModel.getContext());
    }

    @Override
    public String getDefaultPhrase() {
        return "walk,walk";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }

    private void  launchComponent(String packageName, String name,Context context){
        Intent launch_intent = new Intent("android.intent.action.MAIN");
        launch_intent.addCategory("android.intent.category.LAUNCHER");
        launch_intent.setComponent(new ComponentName(packageName, name));
        launch_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(launch_intent);
    }

}
