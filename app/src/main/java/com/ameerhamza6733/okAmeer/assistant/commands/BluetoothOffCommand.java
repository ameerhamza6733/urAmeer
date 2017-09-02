package com.ameerhamza6733.okAmeer.assistant.commands;

import android.content.Context;
import android.content.Intent;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.CommandModel;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.BluetoothActivity;

/**
 * Created by AmeerHamza on 7/21/2017.
 */

public class BluetoothOffCommand implements Command {
    @Override
    public void execute(CommandModel commandModel) {
        Intent intent = new Intent(commandModel.getContext(), BluetoothActivity.class);
        intent.putExtra(BluetoothActivity.BluetoothActivityEXTRA,false);
        commandModel.getContext().startActivity(intent);
    }

    @Override
    public String getDefaultPhrase() {
        return "بلوٹوت بند کرو,بلوٹوت آف کرو,bluetooth off karo,Bluetooth band karo";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return context.getString(R.string.blootooth_band_kiya_ja_raha_hai);
    }
}
