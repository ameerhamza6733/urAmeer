package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.UI.fragment.voiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.interfacess.noNeedCommander;
import com.ameerhamza6733.okAmeer.utial.TTSService;

import java.util.ArrayList;
import java.util.HashMap;

import de.cketti.mailto.EmailIntentBuilder;
import lolodev.permissionswrapper.callback.OnRequestPermissionsCallBack;
import lolodev.permissionswrapper.wrapper.PermissionWrapper;

public class sendEmailActivity extends AppCompatActivity implements noNeedCommander {

    private BroadcastReceiver broadcastReceiver;
    private Handler handler;
    private Runnable runnable;
    private voiceRecgonizationFragment newIntance;
    private Spinner mSpinner;
    private EditText mSmsBody;
    private ArrayList<String> mSpinnerList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private boolean RispitionNumberFoundFlag = false;
    HashMap<String, String> RecsHM = new HashMap<>();
    private CountDownTimer countDownTimer;
    private TextView sendEmailInTextView;
    private EditText mEmailBodytextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        Intent intent = getIntent();
        String IntentExtra = intent.getStringExtra("EmailExtra");
        textToSpeak(getResources().getString(R.string.App_kiss_ko_Email_send_karna_chaaty_ha));

        mSpinner = (Spinner) findViewById(R.id.caling_spinner);
        ImageButton sendEmailITOkButton = (ImageButton) findViewById(R.id.caling_yas);
        ImageButton cancleButton = (ImageButton) findViewById(R.id.caling_cancle);
        sendEmailInTextView = (TextView) findViewById(R.id.making_call_in);
        FloatingActionButton mRetry = (FloatingActionButton) findViewById(R.id.febRetry);
         mEmailBodytextView = (EditText) findViewById(R.id.smsBodayEdittext);

        mSpinnerList.add("contact");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mSpinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    sendEmailInTextView.setText("Email canceled");
                }
            }
        });

        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVoiceRegoniztionFragment("en-IN",false,false);
            }
        });


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra("com.service.message");

                if(!s.contains(getString(R.string.Aap_ki_Email_send_ki_ja_Rahi_ha))){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (ActivityCompat.checkSelfPermission(sendEmailActivity.this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ) {
                            askRunTimePermissions();
                            return;
                        }else
                            showVoiceRegoniztionFragment("en-IN", false, false);
                    } else
                        showVoiceRegoniztionFragment("en-IN", false, false);
                }



            }
        };


    }

        private void askRunTimePermissions() {
            new PermissionWrapper.Builder(this)
                    .addPermissions(new String[]{ Manifest.permission.READ_CONTACTS })
                    //enable rationale message with a custom message

                    //show settings dialog,in this case with default message base on requested permission/s
                    .addPermissionsGoSettings(true)
                    //enable callback to know what option was choosed
                    .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                        @Override
                        public void onGrant() {
                            Log.i(sendSmsActivity.class.getSimpleName(), "Permission was granted.");
                            showVoiceRegoniztionFragment("en-IN", false, false);


                        }

                        @Override
                        public void onDenied(String permission) {
                            Log.i(sendSmsActivity.class.getSimpleName(), "Permission was not granted.");
                        }
                    }).build().request();
        }





    private void showVoiceRegoniztionFragment(final String LEN, final boolean isTranlaterNeeded, final boolean isCommanderNeeded) {

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if(!isFinishing()){
                        FragmentTransaction transactionFragment = getSupportFragmentManager().beginTransaction();
                        newIntance = voiceRecgonizationFragment.newInstance(LEN, isTranlaterNeeded, isCommanderNeeded);
                        newIntance.setStyle(1, R.style.AppTheme);
                        transactionFragment.add(android.R.id.content, newIntance).addToBackStack(null).commitAllowingStateLoss();


                        //  newIntance.show(fragmentManager, "fragment_voice_input");

                    }


                }catch (Exception e){
                    Toast.makeText(sendEmailActivity.this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
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

        Toast.makeText(this,Queary,Toast.LENGTH_LONG).show();
        if(newIntance!=null)
            getSupportFragmentManager().beginTransaction().remove(newIntance).commitAllowingStateLoss();
        if (!this.RispitionNumberFoundFlag)
            new findRecipientsNumber(this, Queary).execute();
        else {
            mEmailBodytextView.setText(Queary);
            textToSpeak(getResources().getString(R.string.Aap_ki_Email_send_ki_ja_Rahi_ha));
            sendEmailCountDown(Queary);
        }

    }

    private void sendEmailCountDown(final String Queary) {

        countDownTimer = new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                sendEmailActivity.this.sendEmailInTextView.setText("Seding Email in : " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                sendEmailActivity.this.sendEmailInTextView.setText("Done");
                EmailIntentBuilder.from(sendEmailActivity.this)
                        .to(RecsHM.get(mSpinner.getSelectedItem().toString()))
                        .body(Queary)
                        .start();
            }
        }.start();
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
                        sendEmailActivity.this.RispitionNumberFoundFlag = true;
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
            if(strings.size()>=1)
            UpdateUI(strings);
            else
                textToSpeak(getResources().getString(R.string.Maff_keejie_mija_is_naam_say_koee_email_nahie_mila));
        }

        private void UpdateUI(ArrayList<String> strings) {
            if (strings.size() == 1) {
                mSpinnerList.set(0, strings.get(0));
                adapter.notifyDataSetChanged();
                textToSpeak(getResources().getString(R.string.Aur_Email_kaya_ha));

            } else if (strings.size() > 1) {
                mSpinnerList.addAll(strings);
                adapter.notifyDataSetChanged();
                mSpinner.performClick();

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
