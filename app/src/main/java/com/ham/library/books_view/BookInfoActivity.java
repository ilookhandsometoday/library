package com.ham.library.books_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ham.library.BaseActivity;
import com.ham.library.R;
import com.ham.library.Rating;
import com.ham.library.dao.entity.BookEntity;
import com.ham.library.dao.entity.ReviewEntity;
import com.ham.library.dao.model.Book;
import com.ham.library.dao.model.Review;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class BookInfoActivity extends BaseActivity {
    private ReviewItemAdapter adapter;
    private BookEntity bookEntity;
    private RecyclerView reviews;
    private Integer id;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        Intent this_intent = getIntent();

        String author_string = this_intent.getStringExtra("author");
        String title_string = this_intent.getStringExtra("title");
        double rating = this_intent.getDoubleExtra("rating", 0);
        this.id = this_intent.getIntExtra("id", 0);


        TextView info_author = findViewById(R.id.info_author);
        info_author.setText(author_string);

        TextView info_title = findViewById(R.id.info_title);
        info_title.setText(title_string);

        TextView info_rating = findViewById(R.id.info_rating);
        info_rating.setText("Рейтинг:" + rating);
        bookEntity = new BookEntity();
        bookEntity.id = id;
        bookEntity.author = author_string;
        bookEntity.rating = rating;
        bookEntity.title = title_string;

        Button deleteBookButton = findViewById(R.id.delete_book_button);
        deleteBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook();
            }
        });

        Button writeReviewButton = findViewById(R.id.write_review_button);
        writeReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReviewActivity();
            }
        });

        this.reviews = findViewById(R.id.reviews);
        this.reviews.setLayoutManager(new LinearLayoutManager(this));
        this.reviews.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        this.adapter = new ReviewItemAdapter();
        this.reviews.setAdapter(this.adapter);

        mainViewModel.getBookReviews(id).observe(this, new Observer<List<ReviewEntity>>() {
            @Override
            public void onChanged(List<ReviewEntity> reviewEntities) {
                fetchData(reviewEntities);
            }
        });
    }

    private void openReviewActivity(){
        Intent intent = new Intent(this, Rating.class);
        intent.putExtra("bookId", this.id);
        startActivityForResult(intent, 11);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){
            return ;
        }
        Toast.makeText(getApplicationContext(), "Изменения сохранены", Toast.LENGTH_LONG).show();
    }

    private void deleteBook(){
        Executors.newSingleThreadExecutor().execute(() -> mainViewModel.deleteBook(this.bookEntity));
        finish();
    }

    public void fetchData(List<ReviewEntity> reviewEntities){
        ArrayList<Review> reviewsFromBD = new ArrayList<>();
        for (ReviewEntity listEntity : reviewEntities) {
            //string to not repeat extracting Review class with Review entities
            com.ham.library.dao.entity.Review review = listEntity.reviewEntity;
            reviewsFromBD.add(new Review(review.id, review.text, review.bookID, review.rating, review.timestamp));
        }
        this.adapter.setDataList(reviewsFromBD);
    }

    public final static class ReviewItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private ArrayList<Review> dataList = new ArrayList<>();

        public static class ViewHolder extends RecyclerView.ViewHolder{
            private TextView timestamp;
            private TextView review;
            private TextView rating;
            public ViewHolder(View itemView){
                super(itemView);
                this.timestamp = itemView.findViewById(R.id.timestamp);
                this.review = itemView.findViewById(R.id.review_text);
                this.rating = itemView.findViewById(R.id.rating_review);
            }

            public void bind(final Review data){
                this.timestamp.setText(data.getTimestamp().toString());
                this.review.setText(data.getText());
                this.rating.setText("Рейтинг:" + data.getRating());
            }
        }

        public void setDataList(ArrayList<Review> dataList) {
            this.dataList = new ArrayList<>();
            if (dataList != null) {
                this.dataList.addAll(dataList);
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.review_item_layout, parent, false);
            return new ReviewItemAdapter.ViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
            Review data = this.dataList.get(position);
            ((ReviewItemAdapter.ViewHolder) holder).bind(data);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }
}
