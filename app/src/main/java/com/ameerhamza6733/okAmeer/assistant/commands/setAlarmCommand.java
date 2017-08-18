package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.setAlarmActivty;

/**
 * Created by AmeerHamza on 6/20/2017.
 */

public class setAlarmCommand implements Command {

    @Override
    public void execute(Context context, String predicate) {



        Intent intent = new Intent(context,setAlarmActivty.class);
        intent.putExtra(setAlarmActivty.EXTRA_TIME,predicate);
        context.startActivity(intent);
    }




    @Override
    public String getDefaultPhrase() {
        return "الارم سیٹ کریں,الارم سیٹ کرو,الارم لگانا,الارم کا اطلاق کریں,alarm set karo,alarm lagao";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return context.getResources().getString(R.string.kis_Time_Pay_set_Karna_ha) ;
    }
    //  praseAlramTime()
}
