package com.ameerhamza6733.okAmeer.UI.Activitys;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ameerhamza6733.okAmeer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class singUpActivity extends Activity {
    TextView signinhere;
    Typeface fonts1;
    private EditText userName;
    private EditText Email;
    private EditText password;
    private TextView msignUp;

    private FirebaseAuth mAuth;
    private String TAG = "sing up activity tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up_with_phone_number);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
        signinhere = (TextView) findViewById(R.id.signinhere);
        userName= (EditText) findViewById(R.id.username);
        Email = (EditText) findViewById(R.id.email);
        password= (EditText) findViewById(R.id.password);
        msignUp= (TextView) findViewById(R.id.signup1);



        msignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("singUpActivty","userInfo"+userName.getText()+password.getText()+Email.getText());
            }
        });
        signinhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        fonts1 = Typeface.createFromAsset(this.getAssets(),
                "fonts/Lato-Regular.ttf");


        signinhere.setTypeface(fonts1);
//            mAuth.createUserWithEmailAndPassword("ameerhamza@gmail.com", "hamza12")
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "createUserWithEmail:success");
//                                FirebaseUser user = mAuth.getCurrentUser();
//                                updateUI(user);
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                                Toast.makeText(singUpActivity.this, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//                                updateUI(null);
//                            }
//
//                            // ...
//                        }
////                    });
//
//        mAuth.signInWithEmailAndPassword("ameerhamza@gmail.com", "hamza12")
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(singUpActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//
//
//    }
//
//    private void updateUI(FirebaseUser user) {
//        try {
//            Log.d(TAG,"updateing ui..."+user.getDisplayName());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
    }
}
