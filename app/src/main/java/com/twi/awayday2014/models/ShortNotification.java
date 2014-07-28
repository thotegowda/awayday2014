package com.twi.awayday2014.models;

import com.orm.SugarRecord;

public class ShortNotification extends SugarRecord<ShortNotification> {

    String message;
    String arrivalTime;

    public ShortNotification() {

    }

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
