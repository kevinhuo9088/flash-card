package com.flashcard.newport.project.filesystem;

import com.flashcard.newport.project.deck.Deck;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;


public class DeckReader implements Callable<Object> {

    private final static String TAG = "DeckReader";
    private File mDeckFile;

    public DeckReader(String deckFile) { mDeckFile = new File(deckFile); }

    public ArrayList<Deck> call(){
        try{
            return readFromStorage();
        } catch (EOFException ex){
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException ex){ ex.getMessage();
            return null;
        }
    }

    public ArrayList<Deck> readFromStorage() throws IOException, ClassNotFoundException {
        ObjectInputStream myObject;
        FileInputStream  myFile;
        BufferedInputStream myInput;
        ArrayList<Deck> dl;

        myFile = new FileInputStream(mDeckFile);
        myInput = new BufferedInputStream(myFile);
        myObject = new ObjectInputStream(myInput);
        dl = new ArrayList<>((ArrayList<Deck>) myObject.readObject());
        myObject.close();
        myFile.close();
        return dl;
    }
    public void initiateStorage() throws IOException{
        mDeckFile.createNewFile();
    }

}

