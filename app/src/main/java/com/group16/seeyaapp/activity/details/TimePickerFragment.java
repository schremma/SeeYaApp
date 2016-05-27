package com.group16.seeyaapp.activity.details;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Andrea on 13/05/16.
 * Shows a clock for selecting a time point, in form of a dialog.
 * The displaying activity should implement DateTimeDialogListener.
 * The selected time is sent to the displaying view.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    /**
     * onTimeSet() callback method
     * Sends the selected time to the displaying view, implementing DateTimeDialogListener.
     * @param view TimePicker view
     * @param hourOfDay selected hour
     * @param minute selected minute
     */
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        ((DateTimeDialogListener)getActivity()).onTimeSelected(hourOfDay,minute, 0);
    }
}