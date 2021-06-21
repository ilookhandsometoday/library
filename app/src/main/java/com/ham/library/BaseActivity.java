package com.ham.library;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.ham.library.dao.LibraryViewModel;

public abstract class BaseActivity  extends AppCompatActivity {

    protected LibraryViewModel mainViewModel;
    protected SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(this).get(LibraryViewModel.class);
        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
    }
}
