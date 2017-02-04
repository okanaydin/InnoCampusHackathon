package com.example.okanaydin.innocampus;

/**
 * Created by okanaydin on 04/02/17.
 */

public class Veri {

    private String title, desc, image;

    public Veri(){

    }
    public Veri(String title, String desc, String image) {
        this.title = title;
        this.desc = desc;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
