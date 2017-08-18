package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.youtubePlayActivity;
/**
 * Created by AmeerHamza on 7/5/2017.
 */
public class youtubePlayCommand implements Command {
    @Override
    public void execute(Context context, String predicate) {
        Log.d("youtube","playing song from youtube");
        Intent intent= new Intent(context,youtubePlayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public String getDefaultPhrase() {
        return "YouTube se gana lagao,YouTube سے گانا لگاؤ";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }
}
