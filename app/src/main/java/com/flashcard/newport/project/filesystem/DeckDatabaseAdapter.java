package com.flashcard.newport.project.filesystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DeckDatabaseAdapter {

    private DeckHelper mHelper;

    private final static String TAG = "DeckDatabaseAdapter";

    public DeckDatabaseAdapter(Context context) {
        mHelper = DeckHelper.getInstance(context);
    }

    public long tableInsert(String deckTable, String hackColumn, ContentValues contentValues) {
        Table table = new Table(deckTable);
        return table.insert(hackColumn, contentValues);
    }

    public Cursor tableQuery(String deckTable, String[] columns, String selection, String[] selectionArgs,
                             String groupBy, String having, String orderBy, String limit) {
        Table table = new Table(deckTable);
        return table.select(columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public long tableReplace(String deckTable, String hackColumn, ContentValues contentValues) {
        Table table = new Table(deckTable);
        return table.replace(hackColumn, contentValues);
    }

    public int tableRemove(String deckTable, String whereClause, String[] whereArgs) {
        Table table = new Table(deckTable);
        return table.delete(whereClause, whereArgs);
    }

    public Cursor tableRawQuery(String sql, String[] selectionArgs){
        Table table = new Table();
        return table.rawQuery(sql, selectionArgs);
    }

    public static class DeckHelper extends SQLiteOpenHelper {
        private static DeckHelper sInstance;

        private final static String TAG = "DeckHelper";

        public final static String DATABASE_NAME = "FlashCards.db";
        public final static String GRADE_TABLE = "Grades";
        public final static String STUDY_INFO_TABLE = "StudyInfo";
        public final static String UID_COLUMN = "_id";
        public final static String DECK_NAME_COLUMN = "Deck_Name";
        public final static String STUDY_POSITION_COLUMN = "Study_Position";
        public final static String GRADE_COLUMN = "Grade";
        public final static String GRADE_POSITION_COLUMN = "Grade_position";
        public final static String REMAINING_QUESTIONS_COLUMN = "Remaining_Questions";
        public final static String STUDY_MODE_COLUMN = "Study_Mode";
        public final static String DATE_COLUMN = "Date";
        private final static String DROP_STUDY_INFO_TABLE = "DROP_TABLE IF EXISTS " + STUDY_INFO_TABLE;
        private final static String DROP_GRADE_TABLE = "DROP_TABLE IF EXISTS " + GRADE_TABLE;
        private final static int DATABASE_VERSION = 1;

        private final static String CREATE_GRADE_TABLE = "CREATE TABLE "+ GRADE_TABLE + " ( " +
                "" + UID_COLUMN + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "" + DECK_NAME_COLUMN + " TEXT NOT NULL, " +
                "" + GRADE_COLUMN + " REAL NOT NULL, " +
                "" + DATE_COLUMN + " DATE DEFAULT (datetime('now', 'localtime'))" +
                ");";

        private final static String CREATE_STUDY_INFO_TABLE = "CREATE TABLE " + STUDY_INFO_TABLE + " ( " +
                "" + UID_COLUMN + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "" + DECK_NAME_COLUMN + " TEXT NOT NULL UNIQUE, " +
                "" + STUDY_POSITION_COLUMN + " INTEGER DEFAULT Null, " +
                "" + REMAINING_QUESTIONS_COLUMN + " TEXT DEFAULT Null, " +
                "" + GRADE_POSITION_COLUMN + " INTEGER NOT NULL, " +
                "" + STUDY_MODE_COLUMN + " INTEGER NOT NULL " +
                ");";

        public static synchronized DeckHelper getInstance(Context context){
            if (sInstance == null) {
                sInstance = new DeckHelper(context.getApplicationContext());
            }
            return sInstance;
        }

        private DeckHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(final SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_STUDY_INFO_TABLE);
                db.execSQL(CREATE_GRADE_TABLE);
            }
            catch (SQLException ex){
                ex.getMessage();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_STUDY_INFO_TABLE);
                db.execSQL(DROP_GRADE_TABLE);
            } catch (SQLException ex) {
                ex.getMessage();
            }
            onCreate(db);
        }
    }

    private class Table {

        private String mDatabaseTable;

        private Table(){}
        private Table(String databaseTable) {
            mDatabaseTable = databaseTable;
        }

        public long insert(final String hackColumn, final ContentValues contentValues) {
            final SQLiteDatabase db = mHelper.getWritableDatabase();
            ExecutorService executor = Executors.newFixedThreadPool(4);
            Callable<Long> callable = new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return db.insert(mDatabaseTable, hackColumn, contentValues);
                }
            };
            Future<Long> f = executor.submit(callable);

            while(true) {
                try {
                    if(f.isDone()) {return f.get();}
                } catch (Exception ex) {ex.getMessage();}
            }
        }

        private Cursor select(final String[] columns, final String selection, final String[] selectionArgs,
                              final String groupBy, final String having, final String orderBy, final String limit) {
            final SQLiteDatabase db = mHelper.getWritableDatabase();
            ExecutorService executor = Executors.newFixedThreadPool(4);
            Callable<Cursor> callable = new Callable<Cursor>() {
                @Override
                public Cursor call() throws Exception {
                    return db.query(mDatabaseTable, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
                }
            };
            Future<Cursor> f = executor.submit(callable);

            while(true) {
                try {
                    if(f.isDone()) {return f.get();}
                } catch (Exception ex) {ex.getMessage();}
            }
        }

        private long replace(final String hackColumn, final ContentValues contentValues) {
            final SQLiteDatabase db = mHelper.getWritableDatabase();
            ExecutorService executor = Executors.newFixedThreadPool(4);
            Callable<Long> callable = new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return db.replace(mDatabaseTable, hackColumn, contentValues);
                }
            };
            Future<Long> f = executor.submit(callable);
            while(true) {
                try {
                    if(f.isDone()) { return f.get();}
                } catch (Exception ex) {ex.getMessage();}
            }
        }

        private int delete(final String whereClause, final String[] whereArgs){
            final SQLiteDatabase db = mHelper.getWritableDatabase();
            ExecutorService executor = Executors.newFixedThreadPool(4);
            Callable<Integer> callable = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return db.delete(mDatabaseTable, whereClause, whereArgs);
                }
            };
            Future<Integer> f = executor.submit(callable);
            while(true) {
                try {
                    if(f.isDone()) {return f.get(); }
                } catch (Exception ex) {ex.getMessage();}
            }
        }

        private Cursor rawQuery(final String sql, final String[] selectionArgs){
            final SQLiteDatabase db = mHelper.getWritableDatabase();

            ExecutorService executor = Executors.newFixedThreadPool(4);
            Callable<Cursor> callable = new Callable<Cursor>() {
                @Override
                public Cursor call() throws Exception {
                    return db.rawQuery(sql, selectionArgs); }
            };
            Future<Cursor> f = executor.submit(callable);
            while(true) {
                try {
                    if(f.isDone()) { return f.get();}
                } catch (Exception ex) {ex.getMessage();}
            }

        }
    }
}
