package com.flashcard.newport.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.flashcard.newport.project.deck.Consts;
import com.flashcard.newport.project.deck.Deck;
import com.flashcard.newport.project.deck.DeckHolder;
import com.flashcard.newport.project.filesystem.DeckWriter;

import java.util.ArrayList;


public class EditCardActivity extends Activity {

    private final static String TAG = "EditCardActivity";
    private DeckHolder mDeckInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_card_activity);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initiateObjects();
        setQuestionTextBox();
        setAnswerTextBox();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void editCard(View v){
        if (!isEditBoxesFilled())
            Toast.makeText(this, Consts.MESSAGE, Toast.LENGTH_SHORT).show();
        else {
            writeToDeckList();
            writeToStorage();
            initiateOptionsIntent();
        }
    }
    public void deleteCard(View v){
        if (!isEditBoxesFilled())
            Toast.makeText(this, Consts.MESSAGE, Toast.LENGTH_SHORT).show();
        else {
            removeFromDeckList();
            writeToStorage();
            initiateOptionsIntent();
        }
    }
    private void removeFromDeckList(){
        int dp = mDeckInfo.getDeckPosition();
        Deck deck = mDeckInfo.getDeckList().get(dp);
        ArrayList<String> qs = deck.getQuestions();
        ArrayList<String> as = deck.getAnswers();

        int p = deck.getCardPosition();
        String q = getQuestionEdit();
        String a = getAnswerEdit();
        qs.remove(q);
        as.remove(a);
    }
    public void editKeyBoard(View v){
        if(findViewById(R.id.question_edit).isFocused() || findViewById(R.id.answer_edit).isFocused())
            hideKeyBoard(v);
    }
    private void initiateObjects(){
        mDeckInfo = (DeckHolder)getApplication();
    }

    private void setQuestionTextBox(){
        EditText qe = (EditText)findViewById(R.id.question_edit);
        String q = mDeckInfo.getQuestion();
        qe.setText(q);
    }

    private void setAnswerTextBox(){
        EditText qt = (EditText)findViewById(R.id.answer_edit);
        String a = mDeckInfo.getAnswer();
        qt.setText(a);
    }
    private void hideKeyBoard(View view){
        InputMethodManager im =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private boolean isEditBoxesFilled() {
        String qt = getQuestionEdit();
        String at = getAnswerEdit();

        if(qt.matches("") || at.matches(""))
            return false;
        else
            return true;
    }
    private String getQuestionEdit() {
        EditText qt = (EditText) findViewById(R.id.question_edit);
        return qt.getText().toString();

    }
    private String getAnswerEdit() {
        EditText at = (EditText)findViewById(R.id.answer_edit);
        return at.getText().toString();
    }
    private void writeToStorage(){
        DeckWriter dw;
        Thread t1;

        dw = new DeckWriter(mDeckInfo.getDeckList(), Consts.DECK_FILEPATH);
        t1 = new Thread(dw);
        t1.start();
    }
    private void writeToDeckList(){
        int dp = mDeckInfo.getDeckPosition();
        Deck deck = mDeckInfo.getDeckList().get(dp);
        ArrayList<String> qs = deck.getQuestions();
        ArrayList<String> as = deck.getAnswers();

        int p = deck.getCardPosition();
        String q = getQuestionEdit();
        String a = getAnswerEdit();

        qs.set(p, q);
        as.set(p, a);
    }
    private void initiateOptionsIntent() {
        Intent it = new Intent(this, EditDeckTableActivity.class);
        startActivity(it);
    }
}