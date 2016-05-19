package com.group16.seeyaapp.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andrea on 16/04/16.
 */
public final class DateHelper {
    final static String DATEFORMAT = "yyyy-MM-dd";
    final static String TIMEFORMAT = "HH:mm:ss";
    final static String FULL_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String DateToDateOnlyString(Date date) {
        DateFormat formatter = new SimpleDateFormat(DATEFORMAT);
        return formatter.format(date);
    }

    public static String DateToTimeOnlyString(Date time) {
        DateFormat formatter = new SimpleDateFormat(TIMEFORMAT);
        return formatter.format(time);
    }

    public static Date StringDateToDate(String dateStr) throws ParseException {
        DateFormat format = new SimpleDateFormat(DATEFORMAT);
        return format.parse(dateStr);
    }

    public static Date StringTimeToDate(String timeStr) throws ParseException {
        DateFormat format = new SimpleDateFormat(TIMEFORMAT);
        return format.parse(timeStr);
    }

    public static Date CompleteStringDateToDate(String dateStr) throws ParseException {
        DateFormat format = new SimpleDateFormat(FULL_DATEFORMAT);
        return format.parse(dateStr);
    }

    public static String CompleteDateToString(Date date) {
        DateFormat formatter = new SimpleDateFormat(FULL_DATEFORMAT);
        return formatter.format(date);
    }

    public static String FormatTime(int hourOfDay, int minute, int second) {
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

    public static String FormatDate(int year,int month,int day) {
        return new StringBuilder().append(year)
                .append("-").append(month + 1).append("-").append(day).toString();
    }
}
