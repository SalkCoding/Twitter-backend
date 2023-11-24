package com.salkcoding.twitter.dto;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Data
public class NotificationOutput {
    private long notificationId;
    private String content;
    private String created;

    public NotificationOutput(long notificationId, String content, long created) {
        this.notificationId = notificationId;
        this.content=content;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(created);
        this.created = simpleDateFormat.format(calendar.getTime());
    }
}
