package com.flashcard.newport.project.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.flashcard.newport.project.R;
import com.flashcard.newport.project.deck.Deck;
import com.flashcard.newport.project.deck.DeckHolder;
import com.flashcard.newport.project.deck.Grade;
import com.flashcard.newport.project.deck.StudyMode;

import java.util.ArrayList;

public class CardBackFragment extends Fragment {

    private final static String TAG = "CardBackFragment";
    private Deck mDeck;
    private CardFragmentActivity mAnimationListener;

    public CardBackFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mAnimationListener = (CardFragmentActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IAnimation");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initiateObjects();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card_back, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        initiateButtons();
        initiateAnswer();
    }


    private void initiateObjects(){
        DeckHolder deckInfo = (DeckHolder) getActivity().getApplication();
        ArrayList<Deck> deckList = deckInfo.getDeckList();
        int deckP = deckInfo.getDeckPosition();
        mDeck = deckList.get(deckP);
    }

    private void initiateAnswer(){
        TextView questionTextView = (TextView)getView().findViewById(R.id.answer);
        int p = StudyMode.getPosition();
        String text = mDeck.getAnswers().get(p);
        questionTextView.setText(text);
    }

    private void initiateButtons(){
        Button cButton, iButton, dButton;

        Button.OnClickListener cListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                StudyMode.increasePosition();
                Grade.increaseNumCorrect();
                mAnimationListener.correctAnswer();
            }
        };

        Button.OnClickListener iListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                StudyMode.increasePosition();
                mAnimationListener.incorrectAnswer();
            }
        };

        Button.OnClickListener dListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                mAnimationListener.complete();
            }
        };

        cButton = (Button)getView().findViewById(R.id.correct_button);
        iButton = (Button)getView().findViewById(R.id.incorrect_button);
        dButton = (Button)getView().findViewById(R.id.complete_button);

        cButton.setOnClickListener(cListener);
        iButton.setOnClickListener(iListener);
        dButton.setOnClickListener(dListener);
    }


}
