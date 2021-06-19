package com.ham.library;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ham.library.dao.DatabaseManager;
import com.ham.library.dao.LibraryViewModel;
import com.ham.library.dao.entity.BookEntity;
import com.ham.library.dao.model.Book;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    ArrayAdapter<Book> adapter;
    protected LibraryViewModel libraryViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        libraryViewModel = ViewModelProviders.of(this).get(LibraryViewModel.class);

        spinner = findViewById(R.id.groupList);

        List<Book> groups = new ArrayList<>();
        BookEntity kk = new BookEntity();
        kk.author = "Григорович Сергей";
        kk.rating = (double)9;
        kk.title = "Вот ты говоришь, а я ВИЖУ";
        kk.id = 2;
        libraryViewModel.insertBook(kk);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        initGroupList(groups);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void initGroupList(List<Book> groups) {
        libraryViewModel.getBooks().observe(this, new Observer<List<BookEntity>>() {
            @Override
            public void onChanged(@Nullable List<BookEntity> list) {
                List<Book> groupsResult = new ArrayList<>();
                for (BookEntity listEntity : list) {
                    groupsResult.add(new Book(listEntity.id, listEntity.title, listEntity.author, listEntity.rating));
                }
                adapter.clear();
                adapter.addAll(groupsResult);
            }
        });
    }
}