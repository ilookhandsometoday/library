package com.ham.library.storage_view;

public class StorageItem {
    private Integer book_id;
    private String book_name;
    private String book_author;
    private Double book_rating;
    private Integer rack_number;
    private Integer shelf_number;
    private Integer place_id;

    public void setId(Integer i){ book_id = i; }

    public void setName(String s){ book_name = s; }

    public void setAuthor(String s){
        book_author = s;
    }

    public void setRating(Double i){ book_rating = i; }

    public void setRack(Integer i){rack_number = i; }

    public void setShelf(Integer i){shelf_number = i; }

    public void setPlace(Integer i){place_id = i; }

    public Integer getId(){
        return book_id;
    }

    public String getName(){
        return book_name;
    }

    public String getAuthor(){
        return book_author;
    }

    public Double getRating(){ return book_rating; }

    public Integer getRack(){
        return rack_number;
    }

    public Integer getShelf(){
        return shelf_number;
    }

    public Integer getPlace(){
        return place_id;
    }
}
