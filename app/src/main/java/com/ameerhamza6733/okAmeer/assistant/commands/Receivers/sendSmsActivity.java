package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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


import pub.devrel.easypermissions.EasyPermissions;

public class sendSmsActivity extends AppCompatActivity implements noNeedCommander, onErrorSevenvoiceRecgoniztion {

    private static final int PICK_CONTACT_REQUEST = 14;
    private static String EXTRA_SMS_OR_WHATS_APP;


    private boolean getSmsBody = false;
    private ArrayList<String> senderNameList;
    private ArrayAdapter<String> adapter;
    private HashMap<String, String> mHashMapContacts = new HashMap<>();
    private CountDownTimer countDownTimer;
    private boolean isRecipientNumberFound = false;

    protected TextView mSedingSmsIn;
    private EditText mSmsBody;
    private Spinner callpickerSpinner;
    private ImageView smsOk;
    private ImageView smsCancle;
    private FloatingActionButton mFebRetry;
    private voiceRecgonizationFragment newIntance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
                     EXTRA_SMS_OR_WHATS_APP= getIntent().getStringExtra("EXTRA_SMS_OR_WHATS_APP");
        showVoiceRegonizerDiloge("en-IN");
        callpickerSpinner = (Spinner) findViewById(R.id.caling_spinner);
        senderNameList = new ArrayList<>();
        senderNameList.add("contact");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, senderNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        callpickerSpinner.setAdapter(adapter);

        mSedingSmsIn = (TextView) findViewById(R.id.making_call_in);
        mSmsBody = (EditText) findViewById(R.id.smsBodayEdittext);
        mFebRetry = (FloatingActionButton) findViewById(R.id.febRetry);
        mSmsBody.setMaxLines(Integer.MAX_VALUE);
        smsOk = (ImageView) findViewById(R.id.caling_yas);

        ActivityCompat.requestPermissions(sendSmsActivity.this,
                new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.SEND_SMS},
                1);

        mFebRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVoiceRegonizerDiloge("en-IN");
            }
        });
        smsOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        try {
                    sendSmsActivity.this.sendItNow(mHashMapContacts.get(sendSmsActivity.this.callpickerSpinner.getSelectedItem().toString()), mSmsBody.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        smsCancle = (ImageView) findViewById(R.id.caling_cancle);
        smsCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(countDownTimer!=null){
                   sendSmsActivity.this.countDownTimer.cancel();
                   sendSmsActivity.this.mSedingSmsIn.setText("Message canceled");
               }
            }
        });
        callpickerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals("contact"))
                    askSms();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    //show voice regonizer input dialog to use

    private void showVoiceRegonizerDiloge(final String LANGUAGE) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    newIntance = voiceRecgonizationFragment.newInstance(LANGUAGE, false,false);
                    newIntance.show(fragmentManager, "sendSmsActivity");
                    newIntance.setStyle(1, R.style.AppTheme);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, 1000);
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
        mSmsBody.setText(smsBody);
        countDownTimer = new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                sendSmsActivity.this.mSedingSmsIn.setText("Seding sms in : " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                sendSmsActivity.this.mSedingSmsIn.setText("done!");
               try {
                   if(EXTRA_SMS_OR_WHATS_APP.equals("sms"))
                       sendSmsActivity.this.sendItNow(mHashMapContacts.get(sendSmsActivity.this.callpickerSpinner.getSelectedItem().toString()), mSmsBody.getText().toString());
                   else
                       sendWhatsAppNow(mHashMapContacts.get(sendSmsActivity.this.callpickerSpinner.getSelectedItem().toString()),smsBody);
               }catch (Exception e){

               }
            }


        }.start();
    }
    /**
     * this method used to  get Phone number and message body from user
     */

    @Override
    public void onNoCommandrExcute(String Queary) {
        Toast.makeText(this,Queary,Toast.LENGTH_LONG).show();
        if (Queary.toLowerCase().equals("nahi karna message")) {
            Toast.makeText(sendSmsActivity.this, "App ka message cancel kar dea gay ha ", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!isRecipientNumberFound)
            new myContentNameFinder(Queary).execute();
        else if (getSmsBody) {
            startSedingSmsCountDown(Queary);
            voiceRecgonizerDismiss();

        }


    }

    private void voiceRecgonizerDismiss() {
        try {
            newIntance.dismiss();
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

    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Read contants and send sms permissios need ",

                   1 , perms);
        }
    }
    /**
     * called when count down(3 second) finish this mathod invoke by  startSedingSmsCountDown(...), it send only SMS
     */

    private void sendItNow(String senderNumber, String smsBody) throws Exception {

        Log.d("send sms",senderNumber+smsBody);

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
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
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
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
    private void sendWhatsAppNow (String number, String smsBody)throws  Exception{


        Log.d("smsActivty","sending whats app...to"+number+"and message ="+smsBody);
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

    /**
     * called when recognizer throw error 7
     */
    @Override
    public void onError7() {
        Toast.makeText(sendSmsActivity.this, "onError 7", Toast.LENGTH_SHORT).show();
    }

    private class myContentNameFinder extends AsyncTask<Void, Void, Void> {
        private String mTargetNumber;


        private List<String> mNameListFounded = new ArrayList<>();
        private Phonenumber.PhoneNumber formattedNumber;


        public myContentNameFinder(String queary) {
            mTargetNumber = queary;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if(EXTRA_SMS_OR_WHATS_APP .equals("sms"))
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

                                    Log.d("whatsapp name","name"+name);
                                    if (mTargetNumber.toLowerCase().contains(name.toLowerCase()) || name.toLowerCase().contains(mTargetNumber.toLowerCase())) {
                                        mNameListFounded.add(name);
                                        isRecipientNumberFound = true;
                                    }


                                    try {
                                        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                                        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                                        Phonenumber.PhoneNumber NumberProto = phoneUtil.parse(number, tm.getSimCountryIso().toUpperCase());
                                        Log.d("phone number",""+phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
                                        number = phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.E164);
                                       number= number.replaceAll("[-+.^:,]","");
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
                    }
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


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                voiceRecgonizerDismiss();
                if (isRecipientNumberFound) {
                    voiceRecgonizerDismiss();
                    updateSpinner();// update the UI and notify to use about Recipients number found
                    myTextToSpeech.intiTextToSpeech(sendSmsActivity.this, "hi", getResources().getString(R.string.Aur_Message_kay_ha));


                } else {

                    myTextToSpeech.intiTextToSpeech(sendSmsActivity.this, "hi", getResources().getString(R.string.Sorry_App_Kiss_Ko_Sms_Send_Karna_Chaatay_ha));
                    showVoiceRegonizerDiloge("en-IN");

                }


            } catch (Exception s) {
                // Toast.makeText(CallingActivity.this, "Exception:" + s.getMessage(), Toast.LENGTH_LONG).show();

            }

        }

        private void updateSpinner() {
            getSmsBody = true;
            if (mNameListFounded.size() == 1) {


                senderNameList.set(0, mNameListFounded.get(0));
                adapter.notifyDataSetChanged();
                askSms();


            } else if (mNameListFounded.size() > 1) {
                senderNameList.addAll(mNameListFounded);
                adapter.notifyDataSetChanged();
                callpickerSpinner.performClick();
            }
        }
    }

    private void askSms() {
        showVoiceRegonizerDiloge("en-IN");
    }
}


