package com.ameerhamza6733.okAmeer.UI.Activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.utial.TTSHelper;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.StepFragment;
import com.hololo.tutorial.library.TutorialActivity;

public class MyTutorialActivity extends TutorialActivity implements StepFragment.OnButtonClickedListener {
    public static final String ACTION_GOOGLE_TTS_NOT_INSTALLED = "google text to speech not installed";
    public static final String ACTION_CHOOSE_GOOGLE_TTS_DEFAULT = "ACTION_CHOOSE_GOOGLE_TTS_DEFAULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_GOOGLE_TTS_NOT_INSTALLED)) {
                show_Instructons_fragment("Install/Enable Google TTS ", "Please install/Enable Google text to speech", "#03A9F4", "Install", R.drawable.installgoogletts, 1);

            }else if (intent.getAction().equals(ACTION_CHOOSE_GOOGLE_TTS_DEFAULT)){
                show_Instructons_fragment("Choose Google TTS as Default","Please choose google text to speech as your default Engine ","#4CAF50","Choose Default", R.drawable.choosedefaultgoogletts,1);
            }
        }
    }

    private void show_Instructons_fragment(String title, String Content, String BackgroundColor, String ButtonTitle, int mDrawable, int buttonEnable) {
        addFragment(new Step.Builder().setTitle(title)
                .setContent(Content)
                .setButtonEnable(buttonEnable)
                .setBackgroundColor(Color.parseColor(BackgroundColor)) // int background color
                .setDrawable(mDrawable) // int top drawable
                .setButtonTitle(ButtonTitle)
                .build());
    }


    @Override
    public void onButtonClicked(View view) {
        Button button = (Button) view;
        switch (button.getText().toString()) {
            case "Install":
                TTSHelper.openGooglePlayToInstallTTS(getApplication());
                break;
            case "Choose Default":
               Intent ttsSettingIntent = new Intent();
                ttsSettingIntent.setAction("com.android.settings.TTS_SETTINGS");
                ttsSettingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(ttsSettingIntent);
                break;

        }
    }

    @Override
    public void finishTutorial() {
        OpenMainActivty();
    }

    @Override
    public void onBackPressed() {
        OpenMainActivty();
    }

    private void OpenMainActivty(){
        Intent ttsSettingIntent = new Intent(this,InitializationAppActivity.class);
        ttsSettingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(ttsSettingIntent);
    }
}
