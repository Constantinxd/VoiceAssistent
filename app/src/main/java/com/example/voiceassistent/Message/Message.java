package com.example.voiceassistent.Message;

import com.example.voiceassistent.Datebase.MessageEntity;

import java.util.Date;

public class Message {
    public String text;
    public Date date;
    public Boolean isSend;

    public Message(String text, Boolean isSend) {
        this.text = text;
        this.date = new Date();
        this.isSend = isSend;
    }

    public Message (MessageEntity entity) {
        text = entity.text;
        date = new Date(entity.date);
        isSend = entity.isSend == 1;
    }
}
