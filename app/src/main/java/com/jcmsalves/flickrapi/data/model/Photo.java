package com.jcmsalves.flickrapi.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by joaoalves on 14/01/2017.
 */

public class Photo {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("link")
    @Expose
    private String webUrl;
    @SerializedName("media")
    @Expose
    private Media media;
    @SerializedName("date_taken")
    @Expose
    private String dateTaken;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("published")
    @Expose
    private String published;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("author_id")
    @Expose
    private String authorId;
    @SerializedName("tags")
    @Expose
    private String tags;

    public String getTitle() {
        return title;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public Media getMedia() {
        return media;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public String getDescription() {
        return description;
    }

    public String getPublished() {
        return published;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getTags() {
        return tags;
    }

    private class Media {

        @SerializedName("m")
        @Expose
        private String photoUrl;

        public String getPhotoUrl() {
            return photoUrl;
        }
    }
}