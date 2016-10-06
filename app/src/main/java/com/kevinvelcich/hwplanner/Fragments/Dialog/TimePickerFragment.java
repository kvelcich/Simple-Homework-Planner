package com.kevinvelcich.hwplanner.Fragments.Dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;
import android.widget.Toast;

public class TimePickerFragment extends DialogFragment {
    Handler mHandler ;
    int mHour;
    int mMinute;

    public TimePickerFragment() {
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        /** Creating a bundle object to pass currently set time to the fragment */
        Bundle b = getArguments();

        /** Getting the hour of day from bundle */
        mHour = b.getInt("set_hour");

        /** Getting the minute of hour from bundle */
        mMinute = b.getInt("set_minute");

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                /** Displaying a short time message containing time set by Time picker dialog fragment */
                Toast.makeText(getActivity(), "Time set to: " + Integer.toString(mHour%13), Toast.LENGTH_SHORT).show();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("hour", hourOfDay);
                editor.putInt("minute", minute);
                editor.commit();
            }
        };

        /** Opening the TimePickerDialog window */
        return new TimePickerDialog(getActivity(), listener, mHour, mMinute, false);
    }
}
