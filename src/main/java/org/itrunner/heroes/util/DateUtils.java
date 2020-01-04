package org.itrunner.heroes.util;

import org.quartz.TimeOfDay;

import java.time.*;
import java.util.Date;

public final class DateUtils {

    private DateUtils() {
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate toLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static TimeOfDay toTimeOfDay(LocalTime localTime) {
        return TimeOfDay.hourAndMinuteOfDay(localTime.getHour(), localTime.getMinute());
    }
}
