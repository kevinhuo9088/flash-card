package com.flashcard.newport.project.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DeleteDeckDialog extends DialogFragment {

    private AlertDialog.OnClickListener mNoListener, mYesListener;
    private DeleteDeckListener mCallback;

    public DeleteDeckDialog(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mCallback = (DeleteDeckListener)getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement StudyInfoListener interface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initiateAlertListeners();
        String title = "Delete Deck";
        String message = "Are you sure you want to delete this deck?";
        String no = "No";
        String yes = "Yes";

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(yes, mYesListener)
                .setNegativeButton(no, mNoListener)
                .create();
    }

    private void initiateAlertListeners() {
        mNoListener = new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        };

        mYesListener = new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                mCallback.deleteDeck();
            }
        };
    }
}

