package com.ameerhamza6733.okAmeer


import android.content.Intent
import android.speech.SpeechRecognizer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.ameerhamza6733.okAmeer.fragment.show_available_command_list
import com.ameerhamza6733.okAmeer.fragment.voiceRecgonizationFragment
import com.ameerhamza6733.okAmeer.interfacess.mttsListener
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech
import com.ameerhamza6733.okAmeer.utial.show_commands_list


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

            val intent = Intent(this, show_commands_list::class.java)
            startActivity(intent)
        }




    }

    override fun onDestroy() {
        super.onDestroy()
        myTextToSpeech.stop();
    }





}
