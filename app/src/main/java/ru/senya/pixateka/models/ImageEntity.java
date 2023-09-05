package ru.senya.pixateka.models;

import androidx.room.PrimaryKey;

import java.util.Date;

public class ImageEntity {

    @PrimaryKey
    private String id;

    private String name;

    private String label;

    private Tag[] tags;

    private String hexColor;

    private int intColor;

    private double width;

    private double height;

    private Integer clicked = 0;

    private String path;

    private Date created;

    private Date updated;

    private UserEntity user;

    static class Tag{
        private String ruTag, enTag;

        public String getEnTag() {
            return enTag;
        }

        public String getRuTag() {
            return ruTag;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Tag[] getTags() {
        return tags;
    }

    public void setTags(Tag[] tags) {
        this.tags = tags;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public int getIntColor() {
        return intColor;
    }

    public void setIntColor(int intColor) {
        this.intColor = intColor;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Integer getClicked() {
        return clicked;
    }

    public void setClicked(Integer clicked) {
        this.clicked = clicked;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity userEntity) {
        this.user = userEntity;
    }
}
