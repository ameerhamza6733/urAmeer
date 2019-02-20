package com.ameerhamza6733.okAmeer.UI.Activitys

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast

import com.ameerhamza6733.okAmeer.R
import com.ameerhamza6733.okAmeer.utial.ExtraUnits

class AgreeToPolicyActivty : AppCompatActivity() {
    private var agreeToPrivicyPolicy = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalish_activty)

        val checkBox = findViewById<CheckBox>(R.id.PrivacyPolicyCheckBox)
        val tvCheckBox = findViewById<TextView>(R.id.tvAgreeToPrivacyPolicy)
        val btAgree = findViewById<Button>(R.id.btAgreeToPrivacyPolciy)

        checkBox.setOnClickListener {
            agreeToPrivicyPolicy = checkBox.isChecked
        }
        btAgree.setOnClickListener {
            if (agreeToPrivicyPolicy){
                ExtraUnits.SaveAgreeToPolicyFlag(this@AgreeToPolicyActivty, true)
                startActivity(Intent(this@AgreeToPolicyActivty, MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(this@AgreeToPolicyActivty,"Please agree to privacy policy ",Toast.LENGTH_LONG).show()
            }
        }
        tvCheckBox.setOnClickListener {


            val openUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://okameer.blogspot.com/2017/07/privacy-policy.html"))
            startActivity(openUrlIntent)
        }


    }
}
