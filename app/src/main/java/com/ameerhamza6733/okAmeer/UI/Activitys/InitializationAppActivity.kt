package com.ameerhamza6733.okAmeer.UI.Activitys

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.ameerhamza6733.okAmeer.R
import com.ameerhamza6733.okAmeer.utial.ExtraUnits
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech

class InitializationAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initialization_app)
        val tvInitiAppStatus = findViewById<TextView>(R.id.tvIntiAppSettingsStatus)
        tvInitiAppStatus.text = "Checking Text to speech"
        myTextToSpeech.intiTextToSpeech(applicationContext,"hi","")
        myTextToSpeech.setTextToSpeechListener {
            tvInitiAppStatus.setText("Successfuly binded to Google text to speech ")
            val ttsSettingIntent = Intent(this, MainActivity::class.java)
            ttsSettingIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            if (!ExtraUnits.getPolicyFlag(this)){
                startActivity(Intent(this@InitializationAppActivity,AgreeToPolicyActivty::class.java))
                finish()
            }else
            startActivity(ttsSettingIntent)
            finish()
        }
    }
}
