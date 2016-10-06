package com.kevinvelcich.hwplanner.Fragments.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kevinvelcich.hwplanner.Activity.MainActivity;
import com.kevinvelcich.hwplanner.Classes.*;
import com.kevinvelcich.hwplanner.Classes.Class;
import com.kevinvelcich.hwplanner.Database.DatabaseClass;
import com.kevinvelcich.hwplanner.Fragments.assignmentsFragment;
import com.kevinvelcich.hwplanner.R;

public class CreateClassDialogFragment extends DialogFragment {

    EditText mEdit;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_createclass, null);
        builder.setView(view)

                /* Adding the action buttons, with listeners. */
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mEdit = (EditText) view.findViewById(R.id.className);
                        DatabaseClass dbHandler = new DatabaseClass(getActivity().getApplicationContext(), null, null, 1);

                        /* Checks if the user inputs anything into the text field. */
                        if (mEdit.getText().toString().length() == 0) {
                            /* Shows error that class is too short. */
                            Toast.makeText(getActivity(), "Classes must be at least one character", Toast.LENGTH_SHORT).show();
                        } else if (dbHandler.findClass(mEdit.getText().toString())) {
                            /* Error text that duplicate class. */
                            Toast.makeText(getActivity(), "There is already a class named \"" + mEdit.getText().toString() + "\"", Toast.LENGTH_SHORT).show();
                        } else {
                            /* Save the class in this else statement. */
                            DialogFragment colorDialogFragment = new ColorDialogFragment();
                            Bundle args = new Bundle();
                            args.putString("name", mEdit.getText().toString());
                            colorDialogFragment.setArguments(args);
                            colorDialogFragment.show(getActivity().getFragmentManager(), "dialog");
                        }
                        dbHandler.close();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CreateClassDialogFragment.this.getDialog().cancel();
                        // Cancels ...
                    }
                });

        builder.setTitle("Create a class");

        return builder.create();
    }
}
