package com.ham.library.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
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

@Dao
public interface LibDao {
    /*
    Book queries
    */
    @Query("SELECT * FROM `book` order by title")
    LiveData<List<BookEntity>> getAllBooks();
    @Query("SELECT * FROM `book` WHERE id = :id")
    LiveData<List<BookEntity>> getAllBooksByID(Integer id);
    @Query("SELECT * FROM `book` order by rating")
    LiveData<List<BookEntity>> getAllBooksOrderByRating();
    @Insert
    void insertBooks(List<BookEntity> data);
    @Insert
    void insertBook(BookEntity data);
    @Update
    void updateBook(BookEntity data);
    @Delete
    void deleteBook(BookEntity data);

    /*
    Place queries
    */
    @Transaction
    @Query("SELECT * FROM `place` WHERE bookcase = :bookcase_num")
    LiveData<List<PlaceEntity>> getBooksInBookcase(Integer bookcase_num);
    @Transaction
    @Query("SELECT * FROM `place` WHERE bookcase = :bookcase_num AND shelf = :shelf_num")
    LiveData<List<PlaceEntity>> getBooksInBookcaseAndShelf(Integer bookcase_num, Integer shelf_num);
    @Transaction
    @Insert
    void insertPlaces(List<Place> data);
    @Transaction
    @Insert
    void insertPlace(Place data);
    @Transaction
    @Update
    void updatePlace(Place data);
    @Transaction
    @Delete
    void deletePlace(Place data);

    /*
    Review queries
    */
    @Transaction
    @Query("SELECT * FROM `review` WHERE book_id = :book_id ORDER BY timestamp DESC LIMIT 1")
    LiveData<List<ReviewEntity>> getLastReview(Integer book_id);
    @Transaction
    @Query("SELECT * FROM `review` WHERE book_id = :book_id AND timestamp BETWEEN :start_time AND :end_time")
    LiveData<List<ReviewEntity>> getReviewsBetweenDates(Integer book_id, Date start_time, Date end_time);
    @Transaction
    @Query("SELECT * FROM `review` WHERE book_id = :book_id AND rating = :rating")
    LiveData<List<ReviewEntity>> getReviewsByRating(Integer book_id, Integer rating);
    @Transaction
    @Query("SELECT * FROM `review` WHERE book_id = :book_id AND rating = :rating AND timestamp BETWEEN :start_time AND :end_time")
    LiveData<List<ReviewEntity>> getReviewsByRatingAndDate(Integer book_id, Integer rating, Date start_time, Date end_time);
    @Transaction
    @Insert
    void insertReviews(List<Review> data);
    @Transaction
    @Insert
    void insertReview(Review data);
    @Transaction
    @Update
    void updateReview(Review data);
    @Transaction
    @Delete
    void deleteReview(Review data);
}
