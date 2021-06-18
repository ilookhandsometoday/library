package com.ham.library.dao.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "place", foreignKeys =
        {@ForeignKey(entity = BookEntity.class, parentColumns = "id", childColumns = "book_id", onDelete = ForeignKey.CASCADE)})
public class Place {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "bookcase")
    public int bookcase = 0;

    @ColumnInfo(name = "shelf")
    public int shelf = 0;

    @ColumnInfo(name = "book_id", index = true)
    public int bookID;
}
