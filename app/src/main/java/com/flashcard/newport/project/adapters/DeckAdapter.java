package com.flashcard.newport.project.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flashcard.newport.project.R;
import com.flashcard.newport.project.deck.Deck;

import java.util.ArrayList;

public class DeckAdapter extends BaseAdapter {

    private final static String TAG = "DeckAdapter";
    ArrayList<Deck> mDeckList;
    ArrayList<String> mDecks;
    ArrayList<Double> mGrades;
    private static LayoutInflater mInflate;

    public DeckAdapter(Activity mainActivity, ArrayList<Deck> deckList, ArrayList<String> decks, ArrayList<Double> grades){
        mDeckList = new ArrayList<>(deckList);
        mDecks = new ArrayList<>(decks);
        mGrades = new ArrayList<>(grades);
        mInflate = (LayoutInflater)mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return mDeckList.size();
    }

    @Override
    public Object getItem(int position){
        return mDeckList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rv;
        if(convertView == null) rv = mInflate.inflate(R.layout.main_activity_listview, null);
        else rv = convertView;

        TextView dtv = (TextView)rv.findViewById(R.id.deck_name);
        TextView ntv = (TextView)rv.findViewById(R.id.number_of_cards);

        String tx = mDeckList.get(position).getName();
        int gtl = mDeckList.get(position).getQuestions().size();
        dtv.setText(tx);
        ntv.setText("Cards: " + String.valueOf(gtl));
        return rv;
    }

}
