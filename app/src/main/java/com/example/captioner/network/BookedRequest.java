package com.example.captioner.network;

public class BookedRequest {
    private String userEmail;
    private String playTitle;

    public BookedRequest(String userEmail, String playTitle) {
        this.userEmail = userEmail;
        this.playTitle = playTitle;
    }

    // Getter and setter methods
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPlayTitle() {
        return playTitle;
    }

    public void setPlayTitle(String playTitle) {
        this.playTitle = playTitle;
    }
}
