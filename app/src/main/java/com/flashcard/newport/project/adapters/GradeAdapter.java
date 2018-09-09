package com.flashcard.newport.project.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.flashcard.newport.project.R;
import com.flashcard.newport.project.filesystem.DeckDatabaseAdapter;


public class GradeAdapter extends CursorAdapter {

    private final static String TAG = "GradeAdapter";

    public GradeAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.grade_listview_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String gCol = DeckDatabaseAdapter.DeckHelper.GRADE_COLUMN;
        String dtCol = DeckDatabaseAdapter.DeckHelper.DATE_COLUMN;
        TextView gtv = (TextView)view.findViewById(R.id.grade_text);
        TextView dtv = (TextView)view.findViewById(R.id.date_text);
        double gd = cursor.getDouble(cursor.getColumnIndex(gCol));
        String tx = cursor.getString(cursor.getColumnIndex(dtCol));
        gtv.setText(String.format("%.1f", gd) + "%");
        dtv.setText(tx);
    }

}