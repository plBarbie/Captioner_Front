package com.example.captioner.model;

import java.io.Serializable;

public class DialogueDTO implements Serializable {
    private int id;
    private String dialogueStartTime;
    private String dialogueEndTime;
    private String dialogueText;
    private String subtitle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDialogueStartTime() {
        return dialogueStartTime;
    }

    public void setDialogueStartTime(String dialogueStartTime) {
        this.dialogueStartTime = dialogueStartTime;
    }

    public String getDialogueEndTime() {
        return dialogueEndTime;
    }

    public void setDialogueEndTime(String dialogueEndTime) {
        this.dialogueEndTime = dialogueEndTime;
    }

    public String getDialogueText() {
        return dialogueText;
    }

    public void setDialogueText(String dialogueText) {
        this.dialogueText = dialogueText;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
