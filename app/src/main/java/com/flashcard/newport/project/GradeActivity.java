package com.flashcard.newport.project;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.flashcard.newport.project.adapters.GradeAdapter;
import com.flashcard.newport.project.deck.Deck;
import com.flashcard.newport.project.deck.DeckHolder;
import com.flashcard.newport.project.filesystem.DeckDatabaseAdapter;


public class GradeActivity extends Activity {

    private final static String TAG = "EditDeckActivity";
    private Cursor mCursor;
    private String mDeckName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initiateObjects();
        readGradeFromStorage();
        if(isGradeForDeck()) {
            setContentView(R.layout.grades_activity);
            initiateListAdapter();
        }
        else
            setContentView(R.layout.no_grade);
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiateObjects(){
        DeckHolder dh = (DeckHolder)getApplication();
        Deck dk = dh.getDeckList().get(dh.getDeckPosition());
        mDeckName = dk.getName();
    }

    private boolean isGradeForDeck(){
        return mCursor.getCount() != 0;
    }

    private void readGradeFromStorage(){

        String name = DeckDatabaseAdapter.DeckHelper.GRADE_TABLE;

        String idC = DeckDatabaseAdapter.DeckHelper.UID_COLUMN;
        String dC = DeckDatabaseAdapter.DeckHelper.DECK_NAME_COLUMN;
        String gC = DeckDatabaseAdapter.DeckHelper.GRADE_COLUMN;
        String dtC = DeckDatabaseAdapter.DeckHelper.DATE_COLUMN;
        String[] columns = {idC, gC, dtC};
        String sel = dC + " = ?";
        String[] selArgs = {mDeckName};
        String orderBy = dtC + " DESC";
        DeckDatabaseAdapter db = new DeckDatabaseAdapter(this);
        mCursor = db.tableQuery(name, columns, sel, selArgs, null, null, orderBy, "50");
    }

    private void initiateListAdapter(){
        ListView lv = (ListView)findViewById(R.id.grade_listview);
        GradeAdapter ga = new GradeAdapter(this, mCursor);
        lv.setAdapter(ga);
    }
}
