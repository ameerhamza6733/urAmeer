package com.ameerhamza6733.okAmeer.utial;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.interfacess.mttsListener;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by AmeerHamza on 6/20/2017.
 */

public class myTextToSpeech {

    private static TextToSpeech textToSpeech;
    public static void intiTextToSpeech(final Context context, final String language, final String text) throws Exception{

        textToSpeech = new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = 0;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        result = textToSpeech.setLanguage(Locale.forLanguageTag(language));
                    }

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                        Toast.makeText(context, "language_not_supported please make sure you select Google text to speech in next screen ", Toast.LENGTH_LONG).show();



                    } else {
                        speakOut(text);
                    }
                } else {

                    if(status == -1) //
                    {
                        Toast.makeText(context, "please make sure Google text to speech ENABLE and UPDATE:  Error code "+status, Toast.LENGTH_LONG).show();

                        // this is will open play store if use have not install or disable google text to speach
                        try {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.tts")));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            context.  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.tts" )));
                        }
                    }else {
                        Toast.makeText(context, "please make sure you select Google text to speech in next screen: Error code "+status, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setAction("com.android.settings.TTS_SETTINGS");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }

                }


            }


        }, "com.google.android.tts");

        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {


            }

            @Override
            public void onError(String utteranceId) {

            }
        });
    }
    private static void  speakOut(String toSpeak) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, toSpeak);
        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, map);
        //Toast.makeText(context, toSpeak, Toast.LENGTH_LONG).show();

    }
    public static void stop() throws Exception {
        if (textToSpeech != null)
            textToSpeech.shutdown();

    }
}
