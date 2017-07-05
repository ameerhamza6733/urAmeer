package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.fragment.voiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.interfacess.noNeedCommander;
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CallingActivity extends AppCompatActivity implements noNeedCommander {

    private static final int PICK_CONTACT_REQUEST = 1;
    private voiceRecgonizationFragment newIntance;
    protected Spinner callpickerSpinner;
    private ImageView CallOk;
    private ImageView CallCancle;
    private boolean blockContectInternt = false;
    private ArrayList<String> spinnerList;
    private ArrayAdapter<String> adapter;
    private HashMap<String, String> mHashMapContacts = new HashMap<>();
    private CountDownTimer countDownTimer;
    private TextView mMakingCallingIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    newIntance = voiceRecgonizationFragment.newInstance("en-IN", false,false);
                    newIntance.show(fragmentManager, "CallingActivity");
                    newIntance.setStyle(1, R.style.AppTheme);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, 1000);
        callpickerSpinner = (Spinner) findViewById(R.id.caling_spinner);
        spinnerList = new ArrayList<>();
        spinnerList.add("contact");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        callpickerSpinner.setAdapter(adapter);

        mMakingCallingIn = (TextView) findViewById(R.id.making_call_in);
        CallOk = (ImageView) findViewById(R.id.caling_yas);
        CallOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CallingActivity.this, "item" + callpickerSpinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                //  makeCallNow(callpickerSpinner.getSelectedItem().toString());
                CallingActivity.this.makeCallNow(CallingActivity.this.callpickerSpinner.getSelectedItem().toString());

            }
        });
        CallCancle = (ImageView) findViewById(R.id.caling_cancle);
        CallCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallingActivity.this.countDownTimer.cancel();
                CallingActivity.this.mMakingCallingIn.setText("Call canceled");
            }
        });


    }

    protected void startMakingCalling() throws Exception {
        myTextToSpeech.intiTextToSpeech(CallingActivity.this, "hi", "आपकी कॉल की जा रही है");
        countDownTimer = new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
               CallingActivity.this. mMakingCallingIn.setText("Making call in : " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                CallingActivity.this. mMakingCallingIn.setText("done!");
                makeCallNow(CallingActivity.this.callpickerSpinner.getSelectedItem().toString());
            }



        }.start();
    }


    @Override
    public void onNoCommandrExcute(String Queary) {
        new myContentNameFinder(Queary).execute();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                blockContectInternt = false;
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

    private void makeCallNow(String s) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mHashMapContacts.get(s)));
            Log.d("callingActivity", "trying to make call at " + mHashMapContacts.get(s));
            CallingActivity.this.startActivity(intent);
        } catch (SecurityException SE) {
            Toast.makeText(this, "SecurityException" + SE.getMessage(), Toast.LENGTH_LONG).show();
        }


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

    private class myContentNameFinder extends AsyncTask<Void, Void, Void> {
        private String Queary;
        boolean isFound = false;

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
                        if (Queary.toLowerCase().contains(name.toLowerCase())) {
                            Log.d("callingActivty", "requiredName found" + name);
                            mNameListFounded.add(name);
                            isFound = true;
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
                CallingActivity.this.newIntance.dismiss();
                if (isFound){
                    updateSpinner();

                }


                else
                    Toast.makeText(CallingActivity.this, "Name not found", Toast.LENGTH_LONG).show();
                isFound = false;
            } catch (Exception s) {
                // Toast.makeText(CallingActivity.this, "Exception:" + s.getMessage(), Toast.LENGTH_LONG).show();

            }

        }

        private void updateSpinner() throws Exception {
            if (mNameListFounded.size() == 1) {


                spinnerList.set(0, mNameListFounded.get(0));
                adapter.notifyDataSetChanged();
                startMakingCalling();

            } else if (mNameListFounded.size() > 1) {
                spinnerList.addAll(mNameListFounded);
                adapter.notifyDataSetChanged();
                callpickerSpinner.performClick();
            }
        }
    }
}
