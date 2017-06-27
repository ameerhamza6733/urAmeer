package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.content.Intent;
import android.os.Handler;
import android.provider.AlarmClock;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.fragment.voiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.interfacess.NonHindiQurary;
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech;

public class setAlarmActivty extends AppCompatActivity implements NonHindiQurary {

    public static final String EXTRA_TIME="extraTime";
    private voiceRecgonizationFragment newIntance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm_activty);
        try {
            Intent i = getIntent();
            String predicate = i.getStringExtra(EXTRA_TIME);
            int time[] = praseAlramTime(predicate);
            if (time != null) {
                setAlramNow(time);
            }else {
                Toast.makeText(this,"الارم کا ٹائم کیا ہے",Toast.LENGTH_LONG).show();
                new myTextToSpeech(this,"hi","आप कितने बजे का अलार्म सेट करना चाहते हैं");
                strtVoiceRegonizerFragment();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Exception" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void strtVoiceRegonizerFragment() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                     newIntance = voiceRecgonizationFragment.newInstance("hi", false);
                    newIntance.show(fragmentManager, setAlarmActivty.this.getClass().getSimpleName());
                    newIntance.setStyle(1, R.style.AppTheme);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, 1000);
    }

    private void setAlramNow(int[] time) {
        Log.d("setAlarm", "hour" + time[0]+time[1]);

        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm");
        i.putExtra(AlarmClock.EXTRA_HOUR, time[0]);
        i.putExtra(AlarmClock.EXTRA_MINUTES, time[1]);
        this.startActivity(i);
        finish();
    }

    private int[] praseAlramTime(String predicate) throws Exception {
        String rawTime = predicate.replaceAll("[^0-9]", "");
        int time[] = new int[2];


        if (rawTime.length() == 4) {


            time[0] = Integer.parseInt(rawTime.substring(0, 2));
            time[1] = Integer.parseInt(rawTime.substring(2, 4));

            return time;
        } else if (rawTime.length() == 3) {
            time[0] = Integer.parseInt(String.valueOf(rawTime.charAt(0)));
            time[1] = Integer.parseInt(rawTime.substring(1,3));
            return time;

        }

        return null;
    }

    @Override
    public void onNonHindiQuraryRecived(String Queary) {
        try {
            int time[] = praseAlramTime(Queary);
            if (time != null)
            {
                setAlramNow(time);
                newIntance.dismiss();

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
