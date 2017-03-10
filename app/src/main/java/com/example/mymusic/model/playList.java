package com.example.mymusic.model;

/**
 * Created by wn123 on 2017/2/27.
 */

public class playList {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }

    private int id;
    private String playListName;
}
