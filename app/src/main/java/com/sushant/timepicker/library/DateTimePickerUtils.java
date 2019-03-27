package com.sushant.timepicker.library;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public final class DateTimePickerUtils {

    private DateTimePickerUtils() {

    }

    public static List<String> getHours() {
        List<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            NumberFormat numberFormat = new DecimalFormat("00");
            hours.add(String.valueOf(numberFormat.format(i)));
        }
        return hours;
    }

    public static List<String> getMinutes() {
        List<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i = i + 5) {
            NumberFormat numberFormat = new DecimalFormat("00");
            minutes.add(String.valueOf(numberFormat.format(i)));
        }
        return minutes;
    }

    public static List<String> getDays(String month) {
        HashMap<String, List<String>> hashMap = monthAndDaysMapper();
        return hashMap.get(month);
    }


    public static List<String> getMonths() {
        List<String> months = new ArrayList<>();
        months.add("JAN");
        months.add("FEB");
        months.add("MAR");
        months.add("APR");
        months.add("MAY");
        months.add("JUN");
        months.add("JUL");
        months.add("AUG");
        months.add("SEP");
        months.add("OCT");
        months.add("NOV");
        months.add("DEC");
        return months;
    }

    private static HashMap<String, List<String>> monthAndDaysMapper() {
        HashMap<String, List<String>> hashMap = new HashMap<>();
        hashMap.put("JAN", getList(32));
        hashMap.put("FEB", getList(29));
        hashMap.put("MAR", getList(32));
        hashMap.put("APR", getList(31));
        hashMap.put("MAY", getList(32));
        hashMap.put("JUN", getList(31));
        hashMap.put("JUL", getList(32));
        hashMap.put("AUG", getList(32));
        hashMap.put("SEP", getList(31));
        hashMap.put("OCT", getList(32));
        hashMap.put("NOV", getList(31));
        hashMap.put("DEC", getList(32));

        return hashMap;

    }

    private static HashMap<String, Integer> getMonthInt() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("JAN", 1);
        hashMap.put("FEB", 2);
        hashMap.put("MAR", 3);
        hashMap.put("APR", 4);
        hashMap.put("MAY", 5);
        hashMap.put("JUN", 6);
        hashMap.put("JUL", 7);
        hashMap.put("AUG", 8);
        hashMap.put("SEP", 9);
        hashMap.put("OCT", 10);
        hashMap.put("NOV", 11);
        hashMap.put("DEC", 12);
        return hashMap;
    }

    private static List<String> getList(int size) {
        List<String> monthList = new ArrayList<>();
        for (int i = 1; i < size; i++) {
            monthList.add(String.valueOf(i));
        }

        return monthList;
    }

    public static String getMonthByNumber(int number) {
        HashMap<String, Integer> hashMap = getMonthInt();
        for (String key : hashMap.keySet()) {
            int value = hashMap.get(key);
            if (value == (number)) {
                return key;
            }

        }
        return null;
    }

    public static String getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
    }

    public static String getCurrentMinutes() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.get(Calendar.MINUTE));
    }

    public static String getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return new SimpleDateFormat("MMM").format(calendar.getTime()).toUpperCase();

    }

    private static String getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    public static String getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static DateTimeFragment.Ranges getClosestIndex(String value) {
        NumberFormat f = new DecimalFormat("00");


        DateTimeFragment.Ranges ranges = new DateTimeFragment.Ranges();


        int intValue = Integer.parseInt(value);

        if (intValue % 5 == 0) {
            ranges.value = f.format(Integer.parseInt(value));
            return ranges;
        } else {
            int newValue = intValue + (5 - intValue % 5);
            if (newValue > 55) {
                ranges.value = "00";
                ranges.offset = 1;
                return ranges;
            }


            ranges.value = String.valueOf(f.format(newValue));
            return ranges;


        }

    }

    public static String getFormattedDate(String month, String day) {
        return getCurrentYear() + "-" + getMonthByName(month) + "-" + day;
    }

    public static String getFormattedTime(String hour, String minute) {
        return hour.trim() + ":" + minute.trim();
    }

    private static Integer getMonthByName(String monthName) {
        return getMonthInt().get(monthName);
    }
}
