package com.example.socialnetworkgui.domain;

import java.time.LocalDateTime;

public class Message {

    private Integer from;

    private Integer to;

    private String text;

    private LocalDateTime time;

    public Message(Integer from,Integer to, String text, LocalDateTime time) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.time = time;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
