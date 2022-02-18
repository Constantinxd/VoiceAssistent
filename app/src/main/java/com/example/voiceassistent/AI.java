package com.example.voiceassistent;

import android.graphics.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AI {
    final static Map<String, String> map = new HashMap<>();

    static {
        map.put("Привет", "Привет");
        map.put("(?i)Hello", "Привет");
        map.put("(?i)(How are you[\\?]?)", "Нормально");
    }

    protected static String getAnswer(String question) {
        Optional<String> answer = map.entrySet()
                .stream()
                .filter(entry -> Pattern.matches(entry.getKey(), question))
                .map(Map.Entry::getValue).findFirst();



        return (answer.isPresent()) ? answer.get() : "Думаю...";
    }
}
