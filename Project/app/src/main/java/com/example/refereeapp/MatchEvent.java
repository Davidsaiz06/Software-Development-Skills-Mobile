package com.example.refereeapp;

import java.io.Serializable;

/**
 * Model class representing a single match incident or event (Goal, Yellow Card, Red Card).
 */
public class MatchEvent implements Serializable {

    public enum EventType {
        GOAL,
        YELLOW_CARD,
        RED_CARD
    }

    private int minute;
    private String description;
    private EventType eventType;

    public MatchEvent(int minute, String description, EventType eventType) {
        this.minute = minute;
        this.description = description;
        this.eventType = eventType;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
