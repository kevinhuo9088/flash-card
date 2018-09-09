package com.flashcard.newport.project.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
import com.flashcard.newport.project.filesystem.DeckDatabaseAdapter;

import java.util.ArrayList;

public class CardFrontFragment extends Fragment { //implements StudyInfoListener {

    private final static String TAG = "CardFrontFragment";
    private Deck mDeck;
    private Cursor mCursor;
    private CardFragmentActivity mAnimationListener;
    private DeckDatabaseAdapter mDeckDatabaseAdapter;

    public CardFrontFragment() {}

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
        readFromDatabase();
        Log.d(TAG, "OnCreate Called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mDeck.getQuestions().size() == 0)
            return inflater.inflate(R.layout.no_card, container, false);
        else
            return inflater.inflate(R.layout.fragment_card_front, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(mDeck.getQuestions().size() != 0) {
            if(isPreviousStudy()) {
                Log.d(TAG, "Made it to previous study");
                displayDialog();
                removeDeckFromList();
            }
            else {
                Log.d(TAG, "Made it to else statement");
                initiateQuestionText();
                initiateButton();
            }
        }
    }

    public void onContinueClick(int studyPosition, int gradePosition){
        continueFromLastQuestion(studyPosition, gradePosition);
        initiateButton();
    }

    public void onRestartClick() {
        initiateQuestionText();
        initiateButton();
    }

    private void initiateObjects(){
        DeckHolder deckInfo = (DeckHolder) getActivity().getApplication();
        ArrayList<Deck> deckList = deckInfo.getDeckList();
        int deckPosition = deckInfo.getDeckPosition();
        mDeck = deckList.get(deckPosition);
        mDeckDatabaseAdapter = new DeckDatabaseAdapter(getActivity());
    }

    private void readFromDatabase(){
        String tableName = DeckDatabaseAdapter.DeckHelper.STUDY_INFO_TABLE;
        String deckColumnName = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN;
        String studyPositionColumnName = DeckDatabaseAdapter.DeckHelper.STUDY_POSITION_COLUMN;
        String gradePositionColumn = DeckDatabaseAdapter.DeckHelper.GRADE_POSITION_COLUMN;
        String[] columns = {deckColumnName, studyPositionColumnName, gradePositionColumn};
        String selection = deckColumnName + " = ?";
        String[] selectionArgs = {mDeck.getName()};
        mCursor = mDeckDatabaseAdapter.tableQuery(tableName, columns, selection, selectionArgs, null, null, null, null);
    }

    public void removeDeckFromList(){
        String tableName = DeckDatabaseAdapter.DeckHelper.STUDY_INFO_TABLE;
        String whereClause = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN + " = ?";
        String[] whereArgs = {mDeck.getName()};
        mDeckDatabaseAdapter.tableRemove(tableName, whereClause, whereArgs);
    }

    private boolean isPreviousStudy(){
        return mCursor.getCount() > 0;
    }

    private int getStudyPosition(){
        String studyPositionColumnName = DeckDatabaseAdapter.DeckHelper.STUDY_POSITION_COLUMN;
        mCursor.moveToFirst();
        return mCursor.getInt(mCursor.getColumnIndex(studyPositionColumnName));
    }

    private int getGradePosition(){
        String gradePositionColumn = DeckDatabaseAdapter.DeckHelper.GRADE_POSITION_COLUMN;
        mCursor.moveToFirst();
        return mCursor.getInt(mCursor.getColumnIndex(gradePositionColumn));
    }
    private void displayDialog(){
        String deckColumnName = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN;
        String studyPositionColumnName = DeckDatabaseAdapter.DeckHelper.STUDY_POSITION_COLUMN;
        String gradePositionColumnName = DeckDatabaseAdapter.DeckHelper.GRADE_POSITION_COLUMN;

        Bundle bundle = new Bundle();
        String deckName = mDeck.getName();
        int studyPosition = getStudyPosition();
        int gradePosition = getGradePosition();
        mCursor.close();

        bundle.putString(deckColumnName, deckName);
        bundle.putInt(studyPositionColumnName, studyPosition);
        bundle.putInt(gradePositionColumnName, gradePosition);

        DialogFragment dialogFragment = new ContinueRestartDialog();
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.setCancelable(false);
        dialogFragment.show(getFragmentManager(), "dialog");
    }

    private void initiateQuestionText(){
        TextView questionTextView = (TextView)getView().findViewById(R.id.question);
        int position = StudyMode.getPosition();
        Log.d(TAG, "Studymode position: " + position);
        String questionText = mDeck.getQuestions().get(position);
        Log.d(TAG, "Question text: " + questionText);
        questionTextView.setText(questionText);
    }

    private void continueFromLastQuestion(int studyPosition, int gradePosition){
        TextView questionTextView = (TextView)getView().findViewById(R.id.question);
        String questionText = mDeck.getQuestions().get(studyPosition);
        StudyMode.setPosition(studyPosition);
        Grade.setNumCorrect(gradePosition);
        questionTextView.setText(questionText);
    }

    private void initiateButton(){
        Button answerButton;
        Button.OnClickListener onclickListener;

        onclickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                mAnimationListener.flipCardBack();
            }
        };

        answerButton = (Button)getView().findViewById(R.id.answer_button);
        answerButton.setOnClickListener(onclickListener);
    }
}
