package com.github.mpagconestoga.mad_a01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class ConfirmationDialog extends AppCompatDialogFragment {

    /*
     *    METHOD      :     onCreateDialog
     *    DESCRIPTION :     Set the title, message, and button text for the subit task
     *                      confirmation dialogue
     *    PARAMETERS  :     Bundle savedInstanceState
     *    RETURNS     :     VOID
     * */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm")
                .setMessage("Create Task?")

                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        CreateSubtasksFragment fragment = (CreateSubtasksFragment)getParentFragment();

                        fragment.createTaskAndFinish();

                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
