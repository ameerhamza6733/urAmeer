package com.ameerhamza6733.okAmeer.UI.Activitys;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.UI.CustomAdapter;
import com.ameerhamza6733.okAmeer.UI.fragment.requstCommandDialogFragment;
import com.ameerhamza6733.okAmeer.interfacess.RequstCommandDialogeFragmentToActivty;
import com.ameerhamza6733.okAmeer.utial.models.Command;
import com.ameerhamza6733.okAmeer.utial.models.CommandPOJO;
import com.ameerhamza6733.okAmeer.utial.TTSService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class votingActivity extends AppCompatActivity implements RequstCommandDialogeFragmentToActivty {

    private FirebaseAuth mAuth;
    private Menu menu;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private CustomAdapter mAdapter;
    private List<Command> mDataset = new ArrayList<>();
    private ProgressBar mProgresBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        FloatingActionButton feb = (FloatingActionButton) findViewById(R.id.addCommadRequstFeb);
        mAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.my_Recylerivew_);
        mProgresBar= (ProgressBar) findViewById(R.id.voting_progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        Log.d("firebase ", "ref = " + mDatabase.child("UserRequstedCommands").getKey());
        feb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {

                    DialogFragment dialog = requstCommandDialogFragment.newInstance();
                    dialog.show(votingActivity.this.getFragmentManager(), "RequstCommandDialogFragment");
                } else {
                    Intent intent = new Intent(votingActivity.this, TTSService.class);
                    intent.putExtra("toSpeak", getString(R.string.Draeeme_harabaanee_apana_akaunt_kholie_usake_baad_hee_aap_ek_nae_kamaand_kee_darakhvaast_kar_sakate_hain));
                    intent.putExtra("Language", "hi");
                    votingActivity.this.startService(intent);

                    Intent i = new Intent(votingActivity.this, singUpActivity.class);
                    votingActivity.this.startActivity(i);
                }
            }
        });



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserRequstedCommands");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get  datasnapshot object and pass to other thread so sorting looping can be done in background

                new myAsynkTask(dataSnapshot).execute();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(votingActivity.this, "Error : " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                Log.w("tag", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        ref.addListenerForSingleValueEvent(postListener);


    }


    private void updateUI(FirebaseUser currentUser) {

        if (currentUser != null)
            menu.findItem(R.id.loginOrLogout).setTitle("Your Text").setTitle("Logout");
        else
            menu.findItem(R.id.loginOrLogout).setTitle("Your Text").setTitle("Login");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menu = menu;
        updateUI(mAuth.getCurrentUser());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.loginOrLogout:
                updateUserState();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUserState() {
        if (mAuth.getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(votingActivity.this, "Logout", Toast.LENGTH_SHORT).show();
            updateUI(mAuth.getCurrentUser());
        } else {
            Intent i = new Intent(votingActivity.this, LoginActivity.class);

            startActivity(i);
        }
    }

    @Override
    public void onRequstNewCommand(String commnadName, String commandActivactionPharase) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            mDatabase.child("UserRequstedCommands").child(praseEmail(FirebaseAuth.getInstance().getCurrentUser())).child(commnadName).child("VotedBy").child(praseEmail(FirebaseAuth.getInstance().getCurrentUser())).setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            mDatabase.child("UserRequstedCommands").child(praseEmail(FirebaseAuth.getInstance().getCurrentUser())).child(commnadName).child("CommandDetail").setValue(new CommandPOJO(commnadName, commandActivactionPharase, FirebaseAuth.getInstance().getCurrentUser().getEmail()));


        } else
            Toast.makeText(this, "somg thing wrong during write to database", Toast.LENGTH_LONG).show();

    }

    private String praseEmail(FirebaseUser currentUser) {
        String email = currentUser.getEmail();
        assert email != null;
        email = email.replace("@", "atThe");
        return email.replace(".", "Dot");
    }

    private class myAsynkTask extends AsyncTask<Void, Void, Void> {

        private DataSnapshot dataSnapshot;

        public myAsynkTask(DataSnapshot dataSnapshot) {
            this.dataSnapshot = dataSnapshot;
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (DataSnapshot nodeEmail : dataSnapshot.getChildren())
                for (DataSnapshot nodeCommandName : nodeEmail.getChildren())
                    for (DataSnapshot nodeCommandN : nodeCommandName.getChildren())
                        if (nodeCommandN.getKey().equalsIgnoreCase("CommandDetail")) {
                            mDataset.add(new Command(nodeCommandN.getValue(CommandPOJO.class)));

                        } else {

                            mDataset.set(mDataset.size() - 1, new Command(nodeCommandN.getChildrenCount(), mDataset.get(mDataset.size() - 1).getCommandPOJO()));
                        }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgresBar.setVisibility(View.GONE);
            if (votingActivity.this.mAdapter != null)
                mAdapter.notifyDataSetChanged();
            else {


                votingActivity.this.mAdapter = new CustomAdapter(mDataset);
                votingActivity.this.recyclerView.setAdapter(mAdapter);
            }
        }
    }



}
