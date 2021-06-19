package com.ham.library.dao.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;


public class ReviewEntity {
    @Embedded
    public Review reviewEntity;
    @Relation(
            parentColumn = "book_id",
            entityColumn = "id"
    )
    public BookEntity bookEntity;
}
