package com.ameerhamza6733.okAmeer.UI.Activitys;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.UI.customfonts.MyTextView;
import com.ameerhamza6733.okAmeer.UI.fragment.VoiceRecgonizationFragment;

public class LoginOrLogout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_logout);
        MyTextView mSignIn = (MyTextView) findViewById(R.id.signin);
        MyTextView mSignUp = (MyTextView) findViewById(R.id.Signup);

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginOrLogout.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                LoginOrLogout.this.startActivity(intent);

            }
        });
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginOrLogout.this,SingUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                LoginOrLogout.this.startActivity(intent);
            }
        });

    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                showVoiceFragment();
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                showVoiceFragment();
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    private void showVoiceFragment() {
        try {
            if(!isFinishing()){

                FragmentTransaction transactionFragment = getSupportFragmentManager().beginTransaction();
                VoiceRecgonizationFragment newIntance = VoiceRecgonizationFragment.newInstance("en-IN", false, true);
                newIntance.setStyle(1, R.style.AppTheme);
                transactionFragment.add(android.R.id.content, newIntance).addToBackStack(null).commitAllowingStateLoss();

            }


        }catch (Exception e){
            Toast.makeText(this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}


