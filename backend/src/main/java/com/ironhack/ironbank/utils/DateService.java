package com.ironhack.ironbank.utils;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static java.time.temporal.ChronoField.*;

@Service
public class DateService {

    public static int getDiffYears(Instant lateDate) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        Date date = parseInstant(lateDate);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);

        return calendar.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
    }

    public static int getDiffMonths(Instant lateDate) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        Date date = parseInstant(lateDate);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);

        return calendar.get(Calendar.MONTH) - calendar2.get(Calendar.MONTH);
    }

    public static Date parseInstant(Instant instant) {
        return Date.from(instant.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Instant parseDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toInstant();
    }
}
