package com.example.voiceassistent;

import androidx.core.util.Consumer;

import com.example.voiceassistent.Forecast.ForecastToString;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AI {
    final static Map<String, String> map = new HashMap<>();

    static {
        map.put("(?i)[a-я\\s]*Привет[а-я\\s]*", "Привет");
        map.put("(?i)([а-я\\s]*Как дела[\\s\\?]*)", "Не плохо");
        map.put("(?i)([а-я\\s]*Чем занимаешься[\\s\\?]*)", "Отвечаю на вопросы");
        map.put("(?i)([а-я\\s]*Ты кто[\\s\\?]*)", "Я - голосовой ассистент");
        map.put("(?i)([а-я\\s]*Что ты умеешь[\\s\\?]*)", "Я умею отвечать на вопросы");
    }

    private static void getDynamicAnswer(String question, final Consumer<String> callback) {
        String answer = "...";

        if (Pattern.matches("(?i)([а-я\\s]*Какой сегодня день[\\s\\?]*)", question))
            answer = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "";

        if (Pattern.matches("(?i)([а-я\\s]*Который час[\\s\\?]*)", question))
            answer = LocalDateTime.now(ZoneId.of("Europe/Moscow")).getHour() + "";

        if (Pattern.matches("(?i)([а-я\\s]*Какой день недели[\\s\\?]*)", question))
            answer = getDayOfWeek();

        if (Pattern.matches("(?i)([а-я\\s]*Сколько дней до " +
                "(\\d|[0-2]\\d|3[0-1])\\.(\\d|0\\d|1[0-2])\\.(19\\d\\d|2[\\d]{3})[\\s\\?]*)", question))
            answer = getDifferenceBetweenDates(question) + "";

        if (Pattern.matches("(?i)(погода в городе [\\wа-яА-Я]+)", question))
            getWeather(question, weather -> callback.accept(weather));
        else
            callback.accept(answer);
    }

    protected static void getAnswer(String question, final Consumer<String> callback) {
        Optional<String> staticAnswer = map.entrySet()
                .stream()
                .filter(entry -> Pattern.matches(entry.getKey(), question))
                .map(Map.Entry::getValue).findFirst();

        if (staticAnswer.isPresent())
            callback.accept(staticAnswer.get());
        else
            getDynamicAnswer(question, answer -> callback.accept(answer));
    }

    private static void getWeather(String question, final Consumer<String> callback) {
        Pattern cityPattern = Pattern.compile("погода в городе (\\p{L}+)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = cityPattern.matcher(question);
        if (matcher.find()){
            String cityName = matcher.group(1);
            ForecastToString.getForecast(cityName, weather -> callback.accept(weather));
        }
    }

    private static String getDayOfWeek() {
        String day = LocalDateTime.now(ZoneId.of("Europe/Moscow")).getDayOfWeek().toString();

        if ("SATURDAY".equals(day)) return "Суббота";
        if ("SUNDAY".equals(day)) return "Воскресенье";
        if ("MONDAY".equals(day)) return "Понедельник";
        if ("TUESDAY".equals(day)) return "Вторник";
        if ("WEDNESDAY".equals(day)) return "Среда";
        if ("THURSDAY".equals(day)) return "Четверг";
        if ("FRIDAY".equals(day)) return "Пятница";

        return "...";
    }

    private static String getDifferenceBetweenDates(String question) {
        String[] d;

        for (String s : question.split("\\s")) {
            d = s.split("\\.");
            if (d.length == 3) {
                LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
                LocalDateTime date = LocalDateTime.of(Integer.parseInt(d[2]),
                        Integer.parseInt(d[1]), Integer.parseInt(d[0]), 0, 0);
                long diff = ChronoUnit.DAYS.between(now, date);

                if (diff < 0)
                    return "Дата уже прошла";
                if (diff == 0)
                    return "Осталось меньше дня";

                return "Дней до данной даты: " + diff;
            }
        }

        return "...";
    }
}
