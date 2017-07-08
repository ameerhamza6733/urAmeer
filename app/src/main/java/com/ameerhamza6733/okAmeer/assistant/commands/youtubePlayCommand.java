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
        context.startActivity(new Intent(context,youtubePlayActivity.class));
    }

    @Override
    public String getDefaultPhrase() {
        return "YouTube,گانا لگاؤ \u200B\u200BYouTube,YouTube سے گانا لگاؤ";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }
}
