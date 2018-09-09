package com.flashcard.newport.project.fragments;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.flashcard.newport.project.filesystem.DeckDatabaseAdapter;

public class ContinueRestartDialog extends DialogFragment {

    private AlertDialog.OnClickListener mRestartListener, mContinueListener;
    private StudyInfoListener mCallback;
    private int mStudyPosition, mGradePosition;

    public ContinueRestartDialog(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        String studyPositionColumnName = DeckDatabaseAdapter.DeckHelper.STUDY_POSITION_COLUMN;
        String gradePositionColumnName = DeckDatabaseAdapter.DeckHelper.GRADE_POSITION_COLUMN;

        mStudyPosition = bundle.getInt(studyPositionColumnName);
        mGradePosition = bundle.getInt(gradePositionColumnName);

        try {
            mCallback = (StudyInfoListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement StudyInfoListener interface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        initiateAlertListeners();
        String title = "Previous Study Session";
        String message = "You exited early from previous study.  Do you wish to continue or restart?";
        String restart = "Restart";
        String start = "Continue";

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(start, mContinueListener)
                .setNegativeButton(restart, mRestartListener)
                .create();
    }

    private void initiateAlertListeners(){
        mRestartListener = new AlertDialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1){
                mCallback.onRestartClick();
            }
        };

        mContinueListener = new AlertDialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1){
                mCallback.onContinueClick(mStudyPosition, mGradePosition);
            }
        };
    }
}

