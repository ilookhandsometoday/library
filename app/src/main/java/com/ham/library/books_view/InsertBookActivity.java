package com.ham.library.books_view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ham.library.BaseActivity;
import com.ham.library.R;
import com.ham.library.dao.entity.BookEntity;

import java.util.concurrent.Executors;

public class InsertBookActivity extends BaseActivity {
    Button okButton;
    EditText author;
    EditText name;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_book);

        this.author = findViewById(R.id.author);
        this.author.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.name = findViewById(R.id.name);
        this.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.okButton = findViewById(R.id.ok_add_button);
        this.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry();
            }
        });
    }

    private void addEntry(){
        BookEntity entity = new BookEntity();
        entity.author = this.author.getText().toString();
        entity.title = this.name.getText().toString();
        Executors.newSingleThreadExecutor().execute(() -> this.mainViewModel.insertBook(entity));

        Toast toast = Toast.makeText(this, "Книга добавлена", Toast.LENGTH_LONG);
        toast.show();
        finish();
    }

    private void checkFields() {
        okButton.setEnabled(author.length() > 0 && name.length() > 0);
    }
}
