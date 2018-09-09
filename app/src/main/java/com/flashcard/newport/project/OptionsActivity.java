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
import android.widget.TextView;

import com.flashcard.newport.project.adapters.OptionAdapter;
import com.flashcard.newport.project.deck.Consts;
import com.flashcard.newport.project.deck.Deck;
import com.flashcard.newport.project.deck.DeckHolder;
import com.flashcard.newport.project.filesystem.DeckDatabaseAdapter;
import com.flashcard.newport.project.filesystem.DeckReader;
import com.flashcard.newport.project.filesystem.DeckWriter;
import com.flashcard.newport.project.fragments.DeleteDeckDialog;
import com.flashcard.newport.project.fragments.DeleteDeckListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OptionsActivity extends Activity implements DeleteDeckListener {

    private final static String TAG = "OptionsActivity";
    private String mDeckName;
    private DeckHolder mDeckInfo;
    private ArrayList<String> mOptions;
    private ArrayList<Integer> mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.options_activity);
        initiateObjects();
        initiateListAdapter();
        setDeckName();
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

    @Override
    public void deleteDeck(){
        DeckReader dr = new DeckReader(Consts.DECK_FILEPATH);
        ArrayList<Deck> odl, dl;
        Intent it;
        DeckDatabaseAdapter db = new DeckDatabaseAdapter(this);
        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<Object> dkr = service.submit(dr);

        while(true) {
            try {
                if (dkr.isDone()) {
                    odl = new ArrayList<>((ArrayList<Deck>) dkr.get());
                    dl = new ArrayList<>(odl);
                    for(Deck deck : dl) {
                        if(deck.getName().equals(mDeckName)){
                            odl.remove(deck);
                            break;
                        }
                    }
                    DeckWriter dw = new DeckWriter(odl, Consts.DECK_FILEPATH);
                    Thread t1 = new Thread(dw);
                    t1.start();
                    try{
                        t1.join();
                    }
                    catch (Exception ex){
                        ex.getMessage();
                    }
                    break;
                }
            } catch (Exception ex) {
                ex.getMessage();
            }
        }

        String gt = DeckDatabaseAdapter.DeckHelper.GRADE_TABLE;
        String dCol = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN;
        String wh = dCol + " = ?";
        String[] whArgs = {mDeckName};
        db.tableRemove(gt, wh, whArgs);

        it = new Intent(this, MainActivity.class);
        startActivity(it);
    }

    void initiateObjects(){
        mDeckInfo = (DeckHolder)getApplication();
        Deck deck = mDeckInfo.getDeckList().get(mDeckInfo.getDeckPosition());
        mDeckName = deck.getName();

        mOptions = new ArrayList<>(Arrays.asList("Study Deck", "Edit Deck", "Add Card", "Grades", "Delete"));
        mImages = new ArrayList<>(Arrays.asList(R.drawable.study, R.drawable.ic_menu_edit, R.drawable.ic_menu_add, R.drawable.btn_check_off, R.drawable.ic_action_cancel));
    }

    private void initiateListAdapter(){

        AdapterView.OnItemClickListener onClickL = new AdapterView.OnItemClickListener() {
            Intent it;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        it = new Intent(parent.getContext(), StudyDeckActivity.class);
                        startActivity(it);
                        break;
                    case 1:
                        it = new Intent(parent.getContext(), EditDeckTableActivity.class);
                        startActivity(it);
                        break;
                    case 2:
                        it = new Intent(parent.getContext(), CreateCardActivity.class);
                        startActivity(it);
                        break;
                    case 3:
                        it = new Intent(parent.getContext(), GradeActivity.class);
                        startActivity(it);
                        break;
                    case 4:
                        DeleteDeckDialog deleteDeckDialog = new DeleteDeckDialog();
                        deleteDeckDialog.setCancelable(false);
                        deleteDeckDialog.show(getFragmentManager(), "dialog");
                }
            }
        };

        ListView lv = (ListView) findViewById(R.id.option_listview);
        OptionAdapter oa = new OptionAdapter(this, mOptions, mImages);
        lv.setAdapter(oa);
        lv.setOnItemClickListener(onClickL);
    }
    private void setDeckName(){
        TextView tv = (TextView) findViewById(R.id.deck_name);
        tv.setText(mDeckName);
    }
}
