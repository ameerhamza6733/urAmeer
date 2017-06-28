package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.ameerhamza6733.okAmeer.fragment.voiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.interfacess.NonHindiQurary;
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class smsActivity extends AppCompatActivity implements NonHindiQurary {

    private static final int PICK_CONTACT_REQUEST = 14;
    private voiceRecgonizationFragment newIntance;
    private Spinner callpickerSpinner;
    private ImageView smsOk;
    private ImageView smsCancle;
    private boolean getSmsBody = false;
    private ArrayList<String> senderNameList;
    private ArrayAdapter<String> adapter;
    private HashMap<String, String> mHashMapContacts = new HashMap<>();
    private CountDownTimer countDownTimer;
    protected TextView mSedingSmsIn;
    private  boolean isSenderNameFound=false;
    private EditText mSmsBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        showVoiceRegonizerDiloge("en-hi");
        callpickerSpinner = (Spinner) findViewById(R.id.caling_spinner);
        senderNameList = new ArrayList<>();
        senderNameList.add("contact");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, senderNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        callpickerSpinner.setAdapter(adapter);

        mSedingSmsIn = (TextView) findViewById(R.id.making_call_in);
        mSmsBody= (EditText) findViewById(R.id.smsBodayEdittext);
        smsOk = (ImageView) findViewById(R.id.caling_yas);
        smsOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(smsActivity.this, "item" + callpickerSpinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                //  makeCallNow(callpickerSpinner.getSelectedItem().toString());
                smsActivity.this.sendItNow(smsActivity.this.callpickerSpinner.getSelectedItem().toString(),mSmsBody.getText().toString());

            }
        });
        smsCancle = (ImageView) findViewById(R.id.caling_cancle);
        smsCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsActivity.this.countDownTimer.cancel();
                smsActivity.this.mSedingSmsIn.setText("Call canceled");
            }
        });
        callpickerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if( !parent.getItemAtPosition(position).toString().equals("contact"))
                    askSms();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void showVoiceRegonizerDiloge(final String LANGUAGE) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    newIntance = voiceRecgonizationFragment.newInstance(LANGUAGE, false);
                    newIntance.show(fragmentManager, "smsActivity");
                    newIntance.setStyle(1, R.style.AppTheme);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, 1000);
    }

    protected void startSendingSms(final String smsBody) {
        voiceRecgonizerDismiss();
        myTextToSpeech.intiTextToSpeech(smsActivity.this, "hi", getResources().getString(R.string.Aap_ka_Message_Send_Kiya_ja_raha_ha));
        mSmsBody.setText(smsBody);
        countDownTimer = new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                smsActivity.this. mSedingSmsIn.setText("Seding sms in : " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                smsActivity.this. mSedingSmsIn.setText("done!");
                sendItNow(smsActivity.this.callpickerSpinner.getSelectedItem().toString(),smsBody);
            }



        }.start();
    }


    @Override
    public void onNonHindiQuraryRecived(String Queary) {
        if(Queary.toLowerCase().equals("nahi karna message"))
        {
            Toast.makeText(smsActivity.this,"App ka message cancel kar dea gay ha ",Toast.LENGTH_SHORT).show();
            finish();
        }
        if(!isSenderNameFound)
        new myContentNameFinder(Queary).execute();
        else if(getSmsBody){
            startSendingSms(Queary);

        }




    }

    private void voiceRecgonizerDismiss() {
       try {
           newIntance.dismiss();
       }catch (Exception e){
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

    private void sendItNow(String senderNumber, String smsBody) {
        try {
            Uri uri = Uri.parse("smsto:"+senderNumber);
            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
            it.putExtra("sms_body", smsBody);
            startActivity(it);
        } catch (SecurityException SE) {
            Toast.makeText(this, "SecurityException" + SE.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onDestroy() {

        if (countDownTimer != null)
            countDownTimer.cancel();
        myTextToSpeech.stop();
        super.onDestroy();

    }

    private class myContentNameFinder extends AsyncTask<Void, Void, Void> {
        private String Queary;


        private List<String> mNameListFounded = new ArrayList<>();


        public myContentNameFinder(String queary) {
            Queary = queary;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Log.d("callingActivty", "requiredName=" + Queary);
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
                        if (Queary.toLowerCase().contains(name.toLowerCase()) || name.toLowerCase().contains(Queary.toLowerCase())) {
                            Log.d("callingActivty", "requiredName found" + name);
                            mNameListFounded.add(name);
                            isSenderNameFound = true;
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
                                mHashMapContacts.put(name, phoneNo);


                            }
                            pCur.close();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                voiceRecgonizerDismiss();
                if (isSenderNameFound){
                    updateSpinner();
                    myTextToSpeech.intiTextToSpeech(smsActivity.this,"hi",getResources().getString(R.string.Aur_Message_kay_ha));


                }


                else{
                    myTextToSpeech.intiTextToSpeech(smsActivity.this,"hi",getResources().getString(R.string.Sorry_App_Kiss_Ko_Sms_Send_Karna_Chaatay_ha));
                    showVoiceRegonizerDiloge("en-hi");

                }


            } catch (Exception s) {
                // Toast.makeText(CallingActivity.this, "Exception:" + s.getMessage(), Toast.LENGTH_LONG).show();

            }

        }

        private void updateSpinner() {
            getSmsBody=true;
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
        showVoiceRegonizerDiloge("en-hi");
    }
}


