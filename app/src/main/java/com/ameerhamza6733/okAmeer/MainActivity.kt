package com.ameerhamza6733.okAmeer


import android.speech.SpeechRecognizer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageButton
import com.ameerhamza6733.okAmeer.fragment.voiceRecgonizationFragment


class MainActivity : AppCompatActivity() {

    private val speechRecognizer: SpeechRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val mSpeakButton = findViewById(R.id.speakButton) as ImageButton
        mSpeakButton.setOnClickListener {
            val fm = this@MainActivity.supportFragmentManager
            val editNameDialogFragment = voiceRecgonizationFragment.newInstance("voice_reg_frag")
            editNameDialogFragment.setStyle(1, R.style.AppTheme)
            editNameDialogFragment.show(fm, "fragment_edit_name")
        }

    }


}
