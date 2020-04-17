package com.example.stayathome.utils;

import android.content.Context;
import android.provider.Settings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {


    public static String getDateFromMillis(long timestamp) {
        String date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        date = formatter.format(timestamp);
        System.out.println("Today is " + date);
        return date;
    }

    public static long getMillisFromDate(String dateFormat) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = formatter.parse(dateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


}
