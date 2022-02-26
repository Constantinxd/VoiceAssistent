package com.example.voiceassistent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.voiceassistent.Datebase.DBHelper;
import com.example.voiceassistent.Datebase.MessageEntity;
import com.example.voiceassistent.Message.Message;
import com.example.voiceassistent.Message.MessageListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    protected Button sendButton;
    protected EditText questionText;
    protected RecyclerView chatMessageList;
    protected MessageListAdapter messageListAdapter;
    protected TextToSpeech textToSpeech;
    private ArrayList<String> chat;
    private final String chatKey = "chatKey";

    SharedPreferences sPref;
    public static final String APP_PREFERENCES = "mysettings";
    private boolean isLight = true;
    private String THEME = "THEME";

    DBHelper dBHelper;
    SQLiteDatabase database;

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

        sPref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        isLight = sPref.getBoolean(THEME, true);

        if (!isLight) getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_main);

        sendButton = findViewById(R.id.sendButton);
        questionText = findViewById(R.id.questionField);
        chatMessageList = findViewById(R.id.chatMessageList);
        messageListAdapter = new MessageListAdapter();

        chatMessageList.setLayoutManager(new LinearLayoutManager(this));
        chatMessageList.setAdapter(messageListAdapter);

        dBHelper = new DBHelper(this);
        database = dBHelper.getWritableDatabase();

        Cursor cursor = database.query(dBHelper.TABLE_MESSAGES, null, null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            int messageIndex = cursor.getColumnIndex(dBHelper.FIELD_MESSAGE);
            int dateIndex = cursor.getColumnIndex(dBHelper.FIELD_DATE);
            int sendIndex = cursor.getColumnIndex(dBHelper.FIELD_SEND);

            do {
                MessageEntity entity = new MessageEntity(cursor.getString(messageIndex),
                        cursor.getString(dateIndex), cursor.getInt(sendIndex));
                Message message = new Message(entity);
                messageListAdapter.messageList.add(message);
                messageListAdapter.notifyDataSetChanged();
                chatMessageList.scrollToPosition(messageListAdapter.messageList.size() - 1);
            } while (cursor.moveToNext());
        }
        cursor.close();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSend();
            }
        });
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStop() {
        database.delete(dBHelper.TABLE_MESSAGES, null, null);
        System.out.println("HEREHEA");
        System.out.println(messageListAdapter.messageList.size());
        for (int i = 0; i < messageListAdapter.messageList.size(); i++) {
            MessageEntity entity = new MessageEntity(messageListAdapter.messageList.get(i));
            System.out.println(entity.text);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.FIELD_MESSAGE, entity.text);
            contentValues.put(DBHelper.FIELD_SEND, entity.isSend);
            contentValues.put(DBHelper.FIELD_DATE, entity.date);

            database.insert(dBHelper.TABLE_MESSAGES,null, contentValues);
        }

        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean(THEME, isLight);
        editor.apply();
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.day_settings:
                isLight = true;
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.night_settings:
                isLight = false;
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
        AI.getAnswer(text, new Consumer<String>() {
            @Override
            public void accept(String answer) {
                chat.add(text);
                chat.add(answer);
                textToSpeech.speak(answer, TextToSpeech.QUEUE_FLUSH,null);
                messageListAdapter.messageList.add(new Message(text, true));
                messageListAdapter.messageList.add(new Message(answer, false));
                messageListAdapter.notifyDataSetChanged();
                chatMessageList.scrollToPosition(messageListAdapter.messageList.size() - 1);
                System.out.println(messageListAdapter.messageList.size());
            }
        });
    }
}