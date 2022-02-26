package com.example.voiceassistent.Holiday;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Date {
    // return dd MMMM yyyy
    public static String getDate(String question) throws IllegalArgumentException{
        String date = question.replaceFirst("(?i)([а-яА-Я\\s]*праздник[и]?\\s)", "")
                .replaceAll("\\?", "");

        // dd MMMM yyyy
        Pattern cityPattern = Pattern.compile("(\\p{N}+)\\s(\\p{L}+)\\s(\\p{N}+)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = cityPattern.matcher(date);
        if (matcher.find()) {
            int day = Integer.parseInt(matcher.group(1));
            int month = getNumOfMonth(matcher.group(2));
            int year = Integer.parseInt(matcher.group(3));

            if (month == -1)
                throw new IllegalArgumentException("Не могу узнать дату");

            if (!isValidDate(day, month, year))
                throw new IllegalArgumentException("Не могу узнать праздники для данной даты");

            return day + " " + getStringOfMonth(month) + " " + year;
        }

        // dd.mm.yyyy
        Pattern pointPattern = Pattern.compile("(\\p{N}+)\\.(\\p{N}+)\\.(\\p{N}+)",
                Pattern.CASE_INSENSITIVE);
        Matcher pointMatcher = pointPattern.matcher(date);
        if (pointMatcher.find()) {
            int day = Integer.parseInt(pointMatcher.group(1));
            int month = Integer.parseInt(pointMatcher.group(2));
            int year = Integer.parseInt(pointMatcher.group(3));

            if (!isValidDate(day, month, year))
                throw new IllegalArgumentException("Не могу узнать праздники для данной даты");

            return day + " " + getStringOfMonth(month) + " " + year;
        }

        // вчера/сегодня/завтра/послезавтра
        Pattern wordPattern = Pattern.compile("(\\p{L}+)",
                Pattern.CASE_INSENSITIVE);
        Matcher wordMatcher = wordPattern.matcher(date);
        if (wordMatcher.find()) {
            String word = wordMatcher.group(1);
            int day = -1;
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int year = Calendar.getInstance().get(Calendar.YEAR);

            if (Pattern.matches("(?i)(сегодня)", word))
                day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            if (Pattern.matches("(?i)(вчера)", word))
                day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1;

            if (Pattern.matches("(?i)(завтра)", word))
                day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1;

            if (Pattern.matches("(?i)(послезавтра)", word))
                day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 2;

            if (day == -1)
                throw new IllegalArgumentException("Не могу узнать дату");

            if (!isValidDate(day, month, year))
                throw new IllegalArgumentException("Не могу узнать праздники для данной даты");

            return day + " " + getStringOfMonth(month) + " " + year;
        }

        throw new IllegalArgumentException("Не могу узнать дату");
    }

    public static int getNumOfMonth(String month) {
        if (Pattern.matches("(?i)(январ[ья])", month)) return 1;
        if (Pattern.matches("(?i)(феврал[ья])", month)) return 2;
        if (Pattern.matches("(?i)(март[а]?)", month)) return 3;
        if (Pattern.matches("(?i)(апрел[ья])", month)) return 4;
        if (Pattern.matches("(?i)(ма[йя])", month)) return 5;
        if (Pattern.matches("(?i)(июн[ья])", month)) return 6;
        if (Pattern.matches("(?i)(июл[ья])", month)) return 7;
        if (Pattern.matches("(?i)(август[а]?)", month)) return 8;
        if (Pattern.matches("(?i)(сентябр[ья])", month)) return 9;
        if (Pattern.matches("(?i)(октябр[ья])", month)) return 10;
        if (Pattern.matches("(?i)(ноябр[ья])", month)) return 11;
        if (Pattern.matches("(?i)(декабр[ья])", month)) return 12;

        return -1;
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

    // Валидный год: 1903 - 2025
    private static boolean isValidDate(int day, int month, int year) {
        if (month < 1 || month > 12 || day < 1 || day > 31 || year < 1903 || year > 2025)
            return false;

        // Високосный год
        if (year % 4 == 0) {
            if ((year % 100 != 0) || (year % 400 == 0))
                if (month == 2 && (day > 29))
                    return false;
        } else {
            if (month == 2 && day > 28)
                return false;
            if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30)
                return false;
        }

        return true;
    }
}
