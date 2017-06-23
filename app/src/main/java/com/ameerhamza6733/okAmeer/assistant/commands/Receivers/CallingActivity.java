package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.fragment.voiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.interfacess.NonHindiQurary;

import java.util.ArrayList;
import java.util.List;

public class CallingActivity extends AppCompatActivity implements NonHindiQurary {

    private static final int PICK_CONTACT_REQUEST = 1;
    private voiceRecgonizationFragment newIntance;
    protected Spinner callpickerSpinner;
    private ImageView CallOk;
    private ImageView CallCancle;
    private boolean isSpinnerTouch=false;
    protected String[] items;
    protected ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                newIntance =  voiceRecgonizationFragment.newInstance("en-hi",false);
                newIntance.show(getSupportFragmentManager(), "CallingActivity");
                newIntance.setStyle(1, R.style.AppTheme);


            }
        }, 100);
        callpickerSpinner = (Spinner) findViewById(R.id.caling_spinner);
       items = new String[] {"Contact" };

       adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);

        callpickerSpinner.setAdapter(adapter);

        callpickerSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("calingActivty","spinner listerner");
                if(!isSpinnerTouch)
                pickContact();
                isSpinnerTouch=true;

                return true;
            }
        });

    }

    @Override
    public void onNonHindiQuraryRecived(String Queary) {
        new myContentNameFinder(Queary).execute();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                isSpinnerTouch=false;
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
                Log.d("callingActivty","number from phone book="+number);

                // Do something with the phone number...
            }
        }
    }
    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }
    private class myContentNameFinder extends AsyncTask<Void,Void,Void>{
        private String Queary;
        boolean isFound=false;
        private List<String> NameListFounded = new ArrayList<>();


        public myContentNameFinder(String queary) {
            Queary = queary;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Log.d("callingActivty","requiredName="+Queary);
                ContentResolver cr = getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);

                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        String id = cur.getString(
                                cur.getColumnIndex(ContactsContract.Contacts._ID));
                         String name = cur.getString(cur.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME));
                       if(name.toLowerCase().contains(Queary.toLowerCase()))
                       {
                           Log.d("callingActivty","requiredName found"+name);
                           NameListFounded.add(name);
                           isFound=true;
                       }
                        if (cur.getInt(cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                    new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                String phoneNo = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));



                            }
                            pCur.close();
                        }
                    }
                }
            }catch (Exception e)
            {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CallingActivity.this.newIntance.dismiss();
            if(isFound){
                Log.d("callingActivity","name found onPostExecute"+NameListFounded.get(0));
               CallingActivity.this.items[0]=NameListFounded.get(0);
                CallingActivity.this.adapter.notifyDataSetChanged();
            }


        }
    }
}
