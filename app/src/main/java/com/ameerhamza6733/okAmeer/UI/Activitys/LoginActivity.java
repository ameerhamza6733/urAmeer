package com.ameerhamza6733.okAmeer.UI.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    public static String ExtraEmail="ExtraEmail";
    TextView createAccount;
    Typeface fonts1;
    EditText userEmail,userPassword;
    private FirebaseAuth mAuth;
    private com.dd.processbutton.iml.ActionProcessButton userlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createAccount = (TextView) findViewById(R.id.create);
        userEmail= (EditText) findViewById(R.id.emailLogin);
        userPassword= (EditText) findViewById(R.id.passwordLogin);
        userlogin= (com.dd.processbutton.iml.ActionProcessButton) findViewById(R.id.signin1);
        mAuth = FirebaseAuth.getInstance();
        Intent intent=getIntent();
        if(intent!=null){
            userEmail.setText(intent.getStringExtra(ExtraEmail));
        }

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, SingUpActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        userlogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
//                if(userEmail.getText().toString().isEmpty() || userPassword.getText().toString().isEmpty()){
//                    Toast.makeText(LoginActivity.this,"please enter Email and password both",Toast.LENGTH_SHORT).show();
//                    return;
//                }
               // LoginIntoFirebase();
            }
        });


        fonts1 = Typeface.createFromAsset(this.getAssets(),
                "fonts/Lato-Regular.ttf");
        createAccount.setTypeface(fonts1);


    }

    private void LoginIntoFirebase() {
        this.userlogin.setLoadingText("FINDING YOUR ACCOUNT...");
        userlogin. setMode(ActionProcessButton.Mode.ENDLESS);
        userlogin.setProgress(1);

        mAuth.signInWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userlogin.setProgress(0);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed."+task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                            userlogin.setProgress(0);
                            updateUI(null);
                        }

                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        if(user!=null){
            Intent i = new Intent(LoginActivity.this, VotingActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

    }
}
