package com.ham.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import com.ham.library.books_view.BooksActivity;

import com.ham.library.storage_view.StorageActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button books_button = findViewById(R.id.books_button);
        books_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBooksActivity();
            }
        });

        Button storage_button = findViewById(R.id.storage_button);
        storage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StorageActivity.class);
                startActivity(intent);
            }
        });

        Button compilation_button = findViewById(R.id.compilation_button);
        compilation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Compilations.class);
                startActivity(intent);
            }
        });
    }

    private void showBooksActivity(){
        Intent intent = new Intent(this, BooksActivity.class);
        startActivity(intent);
    }
}