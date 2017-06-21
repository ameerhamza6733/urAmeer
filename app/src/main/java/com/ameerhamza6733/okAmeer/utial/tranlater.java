package com.ameerhamza6733.okAmeer.utial;

import android.os.AsyncTask;
import android.util.Log;

import com.ameerhamza6733.okAmeer.interfacess.tranlaterCallback;


import org.json.JSONArray;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by AmeerHamza on 6/2/2017.
 */

public class tranlater  {
    private tranlaterCallback callback;
    private String Query;
    private String SourceLeng;
    private String TragmentLeng;

    private String Conn_URL;
    private String TAGg="tranlaterTAG";

    public tranlater(tranlaterCallback callback, String query, String sourceLeng, String tragmentLeng) {
        this.callback = callback;
        this.Query = query;
       this. SourceLeng = sourceLeng;
        this.TragmentLeng = tragmentLeng;




    }


    public void excute()
    {
        new excuteTranslater().execute();
    }
    public class  excuteTranslater extends AsyncTask<Void,Void,Void>
    {
        private String url =null;
        private StringBuffer response;
        private String TranlatedResult;
        private Exception exceptionToBeThrown;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                url= "https://translate.googleapis.com/translate_a/single?"+
                        "client=gtx&"+
                        "sl=" + tranlater.this.SourceLeng +
                        "&tl=" + tranlater.this.TragmentLeng +
                        "&dt=t&q=" + URLEncoder.encode(tranlater.this.Query, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d(TAGg,"TranableText"+Query);

            try {
                Log.d(TAGg,url);
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestProperty("User-Agent", "Mozilla/5.0");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                 response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONArray jsonArray = new JSONArray(response.toString());
                JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);
                JSONArray jsonArray3 = (JSONArray) jsonArray2.get(0);
                TranlatedResult=jsonArray3.get(0).toString();

            }catch (Exception e)
            {
               exceptionToBeThrown=e;

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (exceptionToBeThrown != null) {
                callback.onError(exceptionToBeThrown.getMessage());
                return;
            }
            if(!TranlatedResult.isEmpty())
            {
                Log.d(TAGg,"tranlatedText"+TranlatedResult);
                callback.onSuccess(TranlatedResult);
            }
        }
    }





}
