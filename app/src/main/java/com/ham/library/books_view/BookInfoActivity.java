package com.ham.library.books_view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ham.library.BaseActivity;
import com.ham.library.R;

public class BookInfoActivity extends BaseActivity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        TextView info_author = findViewById(R.id.info_author);
        info_author.setText(getIntent().getStringExtra("author"));

        TextView info_title = findViewById(R.id.info_title);
        info_title.setText(getIntent().getStringExtra("title"));

        TextView info_rating = findViewById(R.id.info_rating);
        info_title.setText(getIntent().getIntExtra("rating", 0));

    }
}
