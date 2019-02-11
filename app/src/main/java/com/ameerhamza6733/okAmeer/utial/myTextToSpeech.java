package com.ameerhamza6733.okAmeer.utial;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.UI.Activitys.MyTutorialActivity;
import com.crashlytics.android.Crashlytics;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by AmeerHamza on 6/20/2017.
 */

public class myTextToSpeech {

    private static final String TAG = "myTextToSpeechTAG";
    private static TextToSpeech textToSpeech;
    private static TextToSpeechListener textToSpeechListener;

    public static TextToSpeech intiTextToSpeech(final Context context, final String language, final String text) {

        textToSpeech = new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (textToSpeech != null && status == TextToSpeech.SUCCESS) {

                    Crashlytics.log(Log.DEBUG, TAG, "installed text to speech engines names " + textToSpeech.getEngines());
                    Crashlytics.log(Log.DEBUG, TAG, "default tts engine " + textToSpeech.getDefaultEngine());
                    if (!isGoogleDefaultTTS()) {
                        AskTOUserChooseGoogleTTS(context);
                        textToSpeech.stop();
                        textToSpeech.shutdown();
                        textToSpeech = null;
                    }
                    if (textToSpeech == null)
                        return;
                    int result = 0;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        result = textToSpeech.setLanguage(Locale.forLanguageTag(language));
                    }

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                        Toast.makeText(context, "language_not_supported please make sure you select Google text to speech in next screen ", Toast.LENGTH_LONG).show();


                    } else {

                        speakOut(text);
                    }
                } else {

                    if (status == -1) //
                    {
                        Toast.makeText(context, "please make sure Google text to speech ENABLE and UPDATE:  Error code " + status, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(context, MyTutorialActivity.class);
                        intent.setAction(MyTutorialActivity.ACTION_GOOGLE_TTS_NOT_INSTALLED);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "please make sure you select Google text to speech in next screen: Error code " + status, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setAction("com.android.settings.TTS_SETTINGS");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }

                }


            }


        });


        return textToSpeech;
    }

    private static void speakOut(String toSpeak) {
        if (textToSpeechListener!=null){
            textToSpeechListener.onTTSSuccess();
            textToSpeechListener=null;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, toSpeak);
        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, map);
        //Toast.makeText(context, toSpeak, Toast.LENGTH_LONG).show();

    }

    public static void stop() throws Exception {
        if (textToSpeech != null)
            textToSpeech.shutdown();
        textToSpeech = null;

    }

    private static boolean isGoogleDefaultTTS() {
        Log.d(TAG,""+textToSpeech.getDefaultEngine().equalsIgnoreCase("com.google.android.tts"));
        return textToSpeech.getDefaultEngine().equalsIgnoreCase("com.google.android.tts");
    }

    private static void AskTOUserChooseGoogleTTS(Context context) {
        Intent intent = new Intent(context, MyTutorialActivity.class);
        intent.setAction(MyTutorialActivity.ACTION_CHOOSE_GOOGLE_TTS_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void setTextToSpeechListener(TextToSpeechListener listener){
        textToSpeechListener=listener;
    }
    public interface TextToSpeechListener{
        void onTTSSuccess();
    }
}

