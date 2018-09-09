package com.flashcard.newport.project.deck;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Deck implements Parcelable, Serializable {

    private final static String TAG = "DECK";

    private String mName;
    private ArrayList<String> mQuestions;
    private ArrayList<String> mAnswers;
    private int mCardPosition;

    public Deck(){
        mQuestions = new ArrayList<>();
        mAnswers = new ArrayList<>();
    }

    public Deck(Deck deck){
        mName = deck.mName;
        mQuestions = new ArrayList<>(deck.getQuestions());
        mAnswers = new ArrayList<>(deck.getAnswers());
    }

    public Deck(String deckName){
        mName = deckName;
        mQuestions = new ArrayList<>();
        mAnswers = new ArrayList<>();
    }

    public Deck(Parcel p){
        mName = p.readString();
        mQuestions = (ArrayList<String>)p.readArrayList(mQuestions.getClass().getClassLoader());
        mAnswers = (ArrayList<String>)p.readArrayList(mQuestions.getClass().getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeArray(mQuestions.toArray());
        dest.writeArray(mAnswers.toArray());
    }

    public static final Creator<Deck> CREATOR = new Creator<Deck>() {
        @Override
        public Deck createFromParcel(Parcel source) {
            return new Deck(source);
        }

        @Override
        public Deck[] newArray(int size) {
            return new Deck[size];
        }
    };

    public String getName(){
        return mName;
    }

    public void setName(String name){
        mName = name;
    }

    public void setQuestion(String question){ mQuestions.add(question); }

    public ArrayList<String> getQuestions(){ return mQuestions; }

    public void setAnswer(String answer){ mAnswers.add(answer); }

    public ArrayList<String> getAnswers(){ return mAnswers; }

    public void setCardPosition(int position){ mCardPosition = position; }

    public int getCardPosition(){ return mCardPosition; }

}
