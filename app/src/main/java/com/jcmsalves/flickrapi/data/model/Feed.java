package com.jcmsalves.flickrapi.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by joaoalves on 14/01/2017.
 */

public class Feed {

    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("generator")
    @Expose
    private String generator;
    @SerializedName("items")
    @Expose
    private ArrayList<Photo> photos = new ArrayList<>();

    public String getModified() {
        return modified;
    }

    public String getGenerator() {
        return generator;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }
}
