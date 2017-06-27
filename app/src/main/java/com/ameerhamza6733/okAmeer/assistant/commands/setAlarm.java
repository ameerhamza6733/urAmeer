package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.setAlarmActivty;

/**
 * Created by AmeerHamza on 6/20/2017.
 */

public class setAlarm implements Command {
    @Override
    public void execute(Context context, String predicate) {



        Intent intent = new Intent(context,setAlarmActivty.class);
        intent.putExtra(setAlarmActivty.EXTRA_TIME,predicate);
        context.startActivity(intent);
    }




    @Override
    public String getDefaultPhrase() {
        return "الارم,";
    }

    @Override
    public String getTtsPhrase() {
        return "आप का अलार्म सेट किया जा रहा है";
    }
    //  praseAlramTime()
}
