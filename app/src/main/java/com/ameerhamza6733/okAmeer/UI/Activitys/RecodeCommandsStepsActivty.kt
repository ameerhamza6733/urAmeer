package com.ameerhamza6733.okAmeer.UI.Activitys

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import androidx.work.*
import com.ameerhamza6733.okAmeer.R
import com.ameerhamza6733.okAmeer.UI.fragment.RecodeUserVoiceFragment
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.SendSmsActivity
import com.ameerhamza6733.okAmeer.utial.Uploader.UploadImageWorker
import lolodev.permissionswrapper.callback.OnRequestPermissionsCallBack
import lolodev.permissionswrapper.wrapper.PermissionWrapper


class RecodeCommandsStepsActivty : AppCompatActivity(), RecodeUserVoiceFragment.OnFragmentInteractionListener {
    override fun onVoiceRecoded(filePath: String, folderName: String) {
        mFilePath=filePath
        mFolderName=folderName
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            uploadImageToServer(filePath, folderName)
        } else{
            askRunTimePermissions()
        }

        if (iterator?.hasNext()) {
            val command = iterator?.next()
            commadsMap.get(command)?.let { onNextCommand(command, it) }
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    lateinit var rendomeCommandsHashSet: List<String>


    lateinit var commadsMap: Map<String, String>
    lateinit var tvCommndsDone: TextView
    lateinit var btSkipRecodingForNow: TextView
    var counter = 1
    var mFilePath=""
    var mFolderName = ""

    companion object {
        lateinit var iterator: Iterator<String>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recode_commands_steps_activty)

        tvCommndsDone = findViewById(R.id.tvCommandsDone)
        btSkipRecodingForNow = findViewById(R.id.btSkipCommandRecoding)

        var commandsList = resources.getStringArray(R.array.recoding_text_urdu).toList()
        var commandListRomanUrdu = resources.getStringArray(R.array.recoding_text_roman_urdu).toList()
        commadsMap = commandsList.zip(commandListRomanUrdu).toMap()
        commandsList = commandsList.shuffled()
        rendomeCommandsHashSet = commandsList.subList(0, 5)
        iterator = rendomeCommandsHashSet.iterator()

        if (iterator?.hasNext()) {
            val command = iterator?.next()
            commadsMap.get(command)?.let { onNextCommand(command, it) }
        } else {
            startActivity(Intent(this@RecodeCommandsStepsActivty, MainActivity::class.java))
            finish()
        }


        btSkipRecodingForNow.setOnClickListener {
            startActivity(Intent(this@RecodeCommandsStepsActivty, MainActivity::class.java))
            finish()
        }
    }

    private fun onNextCommand(commandTobeRecoded: String, romanCommnd: String) {
        tvCommndsDone.setText("$counter/5")
        counter++
        supportFragmentManager?.beginTransaction()?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)?.replace(R.id.container, RecodeUserVoiceFragment.newInstance(commandTobeRecoded, romanCommnd))?.disallowAddToBackStack()?.commit()

    }

    private fun uploadImageToServer(imageAbsolutePath: String, folderName: String) {
        val myData = Data.Builder()
                .putString(UploadImageWorker.KEY_IMAGE_PATH_ARG, imageAbsolutePath)
                .putString(UploadImageWorker.KEY_FOLDER_NAME, folderName)
                .build()

        val myConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val uploadImageWork = OneTimeWorkRequest.Builder(UploadImageWorker::class.java)
                .setInputData(myData)
                .setConstraints(myConstraints)
                .build()
        WorkManager.getInstance().enqueueUniqueWork(imageAbsolutePath, ExistingWorkPolicy.KEEP, uploadImageWork)
    }
    private fun askRunTimePermissions() {

        PermissionWrapper.Builder(this)
                .addPermissions(arrayOf( Manifest.permission.READ_EXTERNAL_STORAGE))
                //enable rationale message with a custom message

                //show settings dialog,in this case with default message base on requested permission/s
                .addPermissionsGoSettings(true)
                //enable callback to know what option was choosed
                .addRequestPermissionsCallBack(object : OnRequestPermissionsCallBack {
                    override fun onGrant() {
                        Log.i(SendSmsActivity::class.java.simpleName, "Permission was granted.")

                        uploadImageToServer(mFilePath, mFolderName)
                    }

                    override fun onDenied(permission: String) {

                    }
                }).build().request()
    }
}
