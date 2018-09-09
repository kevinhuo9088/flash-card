package com.flashcard.newport.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.flashcard.newport.project.deck.Consts;
import com.flashcard.newport.project.deck.Deck;
import com.flashcard.newport.project.deck.DeckHolder;
import com.flashcard.newport.project.filesystem.DeckWriter;

public class NewDeckActivity extends Activity {
    private final static String TAG = "NewDeckActivity";
    private DeckHolder mDeckInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.new_deck_activity);
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

    public void createDeck(View V){
        if(deckNameExists(getDeckName()))
            Toast.makeText(this, Consts.DECK_NAME_EXISTS, Toast.LENGTH_SHORT).show();

        else if(deckCharacterLimit())
            Toast.makeText(this, Consts.DECK_CHARACTER_LIMIT, Toast.LENGTH_SHORT).show();
        else {
            storeDeckName(getDeckName());
            initiateMainActivityIntent();
        }

    }

    private void initiateObjects(){
        mDeckInfo = (DeckHolder)getApplication();
    }

    private String getDeckName(){
        EditText et = (EditText) findViewById(R.id.new_deck_text);
        return et.getText().toString();
    }

    private boolean deckNameExists(String userDeckName){
        for(Deck deck : mDeckInfo.getDeckList()){
            String fn = deck.getName();
            if (userDeckName.matches(fn))
                return true;
        }
        return false;
    }

    private boolean deckCharacterLimit(){
        return getDeckName().length() > 35;
    }

    private void initiateMainActivityIntent(){
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
    }
    private void storeDeckName(String deckName){
        DeckWriter dw;
        Thread t1;

        mDeckInfo.setDeck(deckName);
        mDeckInfo.getDeckList().add(mDeckInfo.getDeck());

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