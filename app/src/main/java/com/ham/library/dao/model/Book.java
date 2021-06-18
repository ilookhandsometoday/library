package com.ham.library.dao.model;

import org.jetbrains.annotations.NotNull;

public class Book {
    private Integer id;
    private String title;
    private String author;
    private double rating;

    public Book(Integer id, String title, String author, double rating) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.rating = rating;
    }

    @NotNull
    @Override
    public String toString() {
        return getTitle() + " " + getAuthor();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}
