package com.example.voiceassistent;

import android.graphics.Path;
import android.graphics.RadialGradient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AI {
    final static Map<String, String> map = new HashMap<>();
    public static String quest;

    static {
        map.put("(?i)[a-я\\s]*Привет[а-я\\s]*", "Привет");
        map.put("(?i)[\\w\\s]*Hello[\\w\\s]*", "Hello");

        map.put("(?i)([а-я\\s]*Как дела[\\s\\?]*)", "Не плохо");
        map.put("(?i)([а-я\\s]*How are you[\\s\\?]*)", "Good");

        map.put("(?i)([а-я\\s]*Чем занимаешься[\\s\\?]*)", "Отвечаю на вопросы");
        map.put("(?i)([а-я\\s]*What are you doing[\\s\\?]*)", "Nothing");

        map.put("(?i)([а-я\\s]*Ты кто[\\s\\?]*)", "Я - голосовой ассистент");
        map.put("(?i)([а-я\\s]*Who are you[\\s\\?]*)", "I`am a voice assistant");

        map.put("(?i)([а-я\\s]*Что ты умеешь[\\s\\?]*)", "Я умею отвечать на вопросы");
        map.put("(?i)([а-я\\s]*What can you do[\\s\\?]*)", "I know how to answer questions");

        map.put("(?i)([а-я\\s]*Какой сегодня день[\\s\\?]*)", Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "");
        map.put("(?i)([а-я\\s]*What today[\\s\\?]*)", Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "");

        map.put("(?i)([а-я\\s]*Который час[\\s\\?]*)", LocalDateTime.now(ZoneId.of("Europe/Moscow")).getHour() + "");
        map.put("(?i)([а-я\\s]*What time is it[\\s\\?]*)", LocalDateTime.now(ZoneId.of("Europe/Moscow")).getMinute() + "");

        map.put("(?i)([а-я\\s]*Какой день недели[\\s\\?]*)", getDayOfWeek(LocalDateTime.now(ZoneId.of("Europe/Moscow")).getDayOfWeek().toString()));
        map.put("(?i)([а-я\\s]*What day of the week[\\s\\?]*)", getDayOfWeek(LocalDateTime.now(ZoneId.of("Europe/Moscow")).getDayOfWeek().toString()));

        map.put("(?i)([а-я\\s]*Сколько дней до (\\d|[0-2]\\d|3[0-1])\\.(\\d|0\\d|1[0-2])\\.(19\\d\\d|2[\\d]{3})[\\s\\?]*)", quest + "");
        map.put("(?i)([а-я\\s]*How many days before (\\d|[0-2]\\d|3[0-1])\\.(\\d|0\\d|1[0-2])\\.(19\\d\\d|2[\\d]{3})[\\s\\?]*)", quest + " <--");
    }

    private static String getDayOfWeek(String day) {
        if ("SATURDAY".equals(day)) return "Суббота";
        if ("SUNDAY".equals(day)) return "Воскресенье";
        if ("MONDAY".equals(day)) return "Понедельник";
        if ("TUESDAY".equals(day)) return "Вторник";
        if ("WEDNESDAY".equals(day)) return "Среда";
        if ("THURSDAY".equals(day)) return "Четверг";
        if ("FRIDAY".equals(day)) return "Пятница";
        return "Думаю...";
    }

    private static String getDifferenceBetweenDates() {
        System.out.println(quest);
        return "2";
    }

    protected static String getAnswer(String question) {
        quest = question;

        Optional<String> answer = map.entrySet()
                .stream()
                .filter(entry -> Pattern.matches(entry.getKey(), question))
                .map(Map.Entry::getValue).findFirst();

        return (answer.isPresent()) ? answer.get() : "...";
    }
}
