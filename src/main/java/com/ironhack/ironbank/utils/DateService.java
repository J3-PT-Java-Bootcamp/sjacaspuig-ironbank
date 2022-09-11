package com.ironhack.ironbank.utils;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static java.time.temporal.ChronoField.*;

@Service
public class DateService {

    public static int getDiffYears(Instant lateDate) {
        Instant now = Instant.now();
        int diff = now.get(YEAR) - lateDate.get(YEAR);
        if (
                now.get(MONTH_OF_YEAR) > lateDate.get(MONTH_OF_YEAR) ||
                (now.get(MONTH_OF_YEAR) == lateDate.get(MONTH_OF_YEAR) && now.get(DAY_OF_MONTH) > lateDate.get(DAY_OF_MONTH))
        ) {
            diff--;
        }
        return diff;
    }

    public static int getDiffMonths(Instant lateDate) {
        Instant now = Instant.now();
        int diffYear = now.get(YEAR) - lateDate.get(YEAR);
        return diffYear * 12 + now.get(MONTH_OF_YEAR) - lateDate.get(MONTH_OF_YEAR);
    }

    public static Date parseInstant(Instant instant) {
        return Date.from(instant.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Instant parseDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toInstant();
    }
}