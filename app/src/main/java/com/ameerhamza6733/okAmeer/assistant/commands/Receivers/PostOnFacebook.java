package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.UI.fragment.voiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.interfacess.noNeedCommander;
import com.ameerhamza6733.okAmeer.utial.TTSService;

import java.util.List;

public class PostOnFacebook extends AppCompatActivity implements noNeedCommander {

    private BroadcastReceiver broadcastReceiver;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_on_facebook);
        FloatingActionButton mRetry = (FloatingActionButton) findViewById(R.id.febRetry);
        intiTextToSpeech("hi",getResources().getString(R.string.aap_phesabuk_par_kya_post_karana_chaahate_hain));
        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVoiceRegoniztionFragment();
            }
        });
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra("com.service.message");
                showVoiceRegoniztionFragment();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
                new IntentFilter("com.service.result"));
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }


    private void showVoiceRegoniztionFragment() {


        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            voiceRecgonizationFragment newIntance = voiceRecgonizationFragment.newInstance("en-IN", false, false);
            newIntance.show(fragmentManager, "postOnfacebook");
            newIntance.setStyle(1, R.style.Theme_AppCompat_Dialog_MinWidth);
        } catch (Exception e) {
            Toast.makeText(this, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNoCommandrExcute(String Queary) {
        try {
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, Queary);
            PackageManager pm = getPackageManager();
            List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
            for (final ResolveInfo app : activityList) {
                if ((app.activityInfo.name).contains("facebook")) {
                    final ActivityInfo activity = app.activityInfo;
                    final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                    shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    shareIntent.setComponent(name);
                    startActivity(shareIntent);
                    break;
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void intiTextToSpeech(final String LEN, final String text) {

        try {
            Intent i = new Intent(PostOnFacebook.this, TTSService.class);
            i.putExtra("toSpeak", text);
            i.putExtra("Language", LEN);
            PostOnFacebook.this.startService(i);
        } catch (Exception e) {
            Toast.makeText(PostOnFacebook.this, "error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}




