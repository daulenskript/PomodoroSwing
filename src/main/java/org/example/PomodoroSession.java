package org.example;

import java.time.LocalDateTime;

class PomodoroSession {
    private LocalDateTime dateTime;
    private int duration;
    private String mood;

    public PomodoroSession(LocalDateTime dateTime, int duration, String mood) {
        this.dateTime = dateTime;
        this.duration = duration;
        this.mood = mood;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getDuration() {
        return duration;
    }

    public String getMood() {
        return mood;
    }
}
