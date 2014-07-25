package com.twi.awayday2014.models;

public class ShortNotification {

    private final String message;
    private final String arrivalTime;

    public ShortNotification(String msg, String arrivalTime) {
        this.message = msg;
        this.arrivalTime = arrivalTime;
    }

    public String message() {
        return message;
    }

    public String messageTime() {
        return arrivalTime;
    }
}
