package ru.proskyryakov.cbrcursondateadapter.cache;

import lombok.Getter;

import java.util.Date;

@Getter
public class DateValue<V> {
    private V value;
    private Date date;

    public DateValue(V value, Long saveTime) {
        this.value = value;
        date = new Date();
        date.setTime(date.getTime() + saveTime);
    }

    public boolean isLive() {
        return date.getTime() > new Date().getTime();
    }
}
