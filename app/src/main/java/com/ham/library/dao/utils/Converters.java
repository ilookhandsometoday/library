package com.ham.library.dao.utils;

import android.util.Log;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date dateFromString(String value) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm",
                        Locale.forLanguageTag("ru"));
        try {
            return value == null ? null : simpleDateFormat.parse(value);
        }
        catch (ParseException e) {
            Log.e("UTILS", "dateFromString: error parsing" + value, e);
        }
        return null;
    }
}
