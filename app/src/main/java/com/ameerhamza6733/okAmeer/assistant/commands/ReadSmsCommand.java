package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;

import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.CommandModel;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.SmsUnreadActivity;

/**
 * Created by AmeerHamza on 7/1/2017.
 */

public class ReadSmsCommand implements Command {
    @Override
    public void execute(CommandModel commandModel) {
        commandModel.getContext().startActivity(new Intent(commandModel.getContext(),SmsUnreadActivity.class));

    }

    @Override
    public String getDefaultPhrase() {

        return "پڑھو SMS ,میسج پڑھو,message padho";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }
}
