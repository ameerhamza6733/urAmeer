package com.ameerhamza6733.okAmeer.interfacess;

/**
 * Created by AmeerHamza on 8/24/2017.
 */

public interface TTSCallBack {
    void onStart(String utteranceId);

    void onDone(String utteranceId);

    void onError(String utteranceId);
}
