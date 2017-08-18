package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;

import lolodev.permissionswrapper.callback.OnRequestPermissionsCallBack;
import lolodev.permissionswrapper.wrapper.PermissionWrapper;

public class BluetoothActivity extends AppCompatActivity {

    public  static String BluetoothActivityEXTRA ="BluetoothActivityEXTRA";
    private static int BLUETOOTH_PERMISSION_CODE=1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        if (Build.VERSION.SDK_INT > 22){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                askRunTimePermissions();
                return;
            }

        }

        else
            turnONorOfF();



    }

    private void askRunTimePermissions() {
        new PermissionWrapper.Builder(this)
                .addPermissions(new String[]{ Manifest.permission.BLUETOOTH , Manifest.permission.BLUETOOTH_ADMIN })
                //enable rationale message with a custom message

                //show settings dialog,in this case with default message base on requested permission/s
                .addPermissionsGoSettings(true)
                //enable callback to know what option was choosed
                .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                    @Override
                    public void onGrant() {
                        Log.i(sendSmsActivity.class.getSimpleName(), "Permission was granted.");
                      turnONorOfF();


                    }

                    @Override
                    public void onDenied(String permission) {
                        Log.i(sendSmsActivity.class.getSimpleName(), "Permission was not granted.");
                    }
                }).build().request();
    }


    private void turnONorOfF() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        boolean isEnabled = bluetoothAdapter.isEnabled();
        if(getIntent()!=null){
            boolean blueToothOnOff = getIntent().getBooleanExtra(BluetoothActivityEXTRA,false);
            if(blueToothOnOff && !isEnabled){
                bluetoothAdapter.enable();
                finish();

            }else if(!blueToothOnOff && isEnabled) {
                bluetoothAdapter.disable();
                finish();
            }
        }
    }


}
