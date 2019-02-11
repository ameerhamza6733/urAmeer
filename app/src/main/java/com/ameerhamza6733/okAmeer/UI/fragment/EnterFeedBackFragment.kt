package com.ameerhamza6733.okAmeer.UI.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import com.ameerhamza6733.okAmeer.R
import com.crashlytics.android.Crashlytics


class EnterFeedBackFragment : DialogFragment() {
    private var listener: OnUserFeedbackListener? = null
    private val TAG ="EnterFeedBackFragment";

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
   val rootVeiew =       inflater.inflate(R.layout.fragment_enter_feed_back, container, false)
        val editTextFeedback  = rootVeiew.findViewById<EditText>(R.id.editTextUserFeedBack)
        val btSendFeedback=rootVeiew.findViewById<Button>(R.id.btSendFeedback);
        btSendFeedback.setOnClickListener {
            Crashlytics.log(Log.INFO,TAG,"feedback: "+editTextFeedback.text.toString())
            onFeedbackButtonPressed(editTextFeedback?.text.toString())
            dismiss()
        }
        return rootVeiew;
    }

    fun onFeedbackButtonPressed(feedback:String) {
        listener?.onFeedBack(feedback)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnUserFeedbackListener) {
            listener = context
        } else {
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnUserFeedbackListener {

        fun onFeedBack(feedback:String)
    }

}
