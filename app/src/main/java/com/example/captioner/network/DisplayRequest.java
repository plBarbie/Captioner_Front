package com.example.captioner.network;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.captioner.model.PlayBean;

import java.time.LocalDateTime;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DisplayRequest {
    PlayBean playBean;
    String title;
    LocalDateTime now = LocalDateTime.now();

    public DisplayRequest(String title) {
        this.title = title;
    }

    public PlayBean getPlayBean() {
        return playBean;
    }

    public void setPlayBean(PlayBean playBean) {
        this.playBean = playBean;
    }

    public LocalDateTime getNow() {
        return now;
    }

    public void setNow(LocalDateTime now) {
        this.now = now;
    }
}
