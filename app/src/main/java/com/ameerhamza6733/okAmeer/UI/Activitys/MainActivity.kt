package com.ameerhamza6733.okAmeer.UI.Activitys


import android.content.Intent
import android.speech.SpeechRecognizer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.ameerhamza6733.okAmeer.R
import com.ameerhamza6733.okAmeer.UI.fragment.voiceRecgonizationFragment
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech


class MainActivity : AppCompatActivity() {


    private val speechRecognizer: SpeechRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val mSpeakButton = findViewById(R.id.speakButton) as ImageButton

        mSpeakButton.setOnClickListener {
            val fm = this@MainActivity.supportFragmentManager
            val newFragment = voiceRecgonizationFragment.newInstance("hi",true,true);

            newFragment.setStyle(1, R.style.AppTheme)

                      newFragment.show(fm, "fragment_voice_input")
        }
        myTextToSpeech.intiTextToSpeech(this, "hi", "")
        val mShowCommand = findViewById(R.id.show_Command_hint) as ImageButton
        mShowCommand.setOnClickListener {

            val intent = Intent(this, singUpActivity::class.java)
            startActivity(intent)
        }




    }

    override fun onDestroy() {
        super.onDestroy()
        myTextToSpeech.stop();
    }





}
