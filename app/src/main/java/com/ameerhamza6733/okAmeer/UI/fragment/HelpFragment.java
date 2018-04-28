package com.ameerhamza6733.okAmeer.UI.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameerhamza6733.okAmeer.R;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;


public class HelpFragment extends Fragment {

    private String[] mCommandtitle;
    private String[] mCommandDir;
    private ArrayList<Card> cards;


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
        cards = new ArrayList<Card>();

        for (int i = 0; i < mCommandtitle.length && i < mCommandDir.length; i++) {

            Card card = new Card(getActivity());

            CardHeader header = new CardHeader(getActivity());
            header.setTitle(mCommandtitle[i]);

            card.addCardHeader(header);
            card.setTitle(mCommandDir[i]);
            cards.add(card);
        }
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        CardListView listView = (CardListView) view.findViewById(R.id.my_help_list);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }

        return view;
    }


}
