package com.example.voiceassistent.Holiday;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.IOException;

public class ParsingHtmlServiceTest {
    String expected;

    @Test
    public void getHoliday() throws IOException {
        expected = "Праздники 13 апреля 2022: \n1. Всемирный день рок-н-ролла\n2. День мецената и благотворителя в России";
        assertEquals(expected, ParsingHtmlService.getHoliday("13 апреля 2022"));

        expected = "Праздник 1 мая 2022: \n1. Праздник весны и труда";
        assertEquals(expected, ParsingHtmlService.getHoliday("1 мая 2022"));

        expected = "2 мая 2022 нет известных мне праздников.";
        assertEquals(expected, ParsingHtmlService.getHoliday("2 мая 2022"));
    }
}