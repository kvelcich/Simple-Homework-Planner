package com.kevinvelcich.hwplanner.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kevinvelcich.hwplanner.Adapters.assignmentListAdapter;
import com.kevinvelcich.hwplanner.Classes.*;
import com.kevinvelcich.hwplanner.Database.DatabaseAssignment;
import com.kevinvelcich.hwplanner.Fragments.Dialog.AssignmentDialogFragment;
import com.kevinvelcich.hwplanner.Fragments.Dialog.EditAssignmentDialogFragment;
import com.kevinvelcich.hwplanner.R;

public class assignmentsFragment extends Fragment implements assignmentListAdapter.AdapterCallback {

    Cursor cursor;
    private assignmentListAdapter adapter;

    public static assignmentsFragment newInstance(String pClass) {
        assignmentsFragment fragment = new assignmentsFragment();
        Bundle args = new Bundle();
        args.putString("parent_class", pClass);
        fragment.setArguments(args);
        return fragment;
    }

    DialogFragment editDialogFragment = null;

    @Override
    public void onMethodCallback(int call, Assignment a) {
        switch (call) {
            case 0:
                DatabaseAssignment db = new DatabaseAssignment(getActivity(), null, null, 1);
                adapter.changeCursor(db.getAssignments(a.getParentClass()));
                adapter.notifyDataSetChanged();
                break;
            case 1:
                DialogFragment dialogFragment = new AssignmentDialogFragment();
                Bundle args = new Bundle();
                args.putString("name", a.getName());
                args.putString("description", a.getDescription());
                args.putInt("year", a.getYear());
                args.putInt("month", a.getMonth());
                args.putInt("day", a.getDay());
                dialogFragment.setArguments(args);
                dialogFragment.show(getActivity().getFragmentManager(), "dialog");
                break;
            case 2:
                if (editDialogFragment == null) {
                    editDialogFragment = new EditAssignmentDialogFragment();
                    Bundle editargs = new Bundle();
                    editargs.putString("name", a.getName());
                    editargs.putString("description", a.getDescription());
                    editargs.putInt("year", a.getYear());
                    editargs.putInt("month", a.getMonth());
                    editargs.putInt("day", a.getDay());
                    editDialogFragment.setArguments(editargs);
                    editDialogFragment.show(getActivity().getFragmentManager(), "dialog");
                }
                break;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assigment, container, false);

        DatabaseAssignment dbHandler = new DatabaseAssignment(getActivity(), null, null, 1);
        cursor = dbHandler.getAssignments(getArguments().getString("parent_class"));
        adapter = new assignmentListAdapter(getActivity().getApplicationContext(), cursor, 0, this);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        return view;
    }
}
