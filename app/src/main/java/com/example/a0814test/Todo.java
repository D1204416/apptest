package com.example.a0814test;


public class Todo {
    private String title;
    private String content;
    private String imgName;
    private int num;

    public Todo(String title, String content, int num, String imgName) {
        this.title = title;
        this.content = content;
        this.num = num;
        this.imgName = imgName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }


}