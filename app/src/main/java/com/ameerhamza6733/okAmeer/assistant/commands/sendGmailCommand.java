package com.ameerhamza6733.okAmeer.assistant.commands;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;

import com.ameerhamza6733.okAmeer.assistant.Command;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.sendEmailActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;

import de.cketti.mailto.EmailIntentBuilder;

/**
 * Created by AmeerHamza on 7/9/2017.
 */

public class sendGmailCommand implements Command {
    @Override
    public void execute(Context context, String predicate) {

        Intent  intent = new Intent(context,sendEmailActivity.class);
        intent.putExtra("EmailExtra",predicate);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }
//
//    private void getUserPrimaryEamil(Context context) {
//        String gmail = null;
//
//        Pattern gmailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
//        Account[] accounts = AccountManager.get(context).getAccounts();
//        for (Account account : accounts) {
//
//            if (gmailPattern.matcher(account.name).matches()) {
//                gmail = account.name;
//
//                Log.d("gamil",gmail);
//            }
//        }
//    }


    @Override
    public String getDefaultPhrase() {
        return "ای میل,Gmail";
    }

    @Override
    public String getTtsPhrase(Context context) {
        return null;
    }
}
