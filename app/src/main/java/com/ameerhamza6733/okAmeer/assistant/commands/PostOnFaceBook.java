package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.CommandModel;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.PostOnFacebook;

/**
 * Created by AmeerHamza on 8/9/2017.
 */
// this mathod work only if user have install the facebook lite
public class PostOnFaceBook implements Command {
    @Override
    public void execute(final CommandModel commandModel) {
        Log.d("Flashlight","turing on flash light");
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                Intent i = new Intent(commandModel.getContext(), PostOnFacebook.class);
                commandModel.getContext().startActivity(i);
                return true;
            }
        });
        handler.sendEmptyMessageDelayed(0, 500);

    }

    @Override
    public String getDefaultPhrase() {
        return "facebook par post karo,Facebook پر پوسٹ کرو";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }
}
