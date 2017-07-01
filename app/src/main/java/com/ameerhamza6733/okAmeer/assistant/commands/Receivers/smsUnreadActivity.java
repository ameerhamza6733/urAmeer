package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.fragment.voiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.interfacess.mttsListener;
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

public class smsUnreadActivity extends AppCompatActivity implements noNeedCommander,mttsListener {

    private ContentResolver resolver;
    final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_unread);

        cards = new ArrayList<Card>();


        unread = SMSUtils.getUnreadMessages(this);
        String text = "";
        if (unread == null || unread.size() < 1) {

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

        showVoiceRegoniztionFragment();


    }

    private void intiTextToSpeech(final String LEN, final String text) {
        mTextToSpeechHandler = new Handler();
        mTextTOSpeechrunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Intent i = new Intent(smsUnreadActivity.this, TTSService.class);
                    i.putExtra("toSpeak", "आपको कई मैसेज आए हैं  क्या आप चाहते हैं इसे सुनना");
                    i.putExtra("Language","hi-IN");
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
                       newIntance.setStyle(1, R.style.Theme_AppCompat_Dialog);
                   }catch (Exception e){

                   }
                }
            };
            handler.postDelayed(runnable, 4000);

    }

    @Override
    public void onNoCommandrExcute(String Queary) {
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

    private void readOrNot(boolean userWantToReadorNot) {
        if (userWantToReadorNot) {
            Intent i = new Intent(this, TTSService.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("toSpeak", unread.get(0).getMessageBody());

            i.putExtra("Language","en-IN");
            this.startService(i);
            Log.d("smsUnreadActivty", "reading sms ..." + unread.get(0).getMessageBody());
            unread.remove(0);
            cards.get(0).setBackgroundColorResourceId(Color.BLUE);
            mCardArrayAdapter.notifyDataSetChanged();
        }
      else {
            Log.d("smsUnreadActivty","Removeing sms ..."+ unread.get(0).getMessageBody());
            if(unread.size()>0){
                Intent i = new Intent(this, TTSService.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("toSpeak", "next message aaya hai"+unread.get(0).getContactName()+"se");
                i.putExtra("Language","en-IN");
                this.startService(i);
                unread.remove(0);
                cards.remove(0);
                showVoiceRegoniztionFragment();

            }
        }



    }

    @Override
    public void onFinsh() {

    }
}

