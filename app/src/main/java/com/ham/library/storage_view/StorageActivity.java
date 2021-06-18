package com.ham.library.storage_view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ham.library.R;
import com.shawnlin.numberpicker.NumberPicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity {

    private NumberPicker rackNum;
    private NumberPicker shelfNum;
    private Integer selectedRack;
    private Integer selectedShelf;

    private RecyclerView storageView;
    List<StorageItem> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        rackNum = (NumberPicker) findViewById(R.id.rackNumPick);
        shelfNum = (NumberPicker) findViewById(R.id.shelfNumPick);

        setNumPickers();
        initData();
    }

    private void initData(){
        list = new ArrayList<>();
        createBookList();
    }

    private void  createBookList(){

    }

    private void setNumPickers(){
        rackNum.setMaxValue(10);
        rackNum.setMinValue(1);
        rackNum.setValue(1);
        rackNum.setFadingEdgeEnabled(true);// Set fading edge enabled
        rackNum.setScrollerEnabled(true);// Set scroller enabled
        rackNum.setWrapSelectorWheel(true);// Set wrap selector wheel
        rackNum.setAccessibilityDescriptionEnabled(true);/// Set accessibility description enabled
        rackNum.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedRack = newVal;
            }
        });

        shelfNum.setMaxValue(5);
        shelfNum.setMinValue(1);
        shelfNum.setValue(1);
        shelfNum.setFadingEdgeEnabled(true);// Set fading edge enabled
        shelfNum.setScrollerEnabled(true);// Set scroller enabled
        shelfNum.setWrapSelectorWheel(true);// Set wrap selector wheel
        shelfNum.setAccessibilityDescriptionEnabled(true);/// Set accessibility description enabled
        shelfNum.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedShelf = newVal;
            }
        });
    }

    private void onStorageItemClick(StorageItem storageItem) { }

}
