package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.CommandModel;

/**
 * Created by AmeerHamza on 7/3/2017.
 */

public class WifiOnCommand implements Command {
    @Override
    public void execute(CommandModel commandModel) {
        Log.d("wifi","wifi on...");
        WifiManager wifiManager = (WifiManager) commandModel.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);

    }

    @Override
    public String getDefaultPhrase() {
        return "وائی \u200B\u200Bفائی آن کر,وائی فائی آن کرو,WiFi آن,WiFi چلاو,Wi-Fi on karo,Wi-Fi chalao";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return context.getResources().getString(R.string.WIFI_ON_Kea_Ja_Raha_Ha);
    }
}
