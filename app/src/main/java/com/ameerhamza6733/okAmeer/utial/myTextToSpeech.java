package com.ameerhamza6733.okAmeer.utial;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.ameerhamza6733.okAmeer.interfacess.mttsListener;

import java.util.Locale;

/**
 * Created by AmeerHamza on 6/20/2017.
 */

public class myTextToSpeech {

    private static TextToSpeech textToSpeech;
    private static mttsListener mTextToSpechDone;


//    public myTextToSpeech(Context context, String language, String text) {
//        this.context = context;
//        this.textToSpeech = new TextToSpeech(this.context,this);
//        this.language = language;
//        this.text = text;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void onInit(int status) {
//        if(status == TextToSpeech.SUCCESS);
//        {
//            Log.d("myTextToSpeech","seuccess"+this.text);
//            this.textToSpeech.setLanguage(Locale.forLanguageTag(this.language));
//            String utteranceId=this.hashCode() + "";
//            this.textToSpeech.speak(this.text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
//            this.textToSpeech.speak(this.text, TextToSpeech.QUEUE_FLUSH, null,utteranceId);
//
//        }


    public static void intiTextToSpeech(Context context, final String language, final String text) throws Exception{
        mTextToSpechDone = (mttsListener) context;
        textToSpeech = new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textToSpeech.setLanguage(Locale.forLanguageTag(language));
                        String utteranceId = this.hashCode() + "";
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);

                    }
                }


            }


        });
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                mTextToSpechDone.onFinsh();

            }

            @Override
            public void onError(String utteranceId) {

            }
        });
    }

    public static void stop() throws Exception {
        if (textToSpeech != null)
            textToSpeech.shutdown();

    }
}
