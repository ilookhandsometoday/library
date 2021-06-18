package com.ham.library.dao.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "review", foreignKeys =
        {@ForeignKey(entity = BookEntity.class, parentColumns = "id", childColumns = "book_id", onDelete = ForeignKey.CASCADE)})
public class Review {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "text")
    @NonNull
    public String text = "";

    @ColumnInfo(name = "rating")
    public int rating = 0;

    @ColumnInfo(name = "timestamp")
    public Date timestamp;

    @ColumnInfo(name = "book_id", index = true)
    public int bookID;
}
