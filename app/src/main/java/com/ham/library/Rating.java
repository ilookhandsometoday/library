package com.ham.library;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ham.library.dao.LibraryViewModel;
import com.ham.library.dao.entity.BookEntity;
import com.ham.library.dao.entity.Review;
import com.ham.library.dao.entity.ReviewEntity;
import com.ham.library.dao.model.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class Rating extends BaseActivity {

    private RatingBar ratingBar;
    int bookId=-1, mode = 0;
    BookEntity book;
    int ratingValue;
    EditText EditReview;
    TextView nameBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_activity);



        EditReview = findViewById(R.id.reviewTextView);
        nameBook = findViewById(R.id.NameBookLabel);
        ratingBar = findViewById(R.id.ratingBar);

        Intent intent = getIntent();
        bookId=intent.getExtras().getInt("bookId");
        mainViewModel.getBooksOrderByID(bookId).observe(this, new Observer<List<BookEntity>>() {
            @Override
            public void onChanged(@Nullable List<BookEntity> list) {
                List<Book> groupsResult = new ArrayList<>();
                if (!list.isEmpty())
                {
                    for (BookEntity listEntity : list) {
                        groupsResult.add(new Book(listEntity.id, listEntity.title, listEntity.author, listEntity.rating));
                        book = listEntity;
                    }
                    nameBook.setText('"'+groupsResult.get(0).getTitle()+'"'+' '+groupsResult.get(0).getAuthor());

                }
            }
        });

        Button addButton = findViewById(R.id.AddReviewButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddReview();
            }
        });

        addListenerOnRatingBar();
SHow();

    }
    public void addListenerOnRatingBar() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ratingValue = (int)rating;
            }
        });
    }

    protected int findId(Date data) {
        String result = Integer.toString(data.getDate())+Integer.toString(data.getMonth())+Integer.toString(data.getHours())+Integer.toString(data.getMinutes())+Integer.toString(data.getSeconds())+Integer.toString(bookId);
        int ID = Integer.parseInt(result);
        return ID;
    }
    protected void SHow() {
        mainViewModel.getBookReviews(bookId).observe(this, new Observer<List<ReviewEntity>>() {

            @Override
            public void onChanged(@Nullable List<ReviewEntity> list) {
                List<Review> groupsResult = new ArrayList<>();
                if (!list.isEmpty())
                {
                    for (ReviewEntity listEntity : list) {
                        Log.d("hello", listEntity.reviewEntity.text+' '+listEntity.reviewEntity.bookID+' '+listEntity.reviewEntity.rating+' '+listEntity.reviewEntity.id+' '+listEntity.reviewEntity.timestamp);
                    }


                } else{
                    Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    protected void AddReview(){
        String review = EditReview.getText().toString();
        if (review.length()==0) {
            review = "";
        }


        Review result = new Review();

        result.bookID = bookId;
        result.text = review;
        result.rating = ratingValue;
        result.timestamp = new Date();
        result.id = findId(result.timestamp);

        BookEntity b = new BookEntity();
        b.id = bookId;
        b.author = book.author;
        b.title = book.title;
        b.rating = Double.valueOf(Integer.toString(ratingValue));
        Executors.newSingleThreadExecutor().execute(() -> mainViewModel.updateBook(b));


        try {

            Executors.newSingleThreadExecutor().execute(() -> mainViewModel.insertReview(result));

        }catch(Exception e) {
            Log.d("add_review", e.toString());
        }

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

}
