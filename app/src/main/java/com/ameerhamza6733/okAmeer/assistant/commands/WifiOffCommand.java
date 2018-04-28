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

public class WifiOffCommand implements Command {
    @Override
    public void execute(CommandModel commandModel) {
        Log.d("wifi","wifi off..");
        WifiManager wifiManager = (WifiManager) commandModel.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
    }

    @Override
    public String getDefaultPhrase() {
        return "وائی \u200B\u200Bفائی آف,وائی \u200B\u200Bفائی بند,وائی فائی بند,وائی فائی آف,wifi آف,Wi-Fi band karo,WiFi off karo";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return context.getResources().getString(R.string.Wifi_Of_Kea_Ja_Raha_Ha);
    }
}
