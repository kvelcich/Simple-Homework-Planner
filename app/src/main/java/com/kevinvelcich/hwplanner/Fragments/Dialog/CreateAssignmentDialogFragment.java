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
import android.widget.Toast;

import com.kevinvelcich.hwplanner.Activity.MainActivity;
import com.kevinvelcich.hwplanner.Classes.Assignment;
import com.kevinvelcich.hwplanner.Database.DatabaseAssignment;
import com.kevinvelcich.hwplanner.Database.DatabaseClass;
import com.kevinvelcich.hwplanner.Fragments.assignmentsFragment;
import com.kevinvelcich.hwplanner.R;

public class CreateAssignmentDialogFragment extends DialogFragment  {

    private onCreateInteractionListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_createassignment, null);
        builder.setView(view)

                /* Adding the action buttons, with listeners. */
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editTitle = (EditText) view.findViewById(R.id.assignmentName);
                        if (editTitle.getText().length() != 0) {
                            DatabaseClass dbHandler = new DatabaseClass(getActivity().getApplicationContext(), null, null, 1);
                            //if (dbHandler))
                            //Create Assignment
                            EditText editDescription = (EditText) view.findViewById(R.id.assignmentDescription);
                            Assignment assignment = new Assignment(getArguments().getString("parent_class"), editTitle.getText().toString(), editDescription.getText().toString());
                            mListener.createAssignmentClicked(assignment);
                        } else
                            Toast.makeText(getActivity(), "Assignments names must have at least one character.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CreateAssignmentDialogFragment.this.getDialog().cancel();
                        // Cancels ...
                    }
                });

        builder.setTitle("Create a class");

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (onCreateInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMainFragmentInteractionListener");
        }
    }

    public interface onCreateInteractionListener {
        void createAssignmentClicked(Assignment assignment);
    }
}
