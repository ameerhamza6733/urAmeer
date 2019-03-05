package com.ameerhamza6733.okAmeer.utial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;

public class ExtraUnits {
    public static final String GetUserMobileId(Context context){
       try {
           return  Settings.Secure.getString(context.getContentResolver(),
                   Settings.Secure.ANDROID_ID);
       }catch (Exception e){
           return "unable to get user id: "+e.getMessage();
       }
    }

    public static void sendFeedbackEmail(String[] addresses, String subject,Context context) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
           context. startActivity(intent);
        }
    }
    public static boolean getPolicyFlag(Context context){
        final SharedPreferences sharedPref = context.getSharedPreferences("Settings", 0);
        return sharedPref.getBoolean("AGREET_TO_PRIVACY_POLICY",false);
    }
    public static void SaveAgreeToPolicyFlag(Context context, boolean isAgree){
        final SharedPreferences sharedPref = context.getSharedPreferences("Settings", 0);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("AGREET_TO_PRIVACY_POLICY",isAgree).apply();

    }


}
