package com.ham.library.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ham.library.dao.entity.BookEntity;
import com.ham.library.dao.entity.Place;
import com.ham.library.dao.entity.Review;
import com.ham.library.dao.utils.Converters;

@Database(entities = {BookEntity.class, Place.class, Review.class},
        version = 1,
        exportSchema = false)
@TypeConverters({Converters.class})
public abstract class DatabaseHelper extends RoomDatabase {
    public static final String DATABASE_NAME = "library";

    public abstract LibDao libDao();
}

