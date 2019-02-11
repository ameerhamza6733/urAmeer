package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.UI.fragment.VoiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.interfacess.INoNeedCommander;
import com.ameerhamza6733.okAmeer.interfacess.IGoogleSpeechRecognzerError;
import com.ameerhamza6733.okAmeer.utial.SMSUtils;
import com.ameerhamza6733.okAmeer.utial.SmsMmsMessage;
import com.ameerhamza6733.okAmeer.utial.TTSService;
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


import lolodev.permissionswrapper.callback.OnRequestPermissionsCallBack;
import lolodev.permissionswrapper.wrapper.PermissionWrapper;

public class SmsUnreadActivity extends AppCompatActivity implements INoNeedCommander,IGoogleSpeechRecognzerError {

    private Handler handler;
    private Runnable runnable;
    private Runnable mTextTOSpeechrunnable;
    private Handler mTextToSpeechHandler;

    private VoiceRecgonizationFragment newIntance;
    private String[] mUrduPositiveWords = {"جی ہاں","ہاں", "جی", "پڑھو","سناو" ,"سناؤ"};
    private String[] mRomanUrdoPositiveWords = {"ji haan","ha","yas",};
    private boolean userWantToReadorNot = false;
    private ArrayList<SmsMmsMessage> unread;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_unread);


        if (Build.VERSION.SDK_INT > 22) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                askRunTimePermissions();
                return;
            } else
                getUnreadSMSandUpdateUI();
        } else
            getUnreadSMSandUpdateUI();


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra("com.service.message");
                showVoiceRegoniztionFragment();
            }
        };

        FloatingActionButton febRetry = (FloatingActionButton) findViewById(R.id.febRetry);
        febRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVoiceRegoniztionFragment();
            }
        });
    }

    private void getUnreadSMSandUpdateUI() {
        unread = SMSUtils.getUnreadMessages(this);
        String text = "";
        if (unread == null || unread.size() < 1) {

            Toast.makeText(this, "no message to read ", Toast.LENGTH_SHORT).show();
        }
        Collections.reverse(unread);


        try {
            intiTextToSpeech("hi-IN", "आपको कई मैसेज आए हैं  क्या आप चाहते हैं इसे सुनना");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class HelpListAdupter extends RecyclerView.Adapter<HelpListVIewHolder>{

        @NonNull
        @Override
        public HelpListVIewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.help_commands_list_view, viewGroup, false);

            return new HelpListVIewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull HelpListVIewHolder helpListVIewHolder, int i) {

            helpListVIewHolder.mCommandTitle.setText(unread.get(helpListVIewHolder.getAdapterPosition()).getContactName());
            helpListVIewHolder.mCommandDescription.setText(unread.get(helpListVIewHolder.getAdapterPosition()).getMessageBody());
        }

        @Override
        public int getItemCount() {
            return unread.size();
        }
    }
    private class HelpListVIewHolder extends  RecyclerView.ViewHolder{
        private final TextView mCommandTitle;
        private final TextView mCommandDescription;
        public HelpListVIewHolder(@NonNull View itemView) {
            super(itemView);
            mCommandTitle=itemView.findViewById(R.id.tvCommandTitle);
            mCommandDescription=itemView.findViewById(R.id.tvCommandDescription);
        }

        public TextView getmCommandDescription() {
            return mCommandDescription;
        }

        public TextView getmCommandTitle() {
            return mCommandTitle;
        }
    }
    private void intiTextToSpeech(final String LEN, final String text) {
        mTextToSpeechHandler = new Handler();
        mTextTOSpeechrunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Intent i = new Intent(SmsUnreadActivity.this, TTSService.class);
                    i.putExtra("toSpeak", text);
                    i.putExtra("Language", LEN);
                    SmsUnreadActivity.this.startService(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mTextToSpeechHandler.postDelayed(mTextTOSpeechrunnable, 1000);

    }

    private void showVoiceRegoniztionFragment() {

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (!isFinishing()) {

                        FragmentTransaction transactionFragment = getSupportFragmentManager().beginTransaction();
                        newIntance = VoiceRecgonizationFragment.newInstance("ur-PK", false, false);
                        newIntance.setStyle(1, R.style.AppTheme);
                        transactionFragment.add(android.R.id.content, newIntance).addToBackStack(null).commitAllowingStateLoss();


                        //  newIntance.show(fragmentManager, "fragment_voice_input");

                    }


                } catch (Exception e) {
                    Toast.makeText(SmsUnreadActivity.this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        handler.postDelayed(runnable, 10);

    }

    @Override
    public void onNoCommandrExcute(String Queary) {
        voiceRecgonizerDismiss();
        for (String s : mUrduPositiveWords) {
            if (Queary.equalsIgnoreCase(s)) {
                userWantToReadorNot = true;
                break;
            } else {
                userWantToReadorNot = false;
            }
        }
        readOrNot(userWantToReadorNot);


    }


    @Override
    protected void onDestroy() {
        try {
            myTextToSpeech.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
                new IntentFilter("com.service.result"));
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    private void askRunTimePermissions() {
        new PermissionWrapper.Builder(this)
                .addPermissions(new String[]{Manifest.permission.READ_SMS})
                //enable rationale message with a custom message

                //show settings dialog,in this case with default message base on requested permission/s
                .addPermissionsGoSettings(true)
                //enable callback to know what option was choosed
                .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                    @Override
                    public void onGrant() {
                        Log.i(SendSmsActivity.class.getSimpleName(), "Permission was granted.");
                        getUnreadSMSandUpdateUI();


                    }

                    @Override
                    public void onDenied(String permission) {
                        Log.i(SendSmsActivity.class.getSimpleName(), "Permission was not granted.");
                    }
                }).build().request();
    }


    private void voiceRecgonizerDismiss() {
        try {
            if (newIntance != null)
                getSupportFragmentManager().beginTransaction().remove(newIntance).commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readOrNot(boolean userWantToReadorNot) {
        try {
            if (userWantToReadorNot)
                speckThisMessage();

            else
                moveToNextMessage();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    // called if user what read sms by assistant

    private void speckThisMessage() throws Exception {

        if (unread.size() > 1)
            intiTextToSpeech("en-IN", unread.get(0).getMessageBody() + "next message aaya hai" + unread.get(1).getContactName() + "se aaya hai aap sunna chahte hai ya ni");
        else
            intiTextToSpeech("en-IN", unread.get(0).getMessageBody());
        unread.remove(0);


    }

    private void moveToNextMessage() throws Exception {
        if (unread.size() > 0) {
            intiTextToSpeech("en-IN", "next message aaya hai" + unread.get(0).getContactName() + "se aaya hai aap sunna chahte hai ya ni");
            unread.remove(0);

        }
    }


    @Override
    public void onError(int Error) {
        voiceRecgonizerDismiss();
    }
}

