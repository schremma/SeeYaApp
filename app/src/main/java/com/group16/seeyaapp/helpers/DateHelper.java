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
    final static String TIMEFORMAT = "hh:mm:ss";
    final static String FULL_DATEFORMAT = "yyyy-MM-dd hh:mm:ss";

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
}
