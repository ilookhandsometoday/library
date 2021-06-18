package com.ham.library.storage_view;

public class StorageItem {

    private Integer book_id;
    private String book_name;
    private String book_author;
    private Integer rack_number;
    private Integer shelf_number;


    public void setId(Integer i){ book_id = i; }

    public void setName(String s){ book_name = s; }

    public void setAuthor(String s){
        book_author = s;
    }

    public void setRack(Integer i){ rack_number = i; }

    public void setShelf(Integer i){ shelf_number = i; }

    public Integer getId(){
        return book_id;
    }

    public String getName(){
        return book_name;
    }

    public String getAuthor(){
        return book_author;
    }

    public Integer getRack(Integer i){ return rack_number; }

    public Integer getShelf(Integer i){ return shelf_number; }
}
