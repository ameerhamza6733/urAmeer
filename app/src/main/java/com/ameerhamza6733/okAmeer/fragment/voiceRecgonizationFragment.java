package com.ameerhamza6733.okAmeer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.CommandInvoker;
import com.ameerhamza6733.okAmeer.interfacess.tranlaterCallback;
import com.ameerhamza6733.okAmeer.utial.tranlater;

import com.github.zagum.speechrecognitionview.RecognitionProgressView;
import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter;

import java.util.ArrayList;

public class voiceRecgonizationFragment extends DialogFragment {
   // static final /* synthetic */ boolean $assertionsDisabled = (!voiceRecgonizationFragment.class.desiredAssertionStatus() ? true : $assertionsDisabled);
    private static final String LANGUAGES = "hi";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION_CODE = 1;
    private static final String TAG = "voiceRecgonizationFragm";
    private static final String TRANLATER_TARGET_LEN = "ur";
    private static final String TRANSLATER_SOURCE_LAN = "hi";
    private static final String TRANSLATER_TYPE = "Google";

    private RecognitionProgressView recognitionProgressView;
    protected SpeechRecognizer speechRecognizer;

    public static voiceRecgonizationFragment newInstance(String title) {
        voiceRecgonizationFragment frag = new voiceRecgonizationFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.voice_recg_fragment, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int[] colors = new int[]{ContextCompat.getColor(getActivity(), R.color.color1), ContextCompat.getColor(getActivity(), R.color.color2), ContextCompat.getColor(getActivity(), R.color.color3), ContextCompat.getColor(getActivity(), R.color.color4), ContextCompat.getColor(getActivity(), R.color.color5)};
        Log.d(TAG, "onViewCreated");
        int[] heights = new int[]{60, 76, 58, 80, 55};
        requestPermission();
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
        this.recognitionProgressView = (RecognitionProgressView) view.findViewById(R.id.recognition_view);
        this.recognitionProgressView.setSpeechRecognizer(this.speechRecognizer);
        this.recognitionProgressView.setColors(colors);
        this.recognitionProgressView.setBarMaxHeightsInDp(heights);
        this.recognitionProgressView.play();
        startRecognition();
        this.recognitionProgressView.postDelayed(new Runnable() {
            public void run() {
            }
        }, 10);
        this.recognitionProgressView.setRecognitionListener(new RecognitionListenerAdapter() {
            public void onResults(Bundle results) {
                voiceRecgonizationFragment.this.showResults(results);
            }
        });
    }


    public void onDestroy() {
        if (this.speechRecognizer != null) {
            this.speechRecognizer.destroy();
            Log.d(TAG, "onDestroy");
        }
        super.onDestroy();
    }

    private void startRecognition() {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("calling_package", getActivity().getPackageName());
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        intent.putExtra("android.speech.extra.LANGUAGE", LANGUAGES);
        intent.putExtra("android.speech.extras.SPEECH_INPUT_MINIMUM_LENGTH_MILLIS", 1000);
        intent.putExtra("android.speech.extras.SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS", 1000);
        intent.putExtra("android.speech.extras.SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS", 1000);
        this.speechRecognizer.startListening(intent);
    }


    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), "android.permission.RECORD_AUDIO")) {
            Toast.makeText(getActivity(), "Requires RECORD_AUDIO permission", Toast.LENGTH_LONG).show();
            return;
        }
        FragmentActivity activity = getActivity();
        String[] strArr = new String[REQUEST_RECORD_AUDIO_PERMISSION_CODE];
        strArr[0] = "android.permission.RECORD_AUDIO";
        ActivityCompat.requestPermissions(activity, strArr, REQUEST_RECORD_AUDIO_PERMISSION_CODE);
    }
    private void showResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList("results_recognition");
        if ( matches != null) {

           new tranlater(new tranlaterCallback() {
               @Override
               public void onError(String str) {

                   Toast.makeText(getActivity(),str,Toast.LENGTH_LONG).show();
                   voiceRecgonizationFragment.this.dismiss();
               }

               @Override
               public void onSuccess(final String str) {
                   Toast.makeText(getActivity(),str,Toast.LENGTH_LONG).show();

                   if (str!=null)
                   {

                       CommandInvoker.excute(getActivity(),str);
                       new Handler().postDelayed(new Runnable() {
                           public void run() {
                               voiceRecgonizationFragment.this.dismiss();

                           }
                       }, 100);

                   }
               }
           }, matches.get(0),TRANSLATER_SOURCE_LAN,TRANLATER_TARGET_LEN).excute();
        }

    }
}
