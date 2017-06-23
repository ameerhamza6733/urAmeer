package com.ameerhamza6733.okAmeer.utial;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.interfacess.NonHindiQurary;

/**
 * Created by AmeerHamza on 6/23/2017.
 */

public class sendHiEnglishDateToActivtys  {
    private NonHindiQurary nonHindiQurary;
    public void setEnglishDateActivtys(Context context , String date,String ActivtyName)throws Exception
    {


        nonHindiQurary = (NonHindiQurary) context;


        if(ActivtyName.equals("CallingActivity"))
        {

            nonHindiQurary.onNonHindiQuraryRecived(date);
        }

    }
}
