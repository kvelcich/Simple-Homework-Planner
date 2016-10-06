package com.kevinvelcich.hwplanner.Fragments.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
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

import java.util.Calendar;

public class AssignmentDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_assignment, null);

        builder.setTitle(getArguments().getString("name"));

        TextView description = (TextView) view.findViewById(R.id.description);
        description.setText(getArguments().getString("description"));
        builder.setView(view)

                /* Adding the action buttons, with listeners. */
                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        TextView date = (TextView) view.findViewById(R.id.date);

        if (checkDate(getArguments().getInt("year"),
                getArguments().getInt("month"),
                getArguments().getInt("day"))) {
            date.setTextColor(Color.parseColor("#F44336"));
            date.setText("Due: Tomorrow");
        } else
            date.setText("Due on: " + Integer.toString(getArguments().getInt("month")) + "/" +
                    Integer.toString(getArguments().getInt("day")) + "/" +
                    Integer.toString(getArguments().getInt("year")));

        return builder.create();
    }

    private boolean checkDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        if (c.get(Calendar.YEAR) == year) {
            if (c.get(Calendar.MONTH) + 1 == month) {
                return (day - c.get(Calendar.DAY_OF_MONTH) == 1);
            } else if (month - c.get(Calendar.MONTH) == 2) {
                return (day == 1);
            }
        } else if (c.get(Calendar.MONTH) == 11 && c.get(Calendar.DAY_OF_MONTH) == 31) {
            return (month == 1 && day == 1);
        }
        return false;
    }
}
