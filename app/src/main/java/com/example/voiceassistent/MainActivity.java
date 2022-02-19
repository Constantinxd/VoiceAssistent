package com.example.voiceassistent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
    implements TextToSpeech.OnInitListener {

    protected Button sendButton;
    protected EditText questionText;
    protected TextView chatWindow;
    protected TextToSpeech textToSpeech;
    private ArrayList<String> chat;
    private final String chatKey = "chatKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        textToSpeech = new TextToSpeech(this, this);
        chat = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = findViewById(R.id.sendButton);
        questionText = findViewById(R.id.questionField);
        chatWindow = findViewById(R.id.chatWindow);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSend();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putStringArrayList(chatKey, chat);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        for (String s : savedInstanceState.getStringArrayList("chat"))
            chatWindow.append(s + "\n");
    }


    @Override
    public void onInit(int i) {
        if (i != TextToSpeech.ERROR) {
            textToSpeech.setLanguage(new Locale("ru"));
        }
    }

    protected void onSend() {
        String text = questionText.getText().toString();
        String answer = AI.getAnswer(text);
        chat.add(text);
        chat.add(answer);
        textToSpeech.speak(answer, TextToSpeech.QUEUE_FLUSH,null);
        chatWindow.append(">> " + text + "\n");
        chatWindow.append("<< " + answer + "\n");
    }
}