package com.ameerhamza6733.okAmeer.UI.Activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.UI.fragment.EnterFeedBackFragment;
import com.ameerhamza6733.okAmeer.UI.fragment.VoiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.SendSmsActivity;
import com.ameerhamza6733.okAmeer.interfacess.IGoogleSpeechRecognzerError;
import com.ameerhamza6733.okAmeer.utial.SpeechRecognizerManager;
import com.ameerhamza6733.okAmeer.utial.ExtraUnits;
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech;
import com.crashlytics.android.Crashlytics;
import com.github.clans.fab.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import lolodev.permissionswrapper.callback.OnRequestPermissionsCallBack;
import lolodev.permissionswrapper.wrapper.PermissionWrapper;

/**
 * Created by AmeerHamza on 7/17/2017.
 */

public class MainActivity extends AppCompatActivity implements IGoogleSpeechRecognzerError, SpeechRecognizerManager.OnMagicWordListener, EnterFeedBackFragment.OnUserFeedbackListener {
    private static final int TTS_CHECK_CODE = 9876;
    private VoiceRecgonizationFragment newIntance;
    private SpeechRecognizerManager mSpeechRecognizerManager;
    private boolean isSpeekButtonPressed = false;
    private final String TAG= "MainActivityTAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.setUserIdentifier(ExtraUnits.GetUserMobileId(getApplicationContext()));
        Crashlytics.log("MainActivity created");
        setContentView(R.layout.activity_main);
        ImageButton mSpeakButton = (ImageButton) findViewById(R.id.speakButton);

        mSpeakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Crashlytics.log("speech button pressed");
                isSpeekButtonPressed = true;
                checkPermissionAndProcess();
            }
        });
        FloatingActionButton FebRequestNewCommand = (FloatingActionButton) findViewById(R.id.fab_request_new_command);
        FloatingActionButton FebHelp = (FloatingActionButton) findViewById(R.id.fab_help);
        FloatingActionButton FebFeedBack = (FloatingActionButton) findViewById(R.id.fab_feedback);
        FebRequestNewCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VotingActivity.class);
                startActivity(intent);
            }
        });
        FebHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowSampleCommandsList.class);
                startActivity(intent);
            }
        });
        FebFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnterFeedBackFragment enterFeedbackFragemtn = new EnterFeedBackFragment();
                enterFeedbackFragemtn.setStyle(DialogFragment.STYLE_NORMAL,R.style.CustomTheme_Dialog_Min_Hight);
                enterFeedbackFragemtn.show(getSupportFragmentManager(),null);


            }
        });



    }

    private void checkPermissionAndProcess() {
        try {
            if (mSpeechRecognizerManager != null) {
                mSpeechRecognizerManager.destroy();
                mSpeechRecognizerManager = null;
            }
        } catch (Exception e) {
        }
        if (Build.VERSION.SDK_INT > 22) {

            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                askRunTimePermissions();

            } else {
                GoogleSpeechOROwn();
            }

        } else {
            GoogleSpeechOROwn();

        }


    }


    private void GoogleSpeechOROwn() {
        if (isSpeekButtonPressed) {
            showVoiceFragment();//Google speech recognizez
        } else {
            registerSpeechRecognizer();//sphinx4 speech recoginizer
        }
    }

    private void showVoiceFragment() {
        try {
            StopOwnSpeechRecognizer();
            if (!isFinishing()) {

                FragmentTransaction transactionFragment = getSupportFragmentManager().beginTransaction();
                newIntance = VoiceRecgonizationFragment.newInstance("en-IN", false, true);
                newIntance.setStyle(1, R.style.AppTheme);
                transactionFragment.add(android.R.id.content, newIntance).addToBackStack(null).commitAllowingStateLoss();

            }


        } catch (Exception e) {
            Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(int Error) {
        Log.d(getClass().getSimpleName(), "onError" + Error);
        if (newIntance != null)
            getSupportFragmentManager().beginTransaction().remove(newIntance).commitAllowingStateLoss();

        registerSpeechRecognizer();

    }

    private void askRunTimePermissions() {

        new PermissionWrapper.Builder(this)
                .addPermissions(new String[]{Manifest.permission.RECORD_AUDIO})
                //enable rationale message with a custom message

                //show settings dialog,in this case with default message base on requested permission/s
                .addPermissionsGoSettings(true)
                //enable callback to know what option was choosed
                .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                    @Override
                    public void onGrant() {
                        Log.i(SendSmsActivity.class.getSimpleName(), "Permission was granted.");
                        GoogleSpeechOROwn();

                    }

                    @Override
                    public void onDenied(String permission) {
                        Toast.makeText(MainActivity.this, "App need permission ", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).build().request();
    }

    @Override
    public void OnMagicWordDeceted(String word) {
        if (mSpeechRecognizerManager != null) {
            mSpeechRecognizerManager.destroy();
            mSpeechRecognizerManager = null;
        }
        showVoiceFragment();
    }


    @Override
    protected void onPause() {
        StopOwnSpeechRecognizer();
        Log.d(getClass().getSimpleName(), "onPause");
        super.onPause();

    }


    @Override
    protected void onStop() {
        Log.d(getClass().getSimpleName(), "onPause");

        super.onStop();
    }

    @Override
    protected void onResume() {
        checkPermissionAndProcess();
        super.onResume();
    }

    private void registerSpeechRecognizer() {
        if (mSpeechRecognizerManager == null) {
            mSpeechRecognizerManager = new SpeechRecognizerManager(getApplicationContext(), new SpeechRecognizerManager.LifeCycle() {
                @Override
                public void onPocketSphinxStart() {
                    try {
                        myTextToSpeech.intiTextToSpeech(getApplicationContext(), "hi", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mSpeechRecognizerManager.setOnResultListner(MainActivity.this);
        }


    }

    private void StopOwnSpeechRecognizer() {
        if (mSpeechRecognizerManager != null) {
            mSpeechRecognizerManager.destroy();
            mSpeechRecognizerManager = null;

        }
    }

    @Override
    public void onFeedBack(@NotNull String feedback) {
       try{
        String  subject=    "Feedback about "+getString(R.string.app_name) +" from "+ExtraUnits.GetUserMobileId(getApplicationContext());
           ExtraUnits.sendFeedbackEmail(new String[]{"develpore2017@gmail.com"},subject,getApplicationContext());
           forceToCrash();
       }catch (Exception e){
           Crashlytics.logException(new Exception("new Feedback from "+ExtraUnits.GetUserMobileId(getApplicationContext()) ));
       }
    }
    private Exception forceToCrash() throws Exception {
        throw new Exception();
    }
}
