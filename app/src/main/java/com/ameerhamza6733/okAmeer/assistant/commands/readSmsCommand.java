package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;

import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.smsUnreadActivity;

/**
 * Created by AmeerHamza on 7/1/2017.
 */

public class readSmsCommand implements Command {
    @Override
    public void execute(Context context, String predicate) {
        context.startActivity(new Intent(context,smsUnreadActivity.class));

    }

    @Override
    public String getDefaultPhrase() {

        return "پڑھو SMS ,میسج پڑھو";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }
}
