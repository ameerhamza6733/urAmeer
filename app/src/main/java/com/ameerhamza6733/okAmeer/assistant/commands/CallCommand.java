package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;

import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.CallingActivity;

/**
 * Created by AmeerHamza on 6/23/2017.
 */

public class CallCommand implements Command {
    @Override
    public void execute(Context context, String predicate) {

        Intent intent =  new Intent(context, CallingActivity.class);
        intent.putExtra(CallingActivity.EXTRA_NAME,predicate);

        context.startActivity(intent);
    }

    @Override
    public String getDefaultPhrase() {
        return "کال کرو,فون کرو,call karo,phone karo";
    }

    @Override
    public String getTtsPhrase(Context context) {
       return "आप किसी कॉल करना चाहते हैं";
    }
}
