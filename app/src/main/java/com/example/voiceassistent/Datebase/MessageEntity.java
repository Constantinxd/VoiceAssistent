package com.example.voiceassistent.Datebase;

import com.example.voiceassistent.Message.Message;

import java.util.Date;

public class MessageEntity {
    public String text;
    public String date;
    public int isSend;

    public MessageEntity(String text, String date, int isSend) {
        this.text = text;
        this.date = date;
        this.isSend = isSend;
    }

    public MessageEntity(Message message) {
        text = message.text;
        date = message.date.toString();
        isSend = (message.isSend) ? 1 : 0;
    }
}
