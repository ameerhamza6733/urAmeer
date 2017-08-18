package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.BluetoothActivity;

/**
 * Created by AmeerHamza on 7/21/2017.
 */

public class BluetoothOnCommand implements Command {
    @Override
    public void execute(Context context, String predicate) {
        Intent intent = new Intent(context, BluetoothActivity.class);
        intent.putExtra(BluetoothActivity.BluetoothActivityEXTRA,true);
        context.startActivity(intent);

    }

    @Override
    public String getDefaultPhrase() {
        return "بلوٹوت آن کرو,بلوٹوت چلاو,Bluetooth on karo,Bluetooth chalao";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return context.getString(R.string.blootooth_on_kiya_ja_raha_hai);}
}
