package com.example.voiceassistent.Holiday;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Calendar;

public class DateTest {
    String question;

    // dd MMMM yyyy
    @Test
    public void getDateddMMMMyyyy() {
        question = "Какие праздники 20 марта 2022";
        assertEquals("20 марта 2022", Date.getDate(question));

        question = "Расскажи пожалуйста какой Праздник 31 декабря 2021";
        assertEquals("31 декабря 2021", Date.getDate(question));

        question = "Праздники 29 февраля 2000";
        assertEquals("29 февраля 2000", Date.getDate(question));

        question = "праздник 01 апреля 2021";
        assertEquals("1 апреля 2021", Date.getDate(question));

        question = "Напомни праздник 31 марта 2023";
        assertEquals("31 марта 2023", Date.getDate(question));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDateddMMMMyyyyWrongDay1() {
        question = "Расскажи пожалуйста какой Праздник 32 декабря 2021";
        Date.getDate(question);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDateddMMMMyyyyWrongDay2() {
        question = "Расскажи пожалуйста какой Праздник 29 февраля 2021";
        Date.getDate(question);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDateddMMMMyyyyWrongMonth() {
        question = "Расскажи пожалуйста какой Праздник 31 дикобря 2021";
        Date.getDate(question);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDateddMMMMyyyyWrongYear() {
        question = "Расскажи пожалуйста какой Праздник 31 декабря 1000";
        Date.getDate(question);
    }

    // dd.mm.yyyy
    @Test
    public void getDateddmmyyyy() {
        question = "Какие праздники 20.03.2022";
        assertEquals("20 марта 2022", Date.getDate(question));

        question = "Расскажи пожалуйста какой Праздник 31.12.2021";
        assertEquals("31 декабря 2021", Date.getDate(question));

        question = "Праздники 29.2.2000";
        assertEquals("29 февраля 2000", Date.getDate(question));

        question = "праздник 01.04.2021";
        assertEquals("1 апреля 2021", Date.getDate(question));

        question = "Напомни праздник 31.3.2023";
        assertEquals("31 марта 2023", Date.getDate(question));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDateddmmyyyyWrongDay1() {
        question = "Расскажи пожалуйста какой Праздник 32.12.2021";
        Date.getDate(question);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDateddmmyyyyWrongDay2() {
        question = "Расскажи пожалуйста какой Праздник 29.02.2021";
        Date.getDate(question);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDateddmmyyyyWrongMonth() {
        question = "Расскажи пожалуйста какой Праздник 31.13.2021";
        Date.getDate(question);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDateddmmyyyyWrongYear() {
        question = "Расскажи пожалуйста какой Праздник 31.12.1000";
        Date.getDate(question);
    }

    // WWWW
    @Test
    public void getDateWWWW() {
        int curDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String curMonth = getStringOfMonth(Calendar.getInstance().get(Calendar.MONTH) + 1);
        int curYear = Calendar.getInstance().get(Calendar.YEAR);

        question = "праздник вчера";
        assertEquals((curDay - 1) + " " + curMonth + " " + curYear, Date.getDate(question));

        question = "праздник завтра";
        assertEquals((curDay + 1) + " " + curMonth + " " + curYear, Date.getDate(question));

        question = "праздники послезавтра";
        assertEquals((curDay + 2) + " " + curMonth + " " + curYear, Date.getDate(question));

        question = "праздник сегодня";
        assertEquals(curDay + " " + curMonth + " " + curYear, Date.getDate(question));
    }

    private static String getStringOfMonth(int month) {
        if (month == 1) return "января";
        if (month == 2) return "февраля";
        if (month == 3) return "марта";
        if (month == 4) return "апреля";
        if (month == 5) return "мая";
        if (month == 6) return "июня";
        if (month == 7) return "июля";
        if (month == 8) return "августа";
        if (month == 9) return "сентября";
        if (month == 10) return "октября";
        if (month == 11) return "ноября";
        return "декабря";
    }
}