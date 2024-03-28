package com.example.captioner.model;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDateTime;

public class Dialogue {
    @SerializedName("dialogue_start_time")
    private LocalDateTime startTime;

    @SerializedName("dialogue_end_time")
    private LocalDateTime endTime;

    @SerializedName("dialogue_text")
    private String text;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
