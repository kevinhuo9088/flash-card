package com.flashcard.newport.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.flashcard.newport.project.adapters.DeckAdapter;
import com.flashcard.newport.project.deck.Consts;
import com.flashcard.newport.project.deck.Deck;
import com.flashcard.newport.project.deck.DeckHolder;
import com.flashcard.newport.project.filesystem.DeckReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends Activity {

    private final static String TAG = "MainActivity";
    private ArrayList<String> mDecks;
    private ArrayList<Double> mGrades;
    private DeckHolder mDeckInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initiateObjects();
        readDeckFromFile();
        initiateListAdapter();
        switch (getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                break;
            case DisplayMetrics.DENSITY_HIGH:
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void newDeck(View V) {
        initiateNewDeckIntent();
    }
    private void initiateObjects() {
        mDeckInfo = (DeckHolder) this.getApplication();
        mDecks = new ArrayList<>();
        mGrades = new ArrayList<>();
    }
    private void initiateNewDeckIntent() {
        Intent it = new Intent(this, NewDeckActivity.class);
        startActivity(it);
    }
    private void initiateListAdapter() {
        AdapterView.OnItemClickListener onClickL = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(parent.getContext(), OptionsActivity.class);
                mDeckInfo.setDeckPosition(position);
                startActivity(it);
            }
        };

        ListView lv = (ListView) findViewById(R.id.deck_listview);
        DeckAdapter da = new DeckAdapter(this, mDeckInfo.getDeckList(), mDecks, mGrades);
        lv.setAdapter(da);
        lv.setOnItemClickListener(onClickL);
    }
     private void readDeckFromFile() {
        Consts.DECK_FILEPATH = getFilesDir() + "/" + Consts.DECK_FILENAME;
        DeckReader dr = new DeckReader(Consts.DECK_FILEPATH);
        try {
            dr.initiateStorage();
        }
        catch (IOException ex){
            ex.getMessage();
        }
        ArrayList<Deck> dl;
        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<Object> df = service.submit(dr);

        while (true) {
            try {
                if (df.isDone()) {
                    dl = new ArrayList<>((ArrayList<Deck>) df.get());
                    mDeckInfo.setDeckList(dl);
                    return;
                }
            } catch (Exception ex) {
                ex.getMessage();
            }
        }
    }
}

