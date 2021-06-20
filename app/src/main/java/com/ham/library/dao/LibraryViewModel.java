package com.ham.library.dao;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ham.library.dao.entity.BookEntity;
import com.ham.library.dao.entity.Place;
import com.ham.library.dao.entity.PlaceEntity;
import com.ham.library.dao.entity.Review;
import com.ham.library.dao.entity.ReviewEntity;

import java.util.Date;
import java.util.List;

public class LibraryViewModel extends AndroidViewModel {
    private Repository repository;

    public LibraryViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    /*
        Books queries
    */
    public LiveData<List<BookEntity>> getBooks() { return repository.getBooks(); }
    public LiveData<List<BookEntity>> getBooksOrderByID(Integer id) { return repository.getBooksOrderByID(id); }
    public LiveData<List<BookEntity>> getBooksOrderByRating() { return repository.getBooksOrderByRating(); }
    public void insertBook(BookEntity data) {
        repository.insertBook(data);
    }
    public void updateBook(BookEntity data) {
        repository.updateBook(data);
    }
    public void deleteBook(BookEntity data) {
        repository.deleteBook(data);
    }

    /*
        Place queries
    */
    public LiveData<List<PlaceEntity>> getPlacesByBookcase(Integer bookcaseNum) { return repository.getPlacesByBookcase(bookcaseNum); }
    public LiveData<List<PlaceEntity>> getPlacesByBookcaseAndShelf(Integer bookcaseNum, Integer shelfNum) { return repository.getPlacesByBookcaseAndShelf(bookcaseNum, shelfNum); }
    public LiveData<List<BookEntity>> getAllBooksNoShelf(){return repository.getAllBooksNoShelf();}
    public LiveData<List<Place>> getAllPlaces(){return repository.getAllPlaces();}
    public void insertPlaces(List<Place> data){repository.insertPlaces(data);}
    public void insertPlace(Place data) {
        repository.insertPlace(data);
    }
    public void updatePlace(Place data) {
        repository.updatePlace(data);
    }
    public void deletePlace(Place data) {
        repository.deletePlace(data);
    }
    /*
        Review queries
    */
    public LiveData<List<ReviewEntity>> getLastReview(Integer bookID) { return repository.getLastReview(bookID); }
    public LiveData<List<ReviewEntity>> getReviewsBetweenDates(Integer bookID, Date start_time, Date end_time) { return repository.getReviewsBetweenDates(bookID, start_time, end_time); }
    public LiveData<List<ReviewEntity>> getReviewsByRating(Integer bookID, Integer rating) { return repository.getReviewsByRating(bookID, rating); }
    public LiveData<List<ReviewEntity>> getReviewsByRatingAndDate(Integer bookID, Integer rating, Date start_time, Date end_time) { return repository.getReviewsByRatingAndDate(bookID, rating, start_time, end_time); }
    public void insertReview(Review data) {
        repository.insertReview(data);
    }
    public void updateReview(Review data) {
        repository.updateReview(data);
    }
    public void deleteReview(Review data) {
        repository.deleteReview(data);
    }
}
