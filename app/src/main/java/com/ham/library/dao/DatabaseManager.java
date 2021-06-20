package com.ham.library.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ham.library.dao.entity.BookEntity;
import com.ham.library.dao.entity.Review;
import com.ham.library.dao.entity.ReviewEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import static com.ham.library.dao.utils.Converters.dateFromString;

public class DatabaseManager {
    private DatabaseHelper db;
    private static DatabaseManager instance;

    public static DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseManager(Context context) {
        db = Room.databaseBuilder(context,
                DatabaseHelper.class, DatabaseHelper.DATABASE_NAME)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                initData(context);
                            }
                        });
                    }
                })
                .build();
    }

    public LibDao getLibDao() {
        return db.libDao();
    }

    private void initData(Context context) {
        List<BookEntity> books = new ArrayList<>();
        BookEntity book = new BookEntity();
        book.author = "Григорович Сергей";
        book.rating = (double)10;
        book.title = "Вот ты говоришь, а я ВИЖУ";
        book.id = 1;
        books.add(book);
        DatabaseManager.getInstance(context).getLibDao().insertBooks(books);

        List<Review> reviews = new ArrayList<>();
        Review review = new Review();
        review.id = 1;
        review.bookID = 1;
        review.text = "Ваще зашибос!";
        review.rating = 5;
        review.timestamp = dateFromString("2021-06-20 21:00");
        reviews.add(review);
        DatabaseManager.getInstance(context).getLibDao().insertReviews(reviews);
    }
}
