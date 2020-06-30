package com.qilu.ec.main.sample;


/**
 * A dummy item representing a piece of content.
 */
public class ExampleItem {
    private String id;
    private String content;
    private String image;
    private Boolean isSaved;

    public Boolean getSaved() {
        return isSaved;
    }

    public void setSaved(Boolean isSaved) {
        this.isSaved = isSaved;
    }

    public ExampleItem() {
    }

    public ExampleItem(String id, String content, /*Drawable*/String image, Boolean isSaved) {
        this.id = id;
        this.content = content;
        this.image = image;
        this.isSaved = isSaved;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public /*Drawable*/String getImage() {
        return image;
    }

    public void setImage(/*Drawable*/String image) {
        this.image = image;
    }
}