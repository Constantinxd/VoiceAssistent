package com.example.voiceassistent.Holiday;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ParsingHtmlService {
    private static final String URL = "https://mirkosmosa.ru/holiday/";

    public static String getHoliday(String date) throws IOException {
        String[] dates = date.split(" ");
        int count = 1;
        int day = Integer.parseInt(dates[0]);
        int month = Date.getNumOfMonth(dates[1]);
        int year = Integer.parseInt(dates[2]);
        StringBuilder answer = new StringBuilder();
        Document document = Jsoup.connect(URL + year).get();

        Elements elements = document.body().select("#holiday_calend > div").get(month - 1).
                select("div.holiday_month > div.next_phase").get(day - 1).
                select("ul > li");

        if (elements.size() != 0) {
            if (elements.size() == 1)
                answer.append("Праздник ");
            else
                answer.append("Праздники ");
            answer.append(day).append(" ").append(dates[1]).append(" ").append(year).
                    append(": ").append("\n");

            for (Element e : elements)
                answer.append(count++).append(". ").append(e.getElementsByTag("a").text())
                        .append("\n");

            return answer.substring(0, answer.length() - 1);
        } else {
            return day + " " + dates[1] + " " + year + " нет известных мне праздников.";
        }
    }
}
