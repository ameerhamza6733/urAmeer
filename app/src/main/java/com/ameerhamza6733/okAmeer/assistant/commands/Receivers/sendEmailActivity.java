package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.fragment.voiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.interfacess.noNeedCommander;
import com.ameerhamza6733.okAmeer.utial.TTSService;

import java.util.ArrayList;
import java.util.HashMap;

import de.cketti.mailto.EmailIntentBuilder;

public class sendEmailActivity extends AppCompatActivity implements noNeedCommander {

    private BroadcastReceiver broadcastReceiver;
    private Handler handler;
    private Runnable runnable;
    private voiceRecgonizationFragment newIntance;
    private Spinner mSpinner;
    private EditText mSmsBody;
    private ArrayList<String> mSpinnerList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private boolean RispitionNumberFoundFlag=false;
    HashMap<String, String> RecsHM = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        Intent intent = getIntent();
        String IntentExtra = intent.getStringExtra("EmailExtra");
        textToSpeak(getResources().getString(R.string.App_kiss_ko_Email_send_karna_chaaty_ha));
        mSpinner = (Spinner) findViewById(R.id.caling_spinner);

        mSpinnerList.add("contact");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mSpinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra("com.service.message");

                showVoiceRegoniztionFragment("en-IN", false, false);

            }
        };


    }

    private void showVoiceRegoniztionFragment(final String LEN, final boolean isTranlaterNeeded, final boolean isCommanderNeeded) {

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    newIntance = voiceRecgonizationFragment.newInstance(LEN, isTranlaterNeeded, isCommanderNeeded);
                    newIntance.show(fragmentManager, "smsUnreadActivty");
                    newIntance.setStyle(1, R.style.Theme_AppCompat_Dialog_MinWidth);
                } catch (Exception e) {

                }
            }
        };
        handler.postDelayed(runnable, 10);

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

    @Override
    public void onNoCommandrExcute(String Queary) {

        newIntance.dismiss();
        if(!this.RispitionNumberFoundFlag)
        new findRecipientsNumber(this, Queary).execute();
        else
        {
            textToSpeak(getResources().getString(R.string.Aap_ki_Email_send_ki_ja_Rahi_ha));
            EmailIntentBuilder.from(this)
                    .to(RecsHM.get(mSpinner.getSelectedItem().toString()))
                    .body(Queary)
                    .start();
        }

    }

    private class findRecipientsNumber extends AsyncTask<Object, Object, ArrayList<String>> {

        private Context context;
        private String mRecipientsName;

        public findRecipientsNumber(Context context, String mRescipientsNumber) {
            this.context = context;
            this.mRecipientsName = mRescipientsNumber;
        }

        @Override
        protected ArrayList<String> doInBackground(Object... params) {
            ArrayList<String> RecsNumbersList = new ArrayList<String>();


            ContentResolver cr = context.getContentResolver();
            String[] PROJECTION = new String[]{ContactsContract.RawContacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.PHOTO_ID,
                    ContactsContract.CommonDataKinds.Email.DATA,
                    ContactsContract.CommonDataKinds.Photo.CONTACT_ID};
            String order = "CASE WHEN "
                    + ContactsContract.Contacts.DISPLAY_NAME
                    + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
                    + ContactsContract.Contacts.DISPLAY_NAME
                    + ", "
                    + ContactsContract.CommonDataKinds.Email.DATA
                    + " COLLATE NOCASE";
            String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
            Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);
            if (cur.moveToFirst()) {
                do {
                    // names comes in hand sometimes
                    String name = cur.getString(1);
                    String emlAddr = cur.getString(3);


                    if (name.toLowerCase().contains(mRecipientsName.toLowerCase()) || mRecipientsName.toLowerCase().contains(name.toLowerCase())) {
                        RecsNumbersList.add(name);
                        sendEmailActivity.this.RispitionNumberFoundFlag=true;
                        Log.d("send email ", "name founded");
                    }
                    RecsHM.put(name.toLowerCase(), emlAddr);

                } while (cur.moveToNext());
            }

            cur.close();
            return RecsNumbersList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            updateSpinner(strings);
        }

        private void updateSpinner(ArrayList<String> strings) {
            if (strings.size() == 1) {
                mSpinnerList.set(0, strings.get(0));
                adapter.notifyDataSetChanged();
                textToSpeak(getResources().getString(R.string.Aur_Email_kaya_ha));

            } else if (strings.size() > 1) {
                mSpinnerList.addAll(strings);
                adapter.notifyDataSetChanged();
                mSpinner.performClick();

            }else {

                textToSpeak(getResources().getString(R.string.Maff_keejie_mija_is_naam_say_koee_email_nahie_mila));
            }
        }
    }

    private void textToSpeak(String textToSpeak) {
        Intent i = new Intent(sendEmailActivity.this, TTSService.class);
        i.putExtra("toSpeak", textToSpeak);
        i.putExtra("Language", "hi");
        sendEmailActivity.this.startService(i);
    }

}
