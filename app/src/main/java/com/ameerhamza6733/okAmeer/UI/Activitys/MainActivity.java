package com.ameerhamza6733.okAmeer.UI.Activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.UI.fragment.voiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.CallingActivity;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.sendSmsActivity;
import com.ameerhamza6733.okAmeer.interfacess.onErrorSevenvoiceRecgoniztion;
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.webianks.easy_feedback.EasyFeedback;

import java.util.List;

import lolodev.permissionswrapper.callback.OnRequestPermissionsCallBack;
import lolodev.permissionswrapper.wrapper.PermissionWrapper;

/**
 * Created by AmeerHamza on 7/17/2017.
 */

public class MainActivity extends AppCompatActivity implements onErrorSevenvoiceRecgoniztion {
    private voiceRecgonizationFragment newIntance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton mSpeakButton = (ImageButton) findViewById(R.id.speakButton);
        try {
            myTextToSpeech.intiTextToSpeech(this, "hi", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSpeakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT > 22) {

                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        askRunTimePermissions();
                        return;
                    }else

                        showVoiceFragment();
                } else

                showVoiceFragment();
            }
        });
        FloatingActionButton FebRequestNewCommand = (FloatingActionButton) findViewById(R.id.fab_request_new_command);
        FloatingActionButton FebHelp= (FloatingActionButton) findViewById(R.id.fab_help);
        FloatingActionButton FebFeedBack = (FloatingActionButton) findViewById(R.id.fab_feedback);
        FebRequestNewCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, votingActivity.class);
                startActivity(intent);
            }
        });
        FebHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, show_sample_commands_list.class);
                startActivity(intent);
            }
        });
        FebFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EasyFeedback.Builder(MainActivity.this)
                        .withEmail("ameerhamza6733@gmail.com")
                        .withSystemInfo()

                        .build()
                        .start();
            }
        });



    }

    private void showVoiceFragment() {
        try {
            if(!isFinishing()){

                FragmentTransaction transactionFragment = getSupportFragmentManager().beginTransaction();
                newIntance = voiceRecgonizationFragment.newInstance("en-IN", false, true);
                newIntance.setStyle(1, R.style.AppTheme);
                transactionFragment.add(android.R.id.content, newIntance).addToBackStack(null).commitAllowingStateLoss();


                //  newIntance.show(fragmentManager, "fragment_voice_input");

            }


        }catch (Exception e){
            Toast.makeText(this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError7() {
        Toast.makeText(this, "onError 7", Toast.LENGTH_SHORT).show();
        if(newIntance!=null)
            getSupportFragmentManager().beginTransaction().remove(newIntance).commitAllowingStateLoss();
    }
    public void getRecognizeIntent()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN");
        startActivityForResult(intent, 111);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111 && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
           Log.d("mainActivty","result"+results.get(0));;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void askRunTimePermissions() {
        new PermissionWrapper.Builder(this)
                .addPermissions(new String[]{ Manifest.permission.RECORD_AUDIO})
                //enable rationale message with a custom message

                //show settings dialog,in this case with default message base on requested permission/s
                .addPermissionsGoSettings(true)
                //enable callback to know what option was choosed
                .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                    @Override
                    public void onGrant() {
                        Log.i(sendSmsActivity.class.getSimpleName(), "Permission was granted.");
                        showVoiceFragment();


                    }

                    @Override
                    public void onDenied(String permission) {
                        Log.i(sendSmsActivity.class.getSimpleName(), "Permission was not granted.");
                    }
                }).build().request();
    }
}
