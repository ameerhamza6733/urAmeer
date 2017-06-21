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
            int time []= praseAlramTime(predicate);



          if(time!=null){
              Log.d("setAlarm","hour"+time[0]);
             try {
                 Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                 i.putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm");
                 i.putExtra(AlarmClock.EXTRA_HOUR, time[0]);
                 i.putExtra(AlarmClock.EXTRA_MINUTES, time[0]);
                 context.startActivity(i);
             }catch (Exception e)
             {
                 Toast.makeText(context,"Exception"+e.getMessage(),Toast.LENGTH_SHORT).show();
             }
          }
        }
//        Log.d("seting alram","time = "+predicate.replaceAll("[^0-9]", ""));
//        Intent intent = new Intent(context,setAlarmActivty.class);
//        intent.putExtra(setAlarmActivty.EXTRA_TIME,predicate);
//        context.startActivity(intent);
    

    private int [] praseAlramTime(String predicate) {
       String rawTime= predicate.replaceAll("[^0-9]", "");
        int time [] = new int[2];


        if(rawTime.length()==4)
        {


            time[0] = Integer.parseInt(rawTime.substring(0,1));
            time[1] = Integer.parseInt(rawTime.substring(2,3));

            return time;
        }else if(rawTime.length()==3)
        {
            time[0] = Integer.parseInt(rawTime.substring(0,1));
            time[1] = Integer.parseInt(String.valueOf(rawTime.charAt(3)));
            return time;

        }

        return null;
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
