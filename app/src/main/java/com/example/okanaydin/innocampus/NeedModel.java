package com.example.okanaydin.innocampus;

/**
 * Created by okanaydin on 04/02/17.
 */

public class NeedModel {

    private String title, desc, image, refugeeId;

    public NeedModel(String title, String desc, String image, String refugeeId) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.refugeeId = refugeeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRefugeeId() {
        return refugeeId;
    }

    public void setRefugeeId(String refugeeId) {
        this.refugeeId = refugeeId;
    }
}
