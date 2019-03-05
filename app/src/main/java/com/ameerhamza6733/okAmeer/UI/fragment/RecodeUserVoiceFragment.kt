package com.ameerhamza6733.okAmeer.UI.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.ameerhamza6733.okAmeer.R
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.SendSmsActivity
import com.ameerhamza6733.okAmeer.utial.ExtraUnits
import com.devlomi.record_view.OnRecordListener
import com.devlomi.record_view.RecordButton
import com.devlomi.record_view.RecordView
import lolodev.permissionswrapper.callback.OnRequestPermissionsCallBack
import lolodev.permissionswrapper.wrapper.PermissionWrapper
import java.io.IOException


private const val AGR_RECODING_TEXT = "AGR_RECODING_TEXT"
private const val AGR_RECODING_ROMAN_TEXT = "AGR_RECODING_ROMAN_TEXT"
private const val TAG = "RecodeUserVoiceFragment";


class RecodeUserVoiceFragment : Fragment() {
    private var mRecodingText: String? = null
    private var mRecodingRomanText: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private var fileName = ""
    private var recorder: MediaRecorder? = null


    private var player: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mRecodingText = it.getString(AGR_RECODING_TEXT)
            mRecodingRomanText = it.getString(AGR_RECODING_ROMAN_TEXT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_recode_user_voice, container, false)
        val recordView = rootView.findViewById(R.id.record_view) as RecordView
        val recordButton = rootView.findViewById(R.id.btMicRecodeVoice) as RecordButton
        val tvRecodingText = rootView.findViewById<TextView>(R.id.tvRecodingText)
        val tvRecodingTextRomanUrdu = rootView.findViewById<TextView>(R.id.tvRecodingTextRomanUrdu)
        val playRecoding = rootView.findViewById<ImageButton>(R.id.playRecoding)
        val btDoneRecoding = rootView.findViewById<ImageButton>(R.id.btDoneRecoding)

        tvRecodingText.setText(mRecodingText)
        tvRecodingTextRomanUrdu.setText(mRecodingRomanText)
        recordButton.setRecordView(recordView);

        fileName = "/${ExtraUnits.GetUserMobileId(activity)}${mRecodingRomanText?.replace(" ", "")}.3gp"
        fileName = "${activity!!.externalCacheDir.absolutePath}$fileName"

        recordView.setOnRecordListener(object : OnRecordListener {
            override fun onStart() {
                //Start Recording..
                playRecoding.visibility = View.INVISIBLE
                onRecord(true)
                Log.d("RecordView", "onStart")

            }

            override fun onCancel() {
                //On Swipe To Cancel
                onRecord(false)
                Log.d("RecordView", "onCancel")

            }

            override fun onFinish(recordTime: Long) {
                //Stop Recording..
                onRecord(false)
                playRecoding.visibility = View.VISIBLE
                btDoneRecoding.visibility = View.VISIBLE
                Log.d("RecordView", "onFinish")


            }

            override fun onLessThanSecond() {
                //When the record time is less than One Second
                Log.d("RecordView", "onLessThanSecond")
            }
        })



        btDoneRecoding.setOnClickListener { listener?.onVoiceRecoded(fileName, mRecodingRomanText!!) }
        playRecoding.setOnClickListener { onPlay(true) }
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            askRunTimePermissions()
        }
        return rootView

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null

        recorder?.release();
        recorder = null;

        player?.release();
        player = null;


    }

    interface OnFragmentInteractionListener {
        fun onVoiceRecoded(filePath: String, folderName: String)
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    private fun onRecord(start: Boolean) {
        if (start) {
            startRecording()
        } else {
            stopRecording()
        }
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            Log.d(TAG, "output file  for audio recoding $fileName")
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
                start()
            } catch (ig: IllegalStateException) {
                ig.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            Log.d(TAG, "start recoding ")

        }
    }

    private fun onPlay(start: Boolean) {
        if (start) {
            startPlaying()
        } else {
            stopPlaying()
        }
    }

    private fun startPlaying() {
        player = MediaPlayer()
        try {
            player?.setDataSource(fileName)
            player?.prepare()
            player?.start()
            Toast.makeText(activity, "Playing", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Log.e(TAG, "prepare() failed")
        }

    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }

    private fun askRunTimePermissions() {

        PermissionWrapper.Builder(activity)
                .addPermissions(arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE))

                .addRequestPermissionsCallBack(object : OnRequestPermissionsCallBack {
                    override fun onGrant() {
                        Log.i(SendSmsActivity::class.java.simpleName, "Permission was granted.")


                    }

                    override fun onDenied(permission: String) {
                        Toast.makeText(activity, "We need " + permission, Toast.LENGTH_SHORT).show()

                    }
                }).build().request()
    }

    companion object {
        @JvmStatic
        fun newInstance(recodingtext: String, recodingRomanText: String) =
                RecodeUserVoiceFragment().apply {
                    arguments = Bundle().apply {
                        putString(AGR_RECODING_TEXT, recodingtext)
                        putString(AGR_RECODING_ROMAN_TEXT, recodingRomanText)
                    }
                }
    }
}
