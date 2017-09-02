package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.UI.fragment.voiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.interfacess.noNeedCommander;
import com.ameerhamza6733.okAmeer.interfacess.onErrorSevenvoiceRecgoniztion;
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lolodev.permissionswrapper.callback.OnRequestPermissionsCallBack;
import lolodev.permissionswrapper.wrapper.PermissionWrapper;

public class sendSmsActivity extends AppCompatActivity implements noNeedCommander, onErrorSevenvoiceRecgoniztion {

    public static final String EXTRA_RECIPIENT_NAME = "EXTRA_RECIPIENT_NAME";
    public static final String[] positiveWords = {"ji haan", "ha", "yas", "bhej do", "send kar do", "kar do"};
    public static final String[] negativeWords = {"nahi send karna", "nahi", "no", "cancel kar do"};
    private static final int PICK_CONTACT_REQUEST = 14;
    private static final String[] changeITwords = {"phir se likho", "dubara se likho", "dubara  likho", "change kar do", "badal"};
    private static String EXTRA_SMS_OR_WHATS_APP;
    protected TextView mSedingSmsIn;
    private boolean FlagGetSmsBody = false;
    private ArrayList<String> senderNameList;
    private ArrayAdapter<String> adapter;
    private HashMap<String, String> mHashMapContacts = new HashMap<>();
    private CountDownTimer countDownTimer;
    private boolean isRecipientNumberFound = false;
    private boolean isNativeUrdu=false;


    private EditText mSmsBody;
    private Spinner callpickerSpinner;
    private ImageView smsOk;
    private ImageView smsCancle;
    private FloatingActionButton mFebRetry;
    private Switch mSwitchTurnOnNativeUrdu;
    private voiceRecgonizationFragment newIntance;
    private BroadcastReceiver broadcastReceiver;
    private TextToSpeech textToSpeech;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        EXTRA_SMS_OR_WHATS_APP = getIntent().getStringExtra("EXTRA_SMS_OR_WHATS_APP");

        setUPUI();

        mFebRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSwitchTurnOnNativeUrdu.getVisibility() ==View.VISIBLE){
                    SwitchBetweenLanguages(mSwitchTurnOnNativeUrdu.isChecked());
                }else
                showVoiceRegonizerDiloge("en-IN");
            }
        });
        smsOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(sendSmsActivity.this,"آپکا میسج بھیجا جا رہا ہے",Toast.LENGTH_SHORT).show();
                    sendSmsActivity.this.sendItNow(mHashMapContacts.get(sendSmsActivity.this.callpickerSpinner.getSelectedItem().toString()), mSmsBody.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        smsCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    sendSmsActivity.this.countDownTimer.cancel();

                }
                Toast.makeText(sendSmsActivity.this,"Message canceled",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        mSwitchTurnOnNativeUrdu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SwitchBetweenLanguages(isChecked);
            }

        });
        callpickerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equalsIgnoreCase("Contact"))

                    intiTextToSpeech("hi", getResources().getString(R.string.Message_kay_ha));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra("com.service.message");


            }
        };
        if (Build.VERSION.SDK_INT > 22) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                askRunTimePermissions();
                return;
            } else if (getIntent().getStringExtra(EXTRA_RECIPIENT_NAME) != null) {
                Log.d("extraname ", "name = " + getIntent().getStringExtra(EXTRA_RECIPIENT_NAME));
                new myContentNameFinder(getIntent().getStringExtra(EXTRA_RECIPIENT_NAME)).execute();
            } else {
                intiTextToSpeech("hi", getResources().getString(R.string.Kis_Ko_message_likha_na));
            }
        } else if (getIntent().getStringExtra(EXTRA_RECIPIENT_NAME) != null)
            new myContentNameFinder(getIntent().getStringExtra(EXTRA_RECIPIENT_NAME)).execute();
        else
            intiTextToSpeech("hi", getResources().getString(R.string.Kis_Ko_message_likha_na));


    }

    private void SwitchBetweenLanguages(boolean isChecked) {
        if (isChecked) {
            mSmsBody.setText("");
            showVoiceRegonizerDiloge("ur-PK");
            FlagGetSmsBody=true;
            isNativeUrdu=true;

        }else {
            mSmsBody.setText("");
            showVoiceRegonizerDiloge("en-IN");
            FlagGetSmsBody=true;
            isNativeUrdu=false;
        }
    }

    private void setUPUI() {
        callpickerSpinner = (Spinner) findViewById(R.id.caling_spinner);
        mSwitchTurnOnNativeUrdu = (Switch) findViewById(R.id.switch_turn_on_native_urdu);
        senderNameList = new ArrayList<>();
        senderNameList.add("Contact");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, senderNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        callpickerSpinner.setAdapter(adapter);
        mSedingSmsIn = (TextView) findViewById(R.id.making_call_in);
        mSmsBody = (EditText) findViewById(R.id.smsBodayEdittext);
        mFebRetry = (FloatingActionButton) findViewById(R.id.febRetry);
        smsOk = (ImageView) findViewById(R.id.caling_yas);
        smsCancle = (ImageView) findViewById(R.id.caling_cancle);

    }
    //show voice regonizer input dialog to use

    private void showVoiceRegonizerDiloge(final String LANGUAGE) {

        try {
            if (!isFinishing()) {
                Log.d("showVoiceRegoni()", "showing... ");
                FragmentTransaction transactionFragment = getSupportFragmentManager().beginTransaction();
                newIntance = voiceRecgonizationFragment.newInstance(LANGUAGE, false, false);
                newIntance.setStyle(1, R.style.AppTheme);

                transactionFragment.add(android.R.id.content, newIntance).addToBackStack(null).commitAllowingStateLoss();

            }


        } catch (Exception e) {
            Toast.makeText(sendSmsActivity.this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void askRunTimePermissions() {
        new PermissionWrapper.Builder(this)
                .addPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS})
                //enable rationale message with a custom message

                //show settings dialog,in this case with default message base on requested permission/s
                .addPermissionsGoSettings(true)
                //enable callback to know what option was choosed
                .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                    @Override
                    public void onGrant() {
                        Log.i(sendSmsActivity.class.getSimpleName(), "Permission was granted.");
                        if (getIntent().getStringExtra(EXTRA_RECIPIENT_NAME) != null)
                            new myContentNameFinder(getIntent().getStringExtra(EXTRA_RECIPIENT_NAME)).execute();

                        else {
                            intiTextToSpeech("hi", getResources().getString(R.string.Kis_Ko_message_likha_na));
                        }


                    }

                    @Override
                    public void onDenied(String permission) {
                        Log.i(sendSmsActivity.class.getSimpleName(), "Permission was not granted.");
                    }
                }).build().request();
    }


    /**
     * Called when the Sms read to send
     */

    protected void startSedingSmsCountDown(final String smsBody) {
        voiceRecgonizerDismiss();
        try {
            myTextToSpeech.intiTextToSpeech(sendSmsActivity.this, "hi", getResources().getString(R.string.Aap_ka_Message_Send_Kiya_ja_raha_ha));
        } catch (Exception e) {
            e.printStackTrace();
        }

        countDownTimer = new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                sendSmsActivity.this.mSedingSmsIn.setText("Seding sms in : " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                sendSmsActivity.this.mSedingSmsIn.setText("Done!");
                try {
                    if (EXTRA_SMS_OR_WHATS_APP.equals("sms"))
                        sendSmsActivity.this.sendItNow(mHashMapContacts.get(sendSmsActivity.this.callpickerSpinner.getSelectedItem().toString()), mSmsBody.getText().toString());
                    else
                        sendWhatsAppNow(mHashMapContacts.get(sendSmsActivity.this.callpickerSpinner.getSelectedItem().toString()), smsBody);
                } catch (Exception e) {

                }
            }


        }.start();
    }

    /**
     * this method used to  get Phone number and message body from user
     */

    @Override
    public void onNoCommandrExcute(String Queary) {
        Toast.makeText(this, Queary, Toast.LENGTH_LONG).show();
        if (!isRecipientNumberFound)
            new myContentNameFinder(Queary).execute();
        else if (FlagGetSmsBody) { // after geting sms body ask user to where he/she want to send this sms or not?
            voiceRecgonizerDismiss();
            FlagGetSmsBody = false;
            isNativeUrdu=false;

            mSwitchTurnOnNativeUrdu.setVisibility(View.INVISIBLE);
            intiTextToSpeech("hi", getResources().getString(R.string.send_kar_do_ya_badal_do));
            mSmsBody.setText(Queary);


        } else {
            voiceRecgonizerDismiss();
            for (String postiveWord : positiveWords)
                if (Queary.toLowerCase().contains(postiveWord.toLowerCase()))
                    startSedingSmsCountDown(mSmsBody.getText().toString());
            for (String negativeWord : negativeWords)
                if (Queary.toLowerCase().contains(negativeWord.toLowerCase()))
                    Toast.makeText(sendSmsActivity.this, "App ka message cancel kar dea gay ha ", Toast.LENGTH_SHORT).show();
            for (String changeTtWord : changeITwords)
                if (Queary.toLowerCase().contains(changeTtWord.toLowerCase())) {
                    FlagGetSmsBody = true;
                    mSwitchTurnOnNativeUrdu.setVisibility(View.VISIBLE);
                    intiTextToSpeech("hi", getResources().getString(R.string.Message_kay_ha));
                    mSmsBody.setText("");

                }

        }


    }

    private void intiTextToSpeech(final String LEN, final String text) {

        try {
            textToSpeech = myTextToSpeech.intiTextToSpeech(sendSmsActivity.this, LEN, text);
            textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {


                }

                @Override
                public void onDone(String utteranceId) {


                    if(mSwitchTurnOnNativeUrdu.getVisibility() == View.VISIBLE){
                        showVoiceRegonizerDiloge("ur-PK");
                    }else
                    showVoiceRegonizerDiloge("en-IN");




                }

                @Override
                public void onError(String utteranceId) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void voiceRecgonizerDismiss() {
        try {
            if (newIntance != null)
                getSupportFragmentManager().beginTransaction().remove(newIntance).commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);
                Log.d("callingActivty", "number from phone book=" + number);

                // Do something with the phone number...
            }
        }
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



    /**
     * called when count down(3 second) finish this mathod invoke by  startSedingSmsCountDown(...), it send only SMS
     */

    private void sendItNow(String senderNumber, String smsBody) throws Exception {

        Log.d("send sms", senderNumber + smsBody);

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        finish();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();

                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(senderNumber, null, smsBody, sentPI, deliveredPI);


    }

    /**
     * called when count down(3 second) finish this mathod invoke by  startSedingSmsCountDown(...), it send only whats app
     */
    private void sendWhatsAppNow(String number, String smsBody) throws Exception {


        Log.d("smsActivty", "sending whats app...to" + number + "and message =" + smsBody);
        Intent sendIntent = new Intent("android.intent.action.MAIN");
        //sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, smsBody);
        sendIntent.putExtra("jid", number + "@s.whatsapp.net"); //phone number without "+" prefix
        sendIntent.setPackage("com.whatsapp");
        startActivity(sendIntent);
    }

    @Override
    protected void onDestroy() {

        if (countDownTimer != null)
            countDownTimer.cancel();
        try {
            myTextToSpeech.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>1)
           getSupportFragmentManager(). popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        else
        super.onBackPressed();
        Log.d("send sms activty","onBackPressed");

    }

    /**
     * called when recognizer throw error 7
     */
    @Override
    public void onError7() {
        Toast.makeText(sendSmsActivity.this, "onError 7", Toast.LENGTH_SHORT).show();
        if (newIntance != null)
            getSupportFragmentManager().beginTransaction().remove(newIntance).commitAllowingStateLoss();
    }

    private class myContentNameFinder extends AsyncTask<Void, Void, Void> {
        long startTime;
        private String mTargetNumber;
        private List<String> mNameListFounded = new ArrayList<>();
        private Phonenumber.PhoneNumber formattedNumber;

        public myContentNameFinder(String queary) {
            mTargetNumber = queary;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startTime = System.nanoTime();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (EXTRA_SMS_OR_WHATS_APP.equals("sms"))
                    findRecipientNumberForSms();//find Recipient number for sms or else user want to send whats app
                else
                    findRecipientNumberForWhatsApp();
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        private void findRecipientNumberForWhatsApp() {
            ContentResolver cr = getContentResolver();

//RowContacts for filter Account Types
            Cursor contactCursor = cr.query(
                    ContactsContract.RawContacts.CONTENT_URI,
                    new String[]{ContactsContract.RawContacts._ID,
                            ContactsContract.RawContacts.CONTACT_ID},
                    ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                    new String[]{"com.whatsapp"},
                    null);

//ArrayList for Store Whatsapp Contact


            if (contactCursor != null) {
                if (contactCursor.getCount() > 0) {
                    if (contactCursor.moveToFirst()) {
                        do {
                            //whatsappContactId for get Number,Name,Id ect... from  ContactsContract.CommonDataKinds.Phone
                            String whatsappContactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));

                            if (whatsappContactId != null) {
                                //Get Data from ContactsContract.CommonDataKinds.Phone of Specific CONTACT_ID
                                Cursor whatsAppContactCursor = cr.query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                                ContactsContract.CommonDataKinds.Phone.NUMBER,
                                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                        new String[]{whatsappContactId}, null);

                                if (whatsAppContactCursor != null) {
                                    whatsAppContactCursor.moveToFirst();
                                    String id = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                                    String name = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                    String number = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                    whatsAppContactCursor.close();

                                    Log.d("whatsapp name", "name" + name);
                                    if (mTargetNumber.toLowerCase().contains(name.toLowerCase()) || name.toLowerCase().contains(mTargetNumber.toLowerCase())) {
                                        mNameListFounded.add(name);
                                        isRecipientNumberFound = true;
                                    }


                                    try {
                                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                                        Phonenumber.PhoneNumber NumberProto = phoneUtil.parse(number, tm.getSimCountryIso().toUpperCase());
                                        Log.d("phone number", "" + phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
                                        number = phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.E164);
                                        number = number.replaceAll("[-+.^:,]", "");
                                    } catch (Exception e) {
                                        System.err.println("NumberParseException was thrown: " + e.toString());
                                    }
                                    //Add Number to ArrayList


                                    mHashMapContacts.put(name.toLowerCase(), number);


                                }
                            }
                        } while (contactCursor.moveToNext());
                        contactCursor.close();
                    }
                }
            }
        }

        private void findRecipientNumberForSms() {

            Log.d("callingActivty", "requiredName=" + mTargetNumber);
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            Log.d("callingActivty", "requiredName=" + cur.getCount());
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    Log.d("callingActivty", "Name from phone Book " + name);
                    if (mTargetNumber.toLowerCase().contains(name.toLowerCase()) || name.toLowerCase().contains(mTargetNumber.toLowerCase())) {
                        Log.d("callingActivty", "requiredName found" + name);
                        mNameListFounded.add(name);
                        isRecipientNumberFound = true;

                        if (cur.getInt(cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                String phoneNo = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                                mHashMapContacts.put(name, phoneNo);// put all  contacts in hash map so latter we just give founded name from mNameListFounded and get number


                            }
                            pCur.close();
                        }
                    }

                }
            }
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                long stopTime = System.nanoTime();
                Log.d("Total time take to  ", String.valueOf(stopTime - startTime));
                voiceRecgonizerDismiss();
                if (isRecipientNumberFound) {
                    voiceRecgonizerDismiss();
                    mSwitchTurnOnNativeUrdu.setVisibility(View.VISIBLE);
                    updateSpinner();// update the UI and notify to use about Recipients number found
                    if (!(mNameListFounded.size() > 1))
                        intiTextToSpeech("hi", getResources().getString(R.string.Message_kay_ha));


                } else {
                    if (newIntance == null)
                        intiTextToSpeech("hi", getResources().getString(R.string.Kis_Ko_message_likha_na));
                    else
                        intiTextToSpeech("hi", getResources().getString(R.string.Sorry_App_Kiss_Ko_Sms_Send_Karna_Chaatay_ha));


                }


            } catch (Exception s) {
                // Toast.makeText(CallingActivity.this, "Exception:" + s.getMessage(), Toast.LENGTH_LONG).show();

            }

        }

        private void updateSpinner() {
            FlagGetSmsBody = true;
            if (mNameListFounded.size() == 1) {


                senderNameList.set(0, mNameListFounded.get(0));
                adapter.notifyDataSetChanged();


            } else if (mNameListFounded.size() > 1) {
                senderNameList.addAll(mNameListFounded);
                adapter.notifyDataSetChanged();
                callpickerSpinner.performClick();
            }
        }
    }


}


