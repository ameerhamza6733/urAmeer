package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.CommandModel;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.YoutubePlayActivity;
/**
 * Created by AmeerHamza on 7/5/2017.
 */
public class YoutubePlayCommand implements Command {
    @Override
    public void execute(CommandModel commandModel) {
        Log.d("youtube","playing song from youtube");
        Intent intent= new Intent(commandModel.getContext(),YoutubePlayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        commandModel.getContext().startActivity(intent);
    }

    @Override
    public String getDefaultPhrase() {
        return "یوٹیوب سے گانا لگاؤ,YouTube se gana lagao";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }
}
