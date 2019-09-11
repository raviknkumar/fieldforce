package com.example.fieldforce.helper;

import java.time.LocalDate;

public class DateUtils {
    public static LocalDate addDaysToLocalDate(LocalDate date, int days){
        if(days >= 0){
            return date.plusDays(days);
        }
        else
            return date.minusDays(Math.abs(days));
    }
}
