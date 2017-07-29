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
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;

public class BluetoothActivity extends AppCompatActivity {

    public  static String BluetoothActivityEXTRA ="BluetoothActivityEXTRA";
    private static int BLUETOOTH_PERMISSION_CODE=1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1)
            requestForSpecificPermission();
        else
            turnONorOfF();



    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode ==BLUETOOTH_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                turnONorOfF();
            }else if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_DENIED){
                Toast.makeText(BluetoothActivity.this,"To access bluetooth we need permission",Toast.LENGTH_SHORT).show();
            }else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
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

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.BLUETOOTH,Manifest.permission.BLUETOOTH_ADMIN}, BLUETOOTH_PERMISSION_CODE);

    }
}
