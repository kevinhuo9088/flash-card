package com.flashcard.newport.project.deck;

import android.app.Application;

import java.util.ArrayList;

public class DeckHolder  extends Application {

    private final static String TAG = "DeckHolder";
    private ArrayList<Deck> mDeckList;
    private Deck mDeck;
    private int mDeckPosition;
    private String mQuestion, mAnswer;

    @Override
    public void onCreate(){
        super.onCreate();
        initiateMainObjects();
    }

    public void setDeck(String deckName){
        mDeck = new Deck(deckName);
    }

    public Deck getDeck(){
        return mDeck;
    }

    public void setDeckPosition(int position){
        mDeckPosition = position;
    }

    public int getDeckPosition(){
        return mDeckPosition;
    }

    public void setDeckList(ArrayList<Deck> deckList){
        mDeckList = deckList;
    }

    public ArrayList<Deck> getDeckList(){
        return mDeckList;
    }

    public void setQuestion(String question){mQuestion = question; }

    public String getQuestion(){ return mQuestion;}

    public void setAnswer(String answer){mAnswer = answer;}

    public String getAnswer(){ return mAnswer; }



    private void initiateMainObjects(){
        mDeckList = new ArrayList<Deck>();
    }


}
