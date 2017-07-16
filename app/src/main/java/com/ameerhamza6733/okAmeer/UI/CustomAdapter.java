package com.ameerhamza6733.okAmeer.UI;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.utial.models.Command;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by AmeerHamza on 7/16/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";
    private final List<Command> mDataSet;


    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView,votingCommandPhrase;
        private  final Button VotingCommandCountButton;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            textView = (TextView) v.findViewById(R.id.voting_command_name_textView);
            votingCommandPhrase= (TextView) v.findViewById(R.id.voting_command_phrase_textView);
            VotingCommandCountButton= (Button) v.findViewById(R.id.voting_command_count_button);
        }

        public TextView getTextView() {
            return textView;
        }

        public TextView getVotingCommandPhrase() {
            return votingCommandPhrase;
        }

        public Button getVotingCommandCountButton() {
            return VotingCommandCountButton;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public CustomAdapter(List<Command> dataSet) {
        mDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.voting_each_row, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder,  int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element

        viewHolder.getTextView().setText(mDataSet.get(position).getCommandPOJO().getCommandName());
        viewHolder.getVotingCommandPhrase().setText(mDataSet.get(position).getCommandPOJO().getCommandAction());
        viewHolder.getVotingCommandCountButton().setText("VOTE("+String.valueOf(mDataSet.get(position).getVotebySize())+")");
        viewHolder.getVotingCommandCountButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                mDatabase.child("UserRequstedCommands").child(praseEmail(mDataSet.get(viewHolder.getAdapterPosition()).getCommandPOJO().getCommandRequstedby())).child(mDataSet.get(viewHolder.getAdapterPosition()).getCommandPOJO().getCommandName()).child("VotedBy").child(praseEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail())).setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                else
                    Toast.makeText(viewHolder.getTextView().getContext(),"please login to your account to cast vote",Toast.LENGTH_LONG).show();

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
    private String praseEmail(String email) {

        assert email != null;
        email = email.replace("@", "atThe");
        return email.replace(".", "Dot");
    }
}