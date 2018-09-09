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

public class CardAdapter extends BaseAdapter{

    private Deck mDeck;
    private static LayoutInflater mInflate;

    public CardAdapter(Activity editDeckTableActivity, Deck deck){
        mDeck = new Deck(deck);
        mInflate = (LayoutInflater)editDeckTableActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return mDeck.getAnswers().size();
    }

    @Override
    public Object getItem(int position){
        return mDeck.getAnswers();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rv;
        int max = 30;

        if(convertView == null) rv = mInflate.inflate(R.layout.edit_card_listview, null);
        else rv = convertView;

        int ql = mDeck.getQuestions().get(position).trim().length();
        String qt =  mDeck.getQuestions().get(position).trim();
        if(qt.contains("\n")) qt = qt.substring(0, qt.indexOf("\n")) + "...";
        else if(ql > max)
            qt =  mDeck.getQuestions().get(position).trim().substring(0, 29) + "...";

        TextView qtv = (TextView)rv.findViewById(R.id.edit_card);
        qtv.setText(qt);
        return rv;
    }
}
