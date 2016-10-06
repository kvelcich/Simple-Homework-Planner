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
import com.kevinvelcich.hwplanner.Classes.*;
import com.kevinvelcich.hwplanner.Classes.Class;
import com.kevinvelcich.hwplanner.Database.DatabaseClass;
import com.kevinvelcich.hwplanner.Fragments.assignmentsFragment;
import com.kevinvelcich.hwplanner.R;

public class ColorDialogFragment extends DialogFragment {

    String colors[] = {"Red", "Pink", "Purple", "Blue", "Cyan", "Teal", "Green", "Yellow", "Orange"};
    private onCreateInteractionListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle("Choose a theme color")
           .setItems(colors, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                   DatabaseClass db = new DatabaseClass(getActivity().getApplicationContext(),
                           null, null, 1);
                   db.addClass(new Class(getArguments().getString("name"), 0, which));
                   db.close();
                   mListener.createClicked();
               }
           });
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
        void createClicked();
    }
}