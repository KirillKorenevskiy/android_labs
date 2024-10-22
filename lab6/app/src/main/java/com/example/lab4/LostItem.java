package com.example.lab4;

import android.net.Uri;

import java.io.Serializable;
import java.util.Comparator;

public class LostItem  implements Serializable, Comparable<LostItem> {
    private String title;
    private String description;
    private String location;
    private String dateFound;
    private String foundBy;
    private String retrieveLocation;
    private String photo;

    public LostItem(String title, String description, String location, String dateFound, String foundBy, String retrieveLocation, String photo) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.dateFound = dateFound;
        this.foundBy = foundBy;
        this.retrieveLocation = retrieveLocation;
        this.photo = photo;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int compareTo(LostItem other) {
        return this.title.compareTo(other.title);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getDateFound() {
        return dateFound;
    }

    public String getFoundBy() {
        return foundBy;
    }

    public String getRetrieveLocation() {
        return retrieveLocation;
    }

    public String getPhoto() {
        return photo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDateFound(String dateFound) {
        this.dateFound = dateFound;
    }

    public void setFoundBy(String foundBy) {
        this.foundBy = foundBy;
    }

    public void setRetrieveLocation(String retrieveLocation) {
        this.retrieveLocation = retrieveLocation;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

