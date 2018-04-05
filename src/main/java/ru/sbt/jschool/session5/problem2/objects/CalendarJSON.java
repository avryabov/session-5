package ru.sbt.jschool.session5.problem2.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 */
public class CalendarJSON extends DateJSON {
    @Override
    public StringBuilder json(Object obj, int tabs) {
        return super.json(((Calendar) obj).getTime(), tabs);
    }
}
