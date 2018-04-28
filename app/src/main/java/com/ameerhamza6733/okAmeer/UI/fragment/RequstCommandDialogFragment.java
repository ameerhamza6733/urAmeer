package com.ameerhamza6733.okAmeer.UI.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.interfacess.RequstCommandDialogeFragmentToActivty;

/**
 * Created by AmeerHamza on 7/16/2017.
 */

public class RequstCommandDialogFragment extends DialogFragment {
    EditText editTextRequstedCommandName;
    EditText editTextRequstedCommandActivationLines;
    RequstCommandDialogeFragmentToActivty mCallBackToActivty;

    /**
     * Public static constructor that creates fragment and
     * passes a bundle with data into it when adapter is created
     */
    public static RequstCommandDialogFragment newInstance() {
        RequstCommandDialogFragment addMealDialogFragment = new RequstCommandDialogFragment();
        Bundle bundle = new Bundle();
        addMealDialogFragment.setArguments(bundle);
        return addMealDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Open the keyboard automatically when the dialog fragment is opened
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("commandRequst","onCreateDialog");
        /* Use the Builder class for convenient dialog construction */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        /* Get the layout inflater */
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_requst_command, null);
        editTextRequstedCommandName = (EditText) rootView.findViewById(R.id.edit_text_requst_command_name);
        editTextRequstedCommandActivationLines = (EditText) rootView.findViewById(R.id.edit_text_requst_command_activation_lines);

        this.mCallBackToActivty= (RequstCommandDialogeFragmentToActivty) getActivity();
        /**
         * Call requstNewCommand() when user taps "Done" keyboard action
         */
        editTextRequstedCommandName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    Toast.makeText(getActivity(), "please enter command activation Phrase", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        editTextRequstedCommandActivationLines.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    // requstNewCommand();
                }
                return true;
            }
        });

        /* Inflate and set the layout for the dialog */
        /* Pass null as the parent view because its going in the dialog layout */
        builder.setView(rootView)
                /* Add action buttons */
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Log.d("command requst",editTextRequstedCommandName.getText().toString()+editTextRequstedCommandActivationLines.getText().toString());

                        requstNewCommand(editTextRequstedCommandName.getText().toString(), editTextRequstedCommandActivationLines.getText().toString());
                    }
                });

        return builder.create();
    }



    /**
     * Add requsted  command
     */
    public void requstNewCommand(String name, String phrase) {
            this.mCallBackToActivty.onRequstNewCommand(editTextRequstedCommandName.getText().toString(), editTextRequstedCommandActivationLines.getText().toString());

    }
}