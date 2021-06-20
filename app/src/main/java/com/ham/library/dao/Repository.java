package com.ham.library.dao;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.ham.library.dao.entity.BookEntity;
import com.ham.library.dao.entity.Place;
import com.ham.library.dao.entity.PlaceEntity;
import com.ham.library.dao.entity.Review;
import com.ham.library.dao.entity.ReviewEntity;

import java.util.Date;
import java.util.List;

public class Repository {
    private final LibDao dao;

    public Repository(Context context) {
        DatabaseManager databaseManager = DatabaseManager.getInstance(context);
        dao = databaseManager.getLibDao();
    }

    /*
        Books queries
    */
    public LiveData<List<BookEntity>> getBooks() { return dao.getAllBooks(); }
    public LiveData<List<BookEntity>> getBooksOrderByID(Integer id) { return dao.getAllBooksByID(id); }
    public LiveData<List<BookEntity>> getBooksOrderByRating() { return dao.getAllBooksOrderByRating(); }
    public void insertBook(BookEntity data) {
        dao.insertBook(data);
    }
    public void updateBook(BookEntity data) {
        dao.updateBook(data);
    }
    public void deleteBook(BookEntity data) {
        dao.deleteBook(data);
    }

    /*
        Place queries
    */
    public LiveData<List<PlaceEntity>> getPlacesByBookcase(Integer bookcaseNum) { return dao.getBooksInBookcase(bookcaseNum); }
    public LiveData<List<PlaceEntity>> getPlacesByBookcaseAndShelf(Integer bookcaseNum, Integer shelfNum) { return dao.getBooksInBookcaseAndShelf(bookcaseNum, shelfNum); }
    public LiveData<List<BookEntity>> getAllBooksNoShelf(){return dao.getAllBooksNoShelf();}
    public LiveData<List<Place>> getAllPlaces(){return dao.getAllPlaces();}
    public void insertPlaces(List<Place> data){dao.insertPlaces(data);}
    public void insertPlace(Place data) {
        dao.insertPlace(data);
    }
    public void updatePlace(Place data) {
        dao.updatePlace(data);
    }
    public void deletePlace(Place data) {
        dao.deletePlace(data);
    }
    /*
        Review queries
    */
    public LiveData<List<ReviewEntity>> getLastReview(Integer bookID) { return dao.getLastReview(bookID); }
    public LiveData<List<ReviewEntity>> getReviewsBetweenDates(Integer bookID, Date start_time, Date end_time) { return dao.getReviewsBetweenDates(bookID, start_time, end_time); }
    public LiveData<List<ReviewEntity>> getReviewsByRating(Integer bookID, Integer rating) { return dao.getReviewsByRating(bookID, rating); }
    public LiveData<List<ReviewEntity>> getReviewsByRatingAndDate(Integer bookID, Integer rating, Date start_time, Date end_time) { return dao.getReviewsByRatingAndDate(bookID, rating, start_time, end_time); }
    public void insertReview(Review data) {
        dao.insertReview(data);
    }
    public void updateReview(Review data) {
        dao.updateReview(data);
    }
    public void deleteReview(Review data) {
        dao.deleteReview(data);
    }
}
