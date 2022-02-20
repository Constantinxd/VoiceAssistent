package com.example.voiceassistent.Message;

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
}
