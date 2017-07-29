package com.ameerhamza6733.okAmeer.UI.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.UI.customfonts.MyTextView;

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
                Intent intent = new Intent(LoginOrLogout.this,singUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                LoginOrLogout.this.startActivity(intent);
            }
        });
    }
}
