package com.flashcard.newport.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
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

public class CreateCardActivity  extends Activity {
    private final static String TAG = "CreateCardActivity";
    private DeckHolder mDeckInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.create_card_activity);
        initiateObjects();
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

    public void addCard(View V) {
        if (!isEditBoxesFilled())
            Toast.makeText(this, Consts.MESSAGE, Toast.LENGTH_SHORT).show();
        else {
            Deck deck = mDeckInfo.getDeckList().get(mDeckInfo.getDeckPosition());
            deck.setQuestion(getQuestionEdit());
            deck.setAnswer(getAnswerEdit());
            Log.d(TAG, mDeckInfo.getDeckList().get(mDeckInfo.getDeckPosition()).toString());
            clearEditBoxes();
        }
    }
    public void writeToList(View v) {
        writeToStorage();
        initiateOptionsIntent();
    }
    public void keyBoard(View v){
        if(findViewById(R.id.question_edit).isFocused() || findViewById(R.id.answer_edit).isFocused())
            hideKeyBoard(v);
    }
    private void initiateObjects(){
        mDeckInfo = (DeckHolder)getApplication();
    }
    private void hideKeyBoard(View view){
        InputMethodManager im =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private boolean isEditBoxesFilled() {
        String qText = getQuestionEdit();
        String aText = getAnswerEdit();

        if(qText.matches("") || aText.matches(""))
            return false;
        else
            return true;
    }
    private void initiateOptionsIntent() {
        Intent it = new Intent(this, OptionsActivity.class);
        startActivity(it);
    }
    private String getQuestionEdit() {
        EditText questionEdit = (EditText) findViewById(R.id.question_edit);
        return questionEdit.getText().toString();
    }
    private String getAnswerEdit() {
        EditText aEdit = (EditText) findViewById(R.id.answer_edit);
        return aEdit.getText().toString();
    }

    private void clearEditBoxes() {
        EditText qEdit = (EditText)findViewById(R.id.question_edit);
        qEdit.setText("");
        EditText aEdit = (EditText)findViewById(R.id.answer_edit);
        aEdit.setText("");
    }

    private void writeToStorage(){
        DeckWriter dw;
        Thread t1;

        dw = new DeckWriter(mDeckInfo.getDeckList(), Consts.DECK_FILEPATH);
        t1 = new Thread(dw);
        t1.start();
        try {
            t1.join();
        }
        catch (Exception ex){
            ex.getMessage();
        }
    }
}
