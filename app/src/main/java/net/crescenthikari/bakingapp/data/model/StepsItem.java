package net.crescenthikari.bakingapp.data.model;

import com.google.gson.annotations.SerializedName;

public class StepsItem {

    @SerializedName("videoURL")
    private String videoURL;

    @SerializedName("description")
    private String description;

    @SerializedName("id")
    private int id;

    @SerializedName("shortDescription")
    private String shortDescription;

    @SerializedName("thumbnailURL")
    private String thumbnailURL;

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public String toString() {
        return
                "StepsItem{" +
                        "videoURL = '" + videoURL + '\'' +
                        ",description = '" + description + '\'' +
                        ",id = '" + id + '\'' +
                        ",shortDescription = '" + shortDescription + '\'' +
                        ",thumbnailURL = '" + thumbnailURL + '\'' +
                        "}";
    }
}