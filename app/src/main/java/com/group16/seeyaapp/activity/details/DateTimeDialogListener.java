package com.group16.seeyaapp.activity.details;

/**
 * Created by Andrea on 19/05/16.
 */
public interface DateTimeDialogListener {
    void onDateSelected(int year, int month, int day);
    void onTimeSelected(int hour, int minute,int second);
}
