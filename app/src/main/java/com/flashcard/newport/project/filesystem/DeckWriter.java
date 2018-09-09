package com.flashcard.newport.project.filesystem;

import com.flashcard.newport.project.deck.Deck;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class DeckWriter implements Runnable {

    private final static String TAG = "StudyWriter";
    private ArrayList<Deck> mDeckList;
    private File mFile;

    public DeckWriter(ArrayList<Deck> dl, String fileName){
        mDeckList = new ArrayList<>(dl);
        mFile = new File(fileName);
    }

    @Override
    public void run(){
        try {
            writeToStorage();
        } catch (Exception e) { e.getMessage();}
    }

    public void writeToStorage() throws IOException {
        FileOutputStream myFile;
        ObjectOutputStream myObject;

        myFile = new FileOutputStream(mFile);
        myObject = new ObjectOutputStream(myFile);
        myObject.writeObject(mDeckList);
        myObject.flush();
        myObject.close();
        myFile.close();
    }
}
