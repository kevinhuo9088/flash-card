package com.flashcard.newport.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.flashcard.newport.project.adapters.CardAdapter;
import com.flashcard.newport.project.deck.Deck;
import com.flashcard.newport.project.deck.DeckHolder;

import java.util.ArrayList;


public class EditDeckTableActivity extends Activity {

    private final static String TAG = "EditDeckActivity";
    private DeckHolder mDeckInfo;
    private Deck mDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initiateObjects();
        if(mDeck.getQuestions().size() == 0)
            setContentView(R.layout.no_edit_card);
        else {
            setContentView(R.layout.edit_deck_table_activity);
            initiateListAdapter(mDeck);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigateUp () {
        onBackPressed();
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

    private void initiateObjects(){
        mDeckInfo = (DeckHolder)getApplication();
        ArrayList<Deck> dl = mDeckInfo.getDeckList();
        int p = mDeckInfo.getDeckPosition();
        mDeck = dl.get(p);
    }

    private void initiateListAdapter(Deck deck){
        AdapterView.OnItemClickListener onCL = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(parent.getContext(), EditCardActivity.class);

                int p = mDeckInfo.getDeckPosition();
                Deck deck = mDeckInfo.getDeckList().get(p);
                deck.setCardPosition(p);

                ArrayList<String> qs = new ArrayList<>(deck.getQuestions());
                ArrayList<String> as = new ArrayList<>(deck.getAnswers());
                String q = qs.get(position);
                String a = as.get(position);
                mDeckInfo.setQuestion(q);
                mDeckInfo.setAnswer(a);
                startActivity(it);
            }
        };

        ListView lv = (ListView) findViewById(R.id.deck_listview);
        CardAdapter c = new CardAdapter(this, deck);
        lv.setAdapter(c);
        lv.setOnItemClickListener(onCL);
    }

}
