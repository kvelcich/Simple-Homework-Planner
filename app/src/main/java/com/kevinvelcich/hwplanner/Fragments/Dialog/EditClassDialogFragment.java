package com.kevinvelcich.hwplanner.Fragments.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kevinvelcich.hwplanner.Classes.Assignment;
import com.kevinvelcich.hwplanner.Database.DatabaseAssignment;
import com.kevinvelcich.hwplanner.Database.DatabaseClass;
import com.kevinvelcich.hwplanner.R;

import java.util.PriorityQueue;

public class EditClassDialogFragment extends DialogFragment {

    private onEditListener mListener;

    EditText mEdit;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_createclass, null);
        final EditText mEdit = (EditText) view.findViewById(R.id.className);
        mEdit.setText(getArguments().getString("name"));
        builder.setView(view)

                /* Adding the action buttons, with listeners. */
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (!mEdit.getText().toString().equals(getArguments().getString("name"))) {
                            DatabaseClass db =
                                    new DatabaseClass(getActivity().getApplicationContext(), null, null, 1);
                            db.changeName(getArguments().getString("name"), mEdit.getText().toString());
                            db.close();

                            DatabaseAssignment dbA =
                                    new DatabaseAssignment(getActivity().getApplicationContext(), null, null, 1);
                            dbA.changeClass(getArguments().getString("name"), mEdit.getText().toString());
                            dbA.close();

                            Toast.makeText(getActivity(),
                                    getArguments().getString("name") + " changed to: " + mEdit.getText().toString(),
                                    Toast.LENGTH_SHORT).show();

                        }
                        mListener.editClassMethod();
                        EditClassDialogFragment.this.getDialog().dismiss();
                    }
                })
                .setNeutralButton("Change Color", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogFragment dialogFragment = new EditColorDialogFragment();
                        Bundle args = new Bundle();
                        args.putString("name", getArguments().getString("name"));
                        dialogFragment.setArguments(args);
                        dialogFragment.show(getFragmentManager(), "dialog");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditClassDialogFragment.this.getDialog().cancel();
                        // Cancels ...
                    }
                });

        builder.setTitle("Edit Class");

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (onEditListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMainFragmentInteractionListener");
        }
    }

    public interface onEditListener {
        void editClassMethod();
    }
}
