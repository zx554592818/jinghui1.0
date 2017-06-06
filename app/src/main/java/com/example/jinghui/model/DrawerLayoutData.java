package com.example.jinghui.model;

/**
 * Created by zhaoxin on 2017-05-22.
 */

public class DrawerLayoutData {
    private int imageView;
    private String text;


    public DrawerLayoutData(int imageView, String text) {
        this.imageView = imageView;
        this.text = text;

    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
