package com.flashcard.newport.project;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.flashcard.newport.project.deck.Deck;
import com.flashcard.newport.project.deck.DeckHolder;
import com.flashcard.newport.project.deck.Grade;
import com.flashcard.newport.project.deck.StudyMode;
import com.flashcard.newport.project.filesystem.DeckDatabaseAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class DeckScoreActivity extends Activity {

    private final static String TAG = "DeckScoreActivity";
    private Deck mDeck;
    private DeckDatabaseAdapter mdb;
    private double mGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deck_score_activity);
        initiateObjects();
        displayGrade();
        writeGradeToStorage();
        reset();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void toOptionsActivity(View v){
        Intent it = new Intent(this, OptionsActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
    }

    private void initiateObjects(){
        DeckHolder dh = (DeckHolder)getApplication();
        ArrayList<Deck> deckList = dh.getDeckList();
        int dp = dh.getDeckPosition();
        mDeck = deckList.get(dp);
        mdb = new DeckDatabaseAdapter(this);
    }

    private void displayGrade(){
        TextView gp = (TextView)findViewById(R.id.deck_percentage);
        TextView gs = (TextView)findViewById(R.id.deck_size);
        int ds = mDeck.getQuestions().size();

        mGrade = Grade.calculateGrade(ds);
        String pc = String.format("%.1f", mGrade) + "%";
        String gc = String.valueOf(Grade.getNumCorrect()) + "/" + String.valueOf(ds);

        gp.setText(pc);
        gs.setText(gc);
    }

    private void writeGradeToStorage(){
        ContentValues cv = new ContentValues();
        DateFormat df = new SimpleDateFormat("MM/dd HH:mm:ss", Locale.ENGLISH);
        Date d = new Date();
        String tableName = DeckDatabaseAdapter.DeckHelper.GRADE_TABLE;
        String dColumn = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN;
        String gColumn = DeckDatabaseAdapter.DeckHelper.GRADE_COLUMN;
        String dtColumn = DeckDatabaseAdapter.DeckHelper.DATE_COLUMN;
        cv.put(dColumn, mDeck.getName());
        cv.put(gColumn, mGrade);
        cv.put(dtColumn, df.format(d));
        mdb.tableInsert(tableName, null, cv);
    }

    private void reset(){
        StudyMode.resetPosition();
        Grade.resetCorrect();
    }


}
