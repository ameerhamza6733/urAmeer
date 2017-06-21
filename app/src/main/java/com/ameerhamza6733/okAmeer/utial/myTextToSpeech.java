package com.ameerhamza6733.okAmeer.utial;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Locale;

/**
 * Created by AmeerHamza on 6/20/2017.
 */

public class myTextToSpeech implements TextToSpeech.OnInitListener {
    private Context context;
    private TextToSpeech textToSpeech ;
    private String language;
    private String text;


    public myTextToSpeech(Context context, String language, String text) {
        this.context = context;
        this.textToSpeech = new TextToSpeech(this.context,this);
        this.language = language;
        this.text = text;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS);
        {
            Log.d("myTextToSpeech","seuccess"+this.text);
            this.textToSpeech.setLanguage(Locale.forLanguageTag(this.language));
            String utteranceId=this.hashCode() + "";
            this.textToSpeech.speak(this.text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            this.textToSpeech.speak(this.text, TextToSpeech.QUEUE_FLUSH, null,utteranceId);

        }

    }
}
