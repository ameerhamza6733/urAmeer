package com.ameerhamza6733.okAmeer.utial;

/**
 * Created by AmeerHamza on 7/2/2017.
 */
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;


import com.ameerhamza6733.okAmeer.UI.Activitys.MyTutorialActivity;
import com.ameerhamza6733.okAmeer.utial.TTSService;
import com.crashlytics.android.Crashlytics;
import com.hololo.tutorial.library.TutorialActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
public class TTSHelper extends UtteranceProgressListener implements TextToSpeech.OnInitListener {
    private final String language;
    private TextToSpeech tts;
    private Context context;
    private String toSpeak;
    private LocalBroadcastManager localBroadcastManager;
    private static String TAG = "TTSHelperTAG";



    public TTSHelper(Context context, String toSpeak, String language,LocalBroadcastManager  localBroadcastManager) {
        this.context = context;
        tts = new TextToSpeech(context, this);
        tts.setOnUtteranceProgressListener(this);
        this.toSpeak = toSpeak;
        this.language=language;
        this.localBroadcastManager=localBroadcastManager;
    }

    public void stop() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            Log.d(TAG,"installed text to speech engines names "+tts.getEngines());
            Crashlytics.log(Log.DEBUG,TAG,"installed text to speech engines names "+tts.getEngines());

            int result = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                result = tts.setLanguage(Locale.forLanguageTag(language));
            }

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context, "language_not_supported", Toast.LENGTH_LONG).show();
            } else {
                speakOut();
            }

        } else {
            Toast.makeText(context, "tts_failed Error code "+status, Toast.LENGTH_LONG).show();
            if(status == -1) //
            {
                Intent intent = new Intent(context, MyTutorialActivity.class);
                intent.setAction(MyTutorialActivity.ACTION_GOOGLE_TTS_NOT_INSTALLED);
                context.startActivity(intent);
            }else {
                Toast.makeText(context, "please make sure you select Google text to speech in next screen ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setAction("com.android.settings.TTS_SETTINGS");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }

    }

    private void speakOut() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, toSpeak);
        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, map);
        //Toast.makeText(context, toSpeak, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStart(String s) {

    }

    @Override
    public void onDone(String s) {
        Intent intent = new Intent("com.service.result");
        if(s != null)
            intent.putExtra("com.service.message", s);
      localBroadcastManager.sendBroadcast(intent);
        context.stopService(new Intent(context, TTSService.class));
    }

    @Override
    public void onError(String s) {

    }
    public static final  void  openGooglePlayToInstallTTS(Application application){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.tts"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(intent);

        } catch (android.content.ActivityNotFoundException anfe) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.tts"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(intent);
        }
    }
}