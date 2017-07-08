package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.fragment.voiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.interfacess.noNeedCommander;
import com.ameerhamza6733.okAmeer.utial.SMSUtils;
import com.ameerhamza6733.okAmeer.utial.SmsMmsMessage;
import com.ameerhamza6733.okAmeer.utial.TTSService;
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;

public class smsUnreadActivity extends AppCompatActivity implements noNeedCommander {

    private Handler handler;
    private Runnable runnable;
    private Runnable mTextTOSpeechrunnable;
    private Handler mTextToSpeechHandler;

    private voiceRecgonizationFragment newIntance;
    private String[] mUrduPositiveWords = {"ہاں", "جی", "پڑھو", "سناؤ"};
    private boolean userWantToReadorNot = false;
    private ArrayList<SmsMmsMessage> unread;
    private ArrayList<Card> cards;
    private CardListView listView;
    private CardArrayAdapter mCardArrayAdapter;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_unread);

        ActivityCompat.requestPermissions(smsUnreadActivity.this,
                new String[]{Manifest.permission.READ_SMS},
                111);
        cards = new ArrayList<Card>();


        unread = SMSUtils.getUnreadMessages(this);
        String text = "";
        if (unread == null || unread.size() < 1) {

            Toast.makeText(this,"no message to read ",Toast.LENGTH_SHORT).show();
        } else {
            Collections.reverse(unread);
            for (SmsMmsMessage message : unread) {
                Card card = new Card(this);
                CardHeader header = new CardHeader(this);
                header.setTitle(message.getContactName());
                card.addCardHeader(header);
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                String dateString = formatter.format(new Date(Long.parseLong(String.valueOf(message.getTimestamp()))));
                card.setTitle(message.getMessageBody() + " time : " + dateString);
                cards.add(card);

            }


            mCardArrayAdapter = new CardArrayAdapter(this, cards);

            listView = (CardListView) findViewById(R.id.myList);
            if (listView != null) {
                listView.setAdapter(mCardArrayAdapter);
            }



            try {
                intiTextToSpeech("hi-IN", "आपको कई मैसेज आए हैं  क्या आप चाहते हैं इसे सुनना");
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


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


    private void intiTextToSpeech(final String LEN, final String text) {
        mTextToSpeechHandler = new Handler();
        mTextTOSpeechrunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Intent i = new Intent(smsUnreadActivity.this, TTSService.class);
                    i.putExtra("toSpeak", text);
                    i.putExtra("Language", LEN);
                    smsUnreadActivity.this.startService(i);
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
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    newIntance = voiceRecgonizationFragment.newInstance("hi", true, false);
                    newIntance.show(fragmentManager, "smsUnreadActivty");
                    newIntance.setStyle(1, R.style.Theme_AppCompat_Dialog_MinWidth);
                } catch (Exception e) {

                }
            }
        };
        handler.postDelayed(runnable, 10);

    }

    @Override
    public void onNoCommandrExcute(String Queary) {
        voiceRecgonizerDismiss();
        for (String s : mUrduPositiveWords) {
            if (Queary.contains(s)) {
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
    private void voiceRecgonizerDismiss() {
        try {
            newIntance.dismiss();
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


}

