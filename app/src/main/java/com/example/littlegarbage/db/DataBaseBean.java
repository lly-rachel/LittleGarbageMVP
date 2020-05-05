package com.example.littlegarbage.db;

public class DataBaseBean {

    private int _id;
    private String garbage;
    private String content;

    public DataBaseBean() {
    }

    public DataBaseBean(int _id, String garbage, String content) {
        this._id = _id;
        this.garbage = garbage;
        this.content = content;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getGarbage() {
        return garbage;
    }

    public void setGarbage(String garbage) {
        this.garbage = garbage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
