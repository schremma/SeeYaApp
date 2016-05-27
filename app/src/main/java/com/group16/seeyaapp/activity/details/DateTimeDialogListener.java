package com.group16.seeyaapp.activity.details;

/**
 * Created by Andrea on 19/05/16.
 * Receives selected date and time values from dialogs displaying controls for the user
 * to pick a specific date or time.
 */
public interface DateTimeDialogListener {
    void onDateSelected(int year, int month, int day);
    void onTimeSelected(int hour, int minute,int second);
}
