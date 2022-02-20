package com.example.voiceassistent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.voiceassistent.Message.Message;
import com.example.voiceassistent.Message.MessageListAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    protected Button sendButton;
    protected EditText questionText;
    protected RecyclerView chatMessageList;
    protected MessageListAdapter messageListAdapter;
    protected TextToSpeech textToSpeech;
    private ArrayList<String> chat;
    private final String chatKey = "chatKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(new Locale("ru"));
                }
            }
        });

        chat = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = findViewById(R.id.sendButton);
        questionText = findViewById(R.id.questionField);
        chatMessageList = findViewById(R.id.chatMessageList);
        messageListAdapter = new MessageListAdapter();

        chatMessageList.setLayoutManager(new LinearLayoutManager(this));
        chatMessageList.setAdapter(messageListAdapter);


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
        Boolean isSend = true;
        for (String s : savedInstanceState.getStringArrayList("chatKey")) {
            messageListAdapter.messageList.add(new Message(s, isSend));
            chat.add(s);
            messageListAdapter.notifyDataSetChanged();
            chatMessageList.scrollToPosition(messageListAdapter.messageList.size() - 1);
            isSend = !isSend;
        }
    }

    protected void onSend() {
        String text = questionText.getText().toString();
        //String answer = AI.getAnswer(text);
        AI.getAnswer(text, new Consumer<String>() {
            @Override
            public void accept(String answer) {
                chat.add(text);
                chat.add(answer);
                textToSpeech.speak(answer, TextToSpeech.QUEUE_FLUSH,null);
                messageListAdapter.messageList.add(new Message(text, true));
                messageListAdapter.messageList.add(new Message(answer, false));
                messageListAdapter.notifyDataSetChanged();
                chatMessageList.scrollToPosition(messageListAdapter.messageList.size()-1);
            }
        });
    }
}