package com.ericsospedra.listshoppingfirebase.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    private static final String INPUT_DATE_FORMAT = "dd-MM-yyyy";

    public static Date createDateFromString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(INPUT_DATE_FORMAT, Locale.US);
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
