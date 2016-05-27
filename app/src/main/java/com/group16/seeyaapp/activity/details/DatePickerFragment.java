package com.group16.seeyaapp.activity.details;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Andrea on 19/05/16.
 * Shows a calender for selecting a date as a dialog.
 * The displaying activity should implement DateTimeDialogListener.
 * The selected date is sent to the displaying view.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /**
     * OnDateListener callback method.
     * Sends the selected time to the displaying view, implementing DateTimeDialogListener.
     * @param view
     * @param year selected year
     * @param month selected month
     * @param day selected day
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        ((DateTimeDialogListener)getActivity()).onDateSelected(year,month,day);
    }
}