package com.flashcard.newport.project;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.flashcard.newport.project.deck.Deck;
import com.flashcard.newport.project.deck.DeckHolder;
import com.flashcard.newport.project.deck.Grade;
import com.flashcard.newport.project.deck.StudyMode;
import com.flashcard.newport.project.filesystem.DeckDatabaseAdapter;
import com.flashcard.newport.project.fragments.CardBackFragment;
import com.flashcard.newport.project.fragments.CardFragmentActivity;
import com.flashcard.newport.project.fragments.CardFrontFragment;


public class StudyDeckActivity extends Activity implements FragmentManager.OnBackStackChangedListener, CardFragmentActivity {

    private final static String TAG = "StudyDeckActivity";
    private Handler mHandler;
    private Deck mDeck;

    public StudyDeckActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_deck_activity);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initiateObjects();

        if (savedInstanceState == null) {
            CardFrontFragment cardFrontFragment = new CardFrontFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, cardFrontFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                if(findViewById(R.id.no_card_layout) == null) {
                    storeStudyInfo();
                    reset();
                }
                else
                    reset();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackStackChanged() {
        invalidateOptionsMenu();
    }
    @Override
    public void onBackPressed(){
        if(findViewById(R.id.no_card_layout) == null) {
            storeStudyInfo();
            reset();
            super.onBackPressed();
        }
        else{
            reset();
            super.onBackPressed();
        }
    }
    @Override
    public void flipCardBack(){
        flipCardToBack();
    }

    @Override
    public void incorrectAnswer(){
        if(mDeck.getQuestions().size() > StudyMode.getPosition()) {
            flipCardToFront();
        }
        else{
            Intent it = new Intent(this, DeckScoreActivity.class);
            startActivity(it);
        }
    }

    @Override
    public void correctAnswer(){
        if(mDeck.getQuestions().size() > StudyMode.getPosition()) {
            flipCardToFront();
        }
        else{
            Intent it = new Intent(this, DeckScoreActivity.class);
            startActivity(it);
        }
    }
    @Override
    public void complete(){
        storeStudyInfo();
        reset();
        initiateOptionsIntent();
    }
    private void initiateObjects(){
        DeckHolder dh = (DeckHolder)getApplication();
        mHandler = new Handler();
        mDeck = new Deck(dh.getDeckList().get(dh.getDeckPosition()));
    }
    private void initiateOptionsIntent(){
        Intent it = new Intent(this, OptionsActivity.class);
        startActivity(it);
    }
    private void reset(){
        StudyMode.resetPosition();
        Grade.resetCorrect();
    }
    private void storeStudyInfo(){
        DeckDatabaseAdapter db = new DeckDatabaseAdapter(this);

        ContentValues cv = new ContentValues();
        String tName = DeckDatabaseAdapter.DeckHelper.STUDY_INFO_TABLE;
        String dCol = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN;
        String spCol = DeckDatabaseAdapter.DeckHelper.STUDY_POSITION_COLUMN;
        String qCol = DeckDatabaseAdapter.DeckHelper.REMAINING_QUESTIONS_COLUMN;
        String gCol = DeckDatabaseAdapter.DeckHelper.GRADE_POSITION_COLUMN;
        String sCol = DeckDatabaseAdapter.DeckHelper.STUDY_MODE_COLUMN;

        cv.put(dCol, mDeck.getName());
        cv.put(spCol, StudyMode.getPosition());
        cv.putNull(qCol);
        cv.put(gCol, Grade.getNumCorrect());
        cv.put(sCol, StudyMode.IN_ORDER_MODE);
        db.tableReplace(tName, spCol, cv);
    }

    private void flipCardToBack() {
        CardBackFragment cardBackFragment = new CardBackFragment();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out)
                .replace(R.id.container, cardBackFragment)
                .commit();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        });
    }
    private void flipCardToFront(){
        CardFrontFragment cardFrontFragment = new CardFrontFragment();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.container, cardFrontFragment)
                .commit();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        });
    }
}