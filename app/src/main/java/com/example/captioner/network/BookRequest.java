package com.example.captioner.network;

import com.example.captioner.model.PlayBean;

public class BookRequest {
    private PlayBean playBean;

    public BookRequest(PlayBean playBean) {
        this.playBean = playBean;
    }



    public PlayBean getPlay() {
        return playBean;
    }

    public void setPlay(PlayBean playBean) {
        this.playBean = playBean;
    }
}
