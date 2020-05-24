package com.example.weiver;

public class TalkListItem {

    private String opposit_id;
    private String time;

    public TalkListItem(String opposit_id, String time) {
        this.opposit_id = opposit_id;
        this.time = time;
    }


    public void setOpposit_id(String opposit_id) {
        this.opposit_id = opposit_id;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getOpposit_id() {
        return opposit_id;
    }


    public String getTime() {
        return time;
    }
}
