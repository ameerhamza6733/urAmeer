/* ====================================================================
 * Copyright (c) 2014 Alpha Cephei Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY ALPHA CEPHEI INC. ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL CARNEGIE MELLON UNIVERSITY
 * NOR ITS EMPLOYEES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 */

package com.ameerhamza6733.okAmeer.utial;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;


public class SpeechRecognizerManager {

    /* Keyword we are looking for to activate menu */
    public static final String OK_AMEER = "ok AMEER";
    /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "wakeup";
    private static final String TAG = SpeechRecognizerManager.class.getSimpleName();
    private edu.cmu.pocketsphinx.SpeechRecognizer mPocketSphinxRecognizer;
    private Context mContext;
    private OnMagicWordListener onmagicWordListener;
    private LifeCycle lifeCycle;
    private AsyncTask<Void, Void, Exception> intiReconginerTask;


    public SpeechRecognizerManager(Context context, LifeCycle lifeCycle) {
        this.mContext = context;
        this.lifeCycle = lifeCycle;
        initPockerSphinx();
        if (intiReconginerTask != null)
            intiReconginerTask.execute();


    }


    private void initPockerSphinx() {

        intiReconginerTask = new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(mContext);

                    //Performs the synchronization of assets in the application and external storage
                    File assetDir = assets.syncAssets();

                    //Creates a new SpeechRecognizer builder with a default configuration
                    SpeechRecognizerSetup speechRecognizerSetup = defaultSetup();

                    //Set Dictionary and Acoustic Model files
                    speechRecognizerSetup.setAcousticModel(new File(assetDir, "en-us-ptm"));
                    speechRecognizerSetup.setDictionary(new File(assetDir, "ameer.dict"));

                    // Threshold to tune for keyphrase to balance between false positives and false negatives
                    speechRecognizerSetup.setKeywordThreshold(1e-45f);

                    //Creates a new SpeechRecognizer object based on previous set up.
                    mPocketSphinxRecognizer = speechRecognizerSetup.getRecognizer();

                    mPocketSphinxRecognizer.addListener(new PocketSphinxRecognitionListener());

                    // Create keyword-activation search.
                    mPocketSphinxRecognizer.addKeyphraseSearch(KWS_SEARCH, OK_AMEER);
                    mPocketSphinxRecognizer.addKeyphraseSearch(KWS_SEARCH, OK_AMEER);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (mPocketSphinxRecognizer != null) {
                    if (result != null) {
                        Toast.makeText(mContext, "Failed to init mPocketSphinxRecognizer ", Toast.LENGTH_SHORT).show();
                    } else {
                        lifeCycle.onPocketSphinxStart();
                        restartSearch(KWS_SEARCH);
                    }
                }


            }
        };

    }


    public void destroy() {
        if (intiReconginerTask != null)
            intiReconginerTask.cancel(true);
        if (mPocketSphinxRecognizer != null) {
            mPocketSphinxRecognizer.cancel();
            mPocketSphinxRecognizer.shutdown();
            mPocketSphinxRecognizer = null;
            onmagicWordListener = null;
            mContext = null;
        }
    }

    public void cancel() {
        if (mPocketSphinxRecognizer != null)
            mPocketSphinxRecognizer.cancel();

    }

    public void restartSearch(String searchName) {

        mPocketSphinxRecognizer.stop();
        mPocketSphinxRecognizer.startListening(searchName);

    }

    public void startListening(String word) {
        if (mPocketSphinxRecognizer != null)
            mPocketSphinxRecognizer.startListening(OK_AMEER);
    }

    public void setOnResultListner(OnMagicWordListener onmagicWordListener) {
        this.onmagicWordListener = onmagicWordListener;
    }


    public interface OnMagicWordListener {
        void OnMagicWordDeceted(String word);
    }

    public interface LifeCycle {
        void onPocketSphinxStart();
    }

    protected class PocketSphinxRecognitionListener implements edu.cmu.pocketsphinx.RecognitionListener {

        @Override
        public void onBeginningOfSpeech() {
        }


        /**
         * In partial result we get quick updates about current hypothesis. In
         * keyword spotting mode we can react here, in other modes we need to wait
         * for final result in onResult.
         */
        @Override
        public void onPartialResult(Hypothesis hypothesis) {
            if (hypothesis == null) {
                Log.d(TAG, "null");


                return;

            }


            String text = hypothesis.getHypstr();

            if (text.equalsIgnoreCase(OK_AMEER)) {
                onmagicWordListener.OnMagicWordDeceted(text);
                if (mContext!=null)
                Toast.makeText(mContext, "You said: " + text, Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onResult(Hypothesis hypothesis) {
        }


        /**
         * We stop mPocketSphinxRecognizer here to get a final result
         */
        @Override
        public void onEndOfSpeech() {

        }

        public void onError(Exception error) {
        }

        @Override
        public void onTimeout() {
        }

    }
}