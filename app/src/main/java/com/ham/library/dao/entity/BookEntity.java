package com.ham.library.dao.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "book", indices = {@Index(value = {"title"}, unique = true)})
public class BookEntity {
    @PrimaryKey
    public Integer id;

    @ColumnInfo(name = "title")
    @NonNull
    public String title = "";

    @ColumnInfo(name = "author")
    @NonNull
    public String author = "";

    @ColumnInfo(name = "rating")
    public Double rating = (double) 0;
}
