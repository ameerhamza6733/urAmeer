package com.ameerhamza6733.okAmeer.UI.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameerhamza6733.okAmeer.R;

import java.util.ArrayList;



public class HelpFragment extends Fragment {

    private String[] mCommandtitle;
    private String[] mCommandDir;
    private int counter =0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_available_command_list, container, false);
        mCommandtitle = getResources().getStringArray(R.array.command_title);
        mCommandDir = getResources().getStringArray(R.array.command_dir);
        RecyclerView recyclerView=  view.findViewById(R.id.recylerivew);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new HelpListAdupter());
        return view;
    }


    private class HelpListAdupter extends RecyclerView.Adapter<HelpListVIewHolder>{

        @NonNull
        @Override
        public HelpListVIewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.help_commands_list_view, viewGroup, false);

            return new HelpListVIewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull HelpListVIewHolder helpListVIewHolder, int i) {
            helpListVIewHolder.mCommandDescription.setText(mCommandDir[helpListVIewHolder.getAdapterPosition()]);
            helpListVIewHolder.mCommandTitle.setText(mCommandtitle[helpListVIewHolder.getAdapterPosition()]);
        }

        @Override
        public int getItemCount() {
            return mCommandtitle.length;
        }
    }
    private class HelpListVIewHolder extends  RecyclerView.ViewHolder{
        private final TextView mCommandTitle;
        private final TextView mCommandDescription;
        public HelpListVIewHolder(@NonNull View itemView) {
            super(itemView);
            mCommandTitle=itemView.findViewById(R.id.tvCommandTitle);
            mCommandDescription=itemView.findViewById(R.id.tvCommandDescription);
        }

        public TextView getmCommandDescription() {
            return mCommandDescription;
        }

        public TextView getmCommandTitle() {
            return mCommandTitle;
        }
    }
}
