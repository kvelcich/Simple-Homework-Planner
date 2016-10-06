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
import com.kevinvelcich.hwplanner.Database.DatabaseAssignment;
import com.kevinvelcich.hwplanner.R;

public class EditAssignmentDialogFragment extends DialogFragment {

    private onEditListener mListener;

    EditText mEdit;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_editassignment, null);
        final EditText mEdit = (EditText)view.findViewById(R.id.assignmentName);
        final EditText mEditDescription = (EditText)view.findViewById(R.id.assignmentDescription);
        final EditText mEditDay = (EditText)view.findViewById(R.id.day);
        final EditText mEditMonth= (EditText)view.findViewById(R.id.month);
        final EditText mEditYear = (EditText)view.findViewById(R.id.year);
        mEdit.setText(getArguments().getString("name"));
        mEditDescription.setText(getArguments().getString("description"));
        mEditDay.setText(Integer.toString(getArguments().getInt("day")));
        mEditMonth.setText(Integer.toString(getArguments().getInt("month")));
        mEditYear.setText(Integer.toString(getArguments().getInt("year")));
        builder.setView(view)

                /* Adding the action buttons, with listeners. */
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        DatabaseAssignment db = new DatabaseAssignment(getActivity(), null, null, 1);
                        if (!mEdit.getText().toString().equals(getArguments().getString("name"))) {
                            db.changeName(getArguments().getString("name"),
                                    mEdit.getText().toString());
                        } if (!mEditDescription.getText().toString().equals(getArguments().getString("description"))) {
                            db.changeDescription(getArguments().getString("description"),
                                    mEditDescription.getText().toString());
                        } if (!mEditDay.getText().toString().equals(Integer.toString(getArguments().getInt("day")))) {
                            db.changeDay(getArguments().getString("name"),
                                    Integer.parseInt(mEditDay.getText().toString()));
                        } if (!mEditMonth.getText().toString().equals(Integer.toString(getArguments().getInt("month")))) {
                            db.changeMonth(getArguments().getString("name"),
                                    Integer.parseInt(mEditMonth.getText().toString()));
                        } if (!mEditYear.getText().toString().equals(Integer.toString(getArguments().getInt("year")))) {
                            db.changeYear(getArguments().getString("name"),
                                    Integer.parseInt(mEditYear.getText().toString()));
                        }

                        db.close();
                        mListener.editAssignmentMethod(0);
                        EditAssignmentDialogFragment.this.getDialog().dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditAssignmentDialogFragment.this.getDialog().cancel();
                        // Cancels ...
                    }
                });

        builder.setTitle("Edit Assignment");

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
        void editAssignmentMethod(int c);
    }
}
