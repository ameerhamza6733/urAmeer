package com.ameerhamza6733.okAmeer.utial;

import android.content.Context;

import com.ameerhamza6733.okAmeer.interfacess.noNeedCommander;

/**
 * Created by AmeerHamza on 6/23/2017.
 */

public class sendToActivtys {
    private noNeedCommander nonHindiQurary;
    public void sendingDataToActivitys(Context context , String date, String ActivtyName)throws Exception
    {


        nonHindiQurary = (noNeedCommander) context;




            nonHindiQurary.onNoCommandrExcute(date);


    }
}
