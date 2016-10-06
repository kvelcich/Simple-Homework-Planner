package com.kevinvelcich.hwplanner.Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kevinvelcich.hwplanner.*;
import com.kevinvelcich.hwplanner.Activity.MainActivity;
import com.kevinvelcich.hwplanner.Adapters.classListAdapter;
import com.kevinvelcich.hwplanner.Classes.Class;
import com.kevinvelcich.hwplanner.Database.DatabaseClass;
import com.kevinvelcich.hwplanner.Fragments.Dialog.EditClassDialogFragment;

import java.util.ArrayList;

public class classFragment extends Fragment implements classListAdapter.AdapterCallback {
    public static final String COLUMN_NAME = "classname";
    public static final String COLUMN_QUANTITY = "quantity";

    Cursor cursor;
    private classListAdapter adapter;
    private static assignmentCall mCall;

    public static classFragment newInstance() {
        return new classFragment();
    }

    @Override
    public void onMethodCallback(int call, String pClass) {
        switch (call) {
            case 0:
                DatabaseClass db = new DatabaseClass(getActivity(), null, null, 1);
                adapter.changeCursor(db.getClasses());
                adapter.notifyDataSetChanged();
                break;
            case 1:
                mCall.assignmentCallMethod(pClass);
                break;
            case 2:
                DialogFragment dialogFragment = new EditClassDialogFragment();
                Bundle args = new Bundle();
                args.putString("name", pClass);
                dialogFragment.setArguments(args);
                dialogFragment.show(getActivity().getFragmentManager(), "dialog");
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assigment, container, false);

        DatabaseClass dbHandler = new DatabaseClass(getActivity(), null, null, 1);
        cursor = dbHandler.getClasses();

        adapter = new classListAdapter(getActivity().getApplicationContext(), cursor, 0, this);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCall = (assignmentCall) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMainFragmentInteractionListener");
        }
    }

    public interface assignmentCall {
        void assignmentCallMethod(String pClass);
    }
}