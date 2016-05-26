package com.group16.seeyaapp.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Andrea on 16/04/16.
 * Utility class to convert between string and Date objects,
 * based on the date and time formats used in the app and on the server.
 */
public final class DateHelper {
    final static String DATEFORMAT = "yyyy-MM-dd";
    final static String TIMEFORMAT = "HH:mm:ss";
    final static String FULL_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Converts a Date object to a string that only shows the date-part (i.e. without the time)
     * @param date Date object to convert
     * @return a string that only shows the date-part (i.e. without the time)
     */
    public static String dateToDateOnlyString(Date date) {
        DateFormat formatter = new SimpleDateFormat(DATEFORMAT);
        return formatter.format(date);
    }

    /**
     * Converts a Date object to a string that only shows the time-part (i.e. without the date)
     * @param time Date object to convert
     * @return a string that only shows the time-part (i.e. without the date)
     */
    public static String dateToTimeOnlyString(Date time) {
        DateFormat formatter = new SimpleDateFormat(TIMEFORMAT);
        return formatter.format(time);
    }

    /**
     * Converts a string with a date to a Date object according to the date-format used in the app.
     * @param dateStr The string with date to convert
     * @return Date object
     * @throws ParseException
     */
    public static Date stringDateToDate(String dateStr) throws ParseException {
        DateFormat format = new SimpleDateFormat(DATEFORMAT);
        return format.parse(dateStr);
    }

    /**
     * Converts a string with a time point to a Date object according to the time-format
     * used in the app.
     * @param timeStr The string with time to convert
     * @return Date object
     * @throws ParseException
     */
    public static Date stringTimeToDate(String timeStr) throws ParseException {
        DateFormat format = new SimpleDateFormat(TIMEFORMAT);
        return format.parse(timeStr);
    }

    /**
     * Converts a string with date and time information to a Date object
     * @param dateStr String with date and time information to convert
     * @return Date object
     * @throws ParseException
     */
    public static Date completeStringDateToDate(String dateStr) throws ParseException {
        DateFormat format = new SimpleDateFormat(FULL_DATEFORMAT);
        return format.parse(dateStr);
    }

    /**
     * Converts a Date object to a String with both date and time information.
     * @param date The Date object to convert
     * @return String with both date and time information
     */
    public static String completeDateToString(Date date) {
        DateFormat formatter = new SimpleDateFormat(FULL_DATEFORMAT);
        return formatter.format(date);
    }

    /**
     * Creates a string representing a time point according to the format: HH:mm:ss.
     * @param hourOfDay the hour
     * @param minute the minute
     * @param second the second
     * @return string representing a time point according to the format: HH:mm:ss
     */
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

    /**
     * Creates a string representing a date in the format: yy-mm-dd
     * @param year
     * @param month
     * @param day
     * @return string representing a date in the format: yy-mm-dd
     */
    public static String formatDate(int year, int month, int day) {
        return new StringBuilder().append(year)
                .append("-").append(month + 1).append("-").append(day).toString();
    }

    /**
     * Returns a new Date that contains the same date as the input parameter, but with the time
     * part set to zero. Can be used to compare dates where only differences in at least a day are
     * important.
     * @param inDate The Date object with the date to create the output Date with
     * @return Date with the time part set to zero
     */
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
