package com.moh.clinicalguideline.data;

import androidx.room.TypeConverter;

import java.util.Date;
import java.util.UUID;

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
    public static String UuidToString(UUID uuid)
    {
        return uuid == null?null:uuid.toString();
    }

    @TypeConverter
    public static UUID UuidfromString(String strUuid)
    {
        return strUuid == null?null:UUID.fromString(strUuid);
    }
}
