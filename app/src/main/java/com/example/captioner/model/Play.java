package com.example.captioner.model;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDateTime;

public class Play {
    @SerializedName("play_title")
    private String title;

    @SerializedName("play_start_time")
    private LocalDateTime startTime;

    @SerializedName("play_end_time")
    private LocalDateTime endTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}

