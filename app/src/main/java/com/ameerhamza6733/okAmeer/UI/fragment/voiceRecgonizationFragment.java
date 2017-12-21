package com.ameerhamza6733.okAmeer.UI.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.CommandInvoker;
import com.ameerhamza6733.okAmeer.interfacess.onGoogleSpeechRecognzerError;
import com.ameerhamza6733.okAmeer.interfacess.tranlaterCallback;
import com.ameerhamza6733.okAmeer.utial.TTSService;
import com.ameerhamza6733.okAmeer.utial.sendToActivtys;
import com.ameerhamza6733.okAmeer.utial.tranlater;
import com.github.zagum.speechrecognitionview.RecognitionProgressView;
import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter;

import java.util.ArrayList;

public class voiceRecgonizationFragment extends DialogFragment {

    private static voiceRecgonizationFragment newIntance;
    private String LANGUAGES = "hi";
    private boolean isTranslateNeeded = true;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION_CODE = 1;
    private static final String TAG = "voiceRecgonizationFragm";
    private static final String TRANLATER_TARGET_LEN = "ur";
    private static final String TRANSLATER_SOURCE_LAN = "hi";


    private static final String BUNDLE_KEY_EXTRA_TRANSLATE_NEED = "BUNDLE_KEY_TRANSLATE_NEED";
    private static final String BUNDLE_KEY_EXTRA_LANGUAGES = "BUNDLE_KEY_EXTRA_LANGUAGES";
    private static final String BUNDLE_KEY_EXTRA_EXCUTE_COMANDER = "BUNDLE_KEY_EXTRA_EXCUTE_COMANDER";
    public static final String PACKAGE_NAME_GOOGLE_NOW = "com.google.android.googlequicksearchbox";
    public static final String ACTIVITY_INSTALL_OFFLINE_FILES = "com.google.android.voicesearch.greco3.languagepack.InstallActivity";


    private RecognitionProgressView recognitionProgressView;
    protected SpeechRecognizer speechRecognizer;
    private sendToActivtys sendDateToNOnHidni;
    private Bundle results;
    Handler handlerError;
    Handler handler;
    private Runnable handlerErrorRunnable;
    private onGoogleSpeechRecognzerError onGoogleSpeechRecognzerError;
    private boolean voiceRecgonizerSuccess = false;
    private boolean excuteCommander = true;


    public static voiceRecgonizationFragment newInstance(String LANGUAGES, boolean isTranslateNeeded, boolean excuteCommander) {
        newIntance = new voiceRecgonizationFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_KEY_EXTRA_LANGUAGES, LANGUAGES);
        args.putBoolean(BUNDLE_KEY_EXTRA_TRANSLATE_NEED, isTranslateNeeded);
        args.putBoolean(BUNDLE_KEY_EXTRA_EXCUTE_COMANDER, excuteCommander);
        newIntance.setArguments(args);
        return newIntance;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.voice_recg_fragment, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int[] colors = new int[]{ContextCompat.getColor(getActivity(), R.color.color1), ContextCompat.getColor(getActivity(), R.color.color2), ContextCompat.getColor(getActivity(), R.color.color3), ContextCompat.getColor(getActivity(), R.color.color4), ContextCompat.getColor(getActivity(), R.color.color5)};
        Log.d(TAG, "onViewCreated");


        LANGUAGES = getArguments().getString(BUNDLE_KEY_EXTRA_LANGUAGES);
        isTranslateNeeded = getArguments().getBoolean(BUNDLE_KEY_EXTRA_TRANSLATE_NEED);
        excuteCommander = getArguments().getBoolean(BUNDLE_KEY_EXTRA_EXCUTE_COMANDER);

        int[] heights = new int[]{60, 76, 58, 80, 55};
        try {
            Log.d(TAG, "" + SpeechRecognizer.isRecognitionAvailable(getActivity()));
            this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity(), ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService"));
            this.recognitionProgressView = (RecognitionProgressView) view.findViewById(R.id.recognition_view);
            this.recognitionProgressView.setSpeechRecognizer(this.speechRecognizer);
            this.recognitionProgressView.setColors(colors);
            this.recognitionProgressView.setBarMaxHeightsInDp(heights);
            this.recognitionProgressView.play();

            startRecognition();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        this.recognitionProgressView.postDelayed(new Runnable() {
//            public void run() {
//                startRecognition();
//            }
//        }, 10);
        this.recognitionProgressView.setRecognitionListener(new RecognitionListenerAdapter() {
            public void onResults(Bundle results) {
                voiceRecgonizationFragment.this.results = results;
                try {

                    voiceRecgonizationFragment.this.showResults(results);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEndOfSpeech() {
                Log.d(TAG, "onEndOfSpeech");

            }

            @Override
            public void onError(final int error) {
                super.onError(error);

                Log.d(TAG, "onError code" + error);

                if(onGoogleSpeechRecognzerError!=null)
                onGoogleSpeechRecognzerError.onError(error);
                if (error == 4) {


                    // promoteUserToDownlaodOfflineSpeachData();
                    Toast.makeText(getActivity(), "انٹرنیٹ دستیاب نہیں ہے", Toast.LENGTH_LONG).show();
                    dismissFragment(); // getSupportFragmentManager().beginTransaction().remove(newIntance).commitAllowingStateLoss();

                }
                else if (!(error == 8)) {

                    handlerError = new Handler();
                    handlerErrorRunnable = new Runnable() {
                        @Override
                        public void run() {

                            try {
                                if (isAdded())
                                    Toast.makeText(getActivity(), "try again: Error code " + error, Toast.LENGTH_LONG).show();
                                dismissFragment(); // getSupportFragmentManager().beginTransaction().remove(newIntance).commitAllowingStateLoss();

                            } catch (Exception i) {
                                i.printStackTrace();
                            }
                        }
                    };
                    voiceRecgonizationFragment.this.handlerError.postDelayed(handlerErrorRunnable, 1000);
                }
            }

        });


    }


    private void showResults(Bundle results) throws Exception {
        final ArrayList<String> matches = results.getStringArrayList("results_recognition");
        assert matches != null;
        Toast.makeText(getActivity(), "You said: " + matches.get(0), Toast.LENGTH_LONG).show();
        if (isTranslateNeeded) {
            if (matches != null)
                yas(matches);
            else {
                Toast.makeText(getActivity(), "please try again..", Toast.LENGTH_LONG).show();
                dismissFragment(); // getSupportFragmentManager().beginTransaction().remove(newIntance).commitAllowingStateLoss();
            }
        } else {

            if (matches != null) {
                try {
                    invockCommanderOrNot(results.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }

    }


    private void yas(final ArrayList<String> matches) {
        Log.d(TAG, "match" + matches.get(0));
        new tranlater(new tranlaterCallback() {
            @Override
            public void onError(String str) {
                Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
                dismissFragment(); // getSupportFragmentManager().beginTransaction().remove(newIntance).commitAllowingStateLoss();
            }

            @Override
            public void onSuccess(final String str) {
                //    if (str != null)
                //  invockCommanderOrNot(new ArrayList<String>().add(str));

            }
        }, matches.get(0), TRANSLATER_SOURCE_LAN, TRANLATER_TARGET_LEN).excute();

    }

    private void invockCommanderOrNot(final ArrayList<String> str) {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (excuteCommander) {
                    try {
                        dismissFragment(); // getSupportFragmentManager().beginTransaction().remove(newIntance).commitAllowingStateLoss();
                        boolean isCommandFound = CommandInvoker.excute(getActivity(), str);
                        if (!isCommandFound) {

                            Intent i = new Intent(getActivity(), TTSService.class);
                            i.putExtra("toSpeak", getString(R.string.Dobaara_say_koshish_keejie_muja_aapakee_ki_samajh_nahi_aaee));
                            i.putExtra("Language", "hi");
                            getActivity().startService(i);
                            onGoogleSpeechRecognzerError.onError(-1);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    try {
                        doNotExcuteCommander(str.get(0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }
        }, 1000);
    }

    private void dismissFragment() {

        if (newIntance != null)
            try {
                getActivity().getSupportFragmentManager().beginTransaction().remove(newIntance).commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    private void doNotExcuteCommander(String date) throws Exception {
        Log.d("voice regonizer", "do not excute commander");
        sendDateToNOnHidni = new sendToActivtys();
        sendDateToNOnHidni.sendingDataToActivitys(getActivity(), date, getActivity().getClass().getSimpleName());
    }


    private void startRecognition() throws Exception {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        if (isAdded()) {
            intent.putExtra("calling_package", getActivity().getPackageName());
            intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
            intent.putExtra("android.speech.extra.LANGUAGE", LANGUAGES);
            intent.putExtra("android.speech.extras.SPEECH_INPUT_MINIMUM_LENGTH_MILLIS", 3000);
            intent.putExtra("android.speech.extras.SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS", 3000);
            intent.putExtra("android.speech.extras.SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS", 3000);
            this.speechRecognizer.startListening(intent);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onGoogleSpeechRecognzerError = (onGoogleSpeechRecognzerError) context;
        } catch (Exception e) {

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }


    @Override
    public void onPause() {
        Log.d(TAG,"onPause");
        if (this.speechRecognizer != null) {
            {
                Log.d(TAG,"stopListening");

                this.speechRecognizer.cancel();
                speechRecognizer.destroy();


            }
        }
        if (this.recognitionProgressView != null) {
            this.recognitionProgressView.stop();
            this.recognitionProgressView = null;
        }
        if (handlerError != null)
            handlerError.removeCallbacks(handlerErrorRunnable);
        super.onPause();
    }
}
