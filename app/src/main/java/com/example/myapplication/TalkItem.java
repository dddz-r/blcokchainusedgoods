package com.example.myapplication;

public class TalkItem {
    private String owner_id;
    private String opposit_id;
    private String time;
    private String contents;

    public TalkItem(String owner_id, String opposit_id, String contents, String time) {
        this.owner_id = owner_id;
        this.opposit_id = opposit_id;
        this.contents = contents;
        this.time = time;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public void setOpposit_id(String opposit_id) {
        this.opposit_id = opposit_id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getOwner_id(){
        return owner_id;
    }

    public String getOpposit_id() {
        return opposit_id;
    }

    public String getContents() {
        return contents;
    }

    public String getTime() {
        return time;
    }
}
