package com.ham.library.dao.model;

import org.jetbrains.annotations.NotNull;

public class Place {
    private Integer id;
    private Integer bookcase;
    private Integer shelf;
    private Integer bookID;

    public Place(Integer id, Integer bookcase, Integer shelf, Integer bookID) {
        this.id = id;
        this.bookcase = bookcase;
        this.shelf = shelf;
        this.bookID = bookID;
    }

    @NotNull
    @Override
    public String toString() {
        return getBookcase() + " " + getShelf() + getBookID();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookcase() {
        return bookcase;
    }

    public void setBookcase(Integer bookcase) {
        this.bookcase = bookcase;
    }

    public Integer getShelf() {
        return shelf;
    }

    public void setShelf(Integer shelf) {
        this.shelf = shelf;
    }

    public Integer getBookID() {
        return bookID;
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }
}
