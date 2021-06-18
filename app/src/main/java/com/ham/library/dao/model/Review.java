package com.ham.library.dao.model;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Review {
    private Integer id;
    private String text;
    private Integer bookID;
    private double rating;
    private Date timestamp;

    public Review(Integer id, String text, Integer bookID, double rating, Date timestamp) {
        this.id = id;
        this.text = text;
        this.bookID = bookID;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    @NotNull
    @Override
    public String toString() {
        return getText();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getBookID() {
        return bookID;
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
