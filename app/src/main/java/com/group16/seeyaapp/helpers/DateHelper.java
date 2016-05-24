package com.group16.seeyaapp.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Andrea on 16/04/16.
 */
public final class DateHelper {
    final static String DATEFORMAT = "yyyy-MM-dd";
    final static String TIMEFORMAT = "HH:mm:ss";
    final static String FULL_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String dateToDateOnlyString(Date date) {
        DateFormat formatter = new SimpleDateFormat(DATEFORMAT);
        return formatter.format(date);
    }

    public static String dateToTimeOnlyString(Date time) {
        DateFormat formatter = new SimpleDateFormat(TIMEFORMAT);
        return formatter.format(time);
    }

    public static Date stringDateToDate(String dateStr) throws ParseException {
        DateFormat format = new SimpleDateFormat(DATEFORMAT);
        return format.parse(dateStr);
    }

    public static Date stringTimeToDate(String timeStr) throws ParseException {
        DateFormat format = new SimpleDateFormat(TIMEFORMAT);
        return format.parse(timeStr);
    }

    public static Date completeStringDateToDate(String dateStr) throws ParseException {
        DateFormat format = new SimpleDateFormat(FULL_DATEFORMAT);
        return format.parse(dateStr);
    }

    public static String completeDateToString(Date date) {
        DateFormat formatter = new SimpleDateFormat(FULL_DATEFORMAT);
        return formatter.format(date);
    }

    public static String formatTime(int hourOfDay, int minute, int second) {
        StringBuilder sb = new StringBuilder(6);
        if (hourOfDay < 10) {
            sb.append('0');
        }
        sb.append(hourOfDay);
        sb.append(':');
        if (minute < 10) {
            sb.append('0');
        }
        sb.append(minute);
        sb.append(':');

        if (second < 10) {
            sb.append('0');
        }
        sb.append(second);

        return sb.toString();
    }

    public static String formatDate(int year, int month, int day) {
        return new StringBuilder().append(year)
                .append("-").append(month + 1).append("-").append(day).toString();
    }

    public static Date getZeroTimeDate(Date inDate) {
        Date res = inDate;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(inDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        res = calendar.getTime();

        return res;
    }
}
