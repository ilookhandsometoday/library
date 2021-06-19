package com.ham.library.storage_view;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ham.library.R;
import com.ham.library.dao.LibraryViewModel;
import com.ham.library.dao.entity.Place;
import com.ham.library.dao.entity.PlaceEntity;
import com.shawnlin.numberpicker.NumberPicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

public class StorageActivity extends AppCompatActivity implements ReplaceDialog.OnCompleteListener{

    private NumberPicker rackNum;
    private NumberPicker shelfNum;
    private static Integer selectedRack = 1;
    private static Integer selectedShelf = 1;
    private static FragmentManager manager;

    private Boolean isRackEmpty = false;

    ProgressBar pb;
    private RecyclerView storageView;
    private ItemAdapter adapter;
    private List<StorageItem> list;
    private LibraryViewModel LVM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        LVM = ViewModelProviders.of(this).get(LibraryViewModel.class);
        rackNum = findViewById(R.id.rackNum);
        shelfNum = findViewById(R.id.shelfNum);
        pb = findViewById(R.id.progressBar);
        manager = getSupportFragmentManager();

        setNumPickers();

        storageView = findViewById(R.id.storageView);
        storageView.setLayoutManager(new LinearLayoutManager(this));
        storageView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new ItemAdapter(this::onStorageItemClick);
        initData();
    }

    private void initData(){
        list = new ArrayList<>();

        getBookList();
    }

    private void getBookList(){
        checkRack();
        if(!isRackEmpty)
            LVM.getPlacesByBookcaseAndShelf(selectedRack,selectedShelf).observe(this, placeEntities -> {
                for(PlaceEntity plEnt: placeEntities) {
                    if (plEnt != null) list.add(createItem(plEnt));
                }
                Log.d("mydb", "Кол-во книг на полке: " + list.size());
                if(list.size() == 0) {
                    NoStorageItem noItems = new NoStorageItem();
                    Log.d("mydb", "Создан объект сообщения");
                    noItems.setMessage(getString(R.string.SA_nobooks));
                    Log.d("mydb", "Сообщение записано");
                    list.add(noItems);
                    Log.d("mydb", "Сообщение в список добавлено");
                }
                adapter.setDataList(list);
                storageView.setAdapter(adapter);
            });
    }

    private void checkRack(){
        LVM.getPlacesByBookcase(selectedRack).observe(this, placeEntities -> {
            if(placeEntities.size() == 0)
            {
                String mes = getString(R.string.SA_nobooks_rack_1) + " " + selectedRack + " " + getString(R.string.SA_nobooks_rack_2);
                Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
                isRackEmpty = true;
            }
            else isRackEmpty = false;
        });
    }

    private StorageItem createItem(PlaceEntity data){
        StorageItem si = new StorageItem();
        si.setId(data.bookEntity.id);
        si.setAuthor(data.bookEntity.author);
        si.setName(data.bookEntity.title);
        si.setRating(data.bookEntity.rating);
        si.setPlace(data.placeEntity.id);
        si.setRack(data.placeEntity.bookcase);
        si.setShelf(data.placeEntity.shelf);
        return si;
    }

    private void setNumPickers(){
        rackNum.setMaxValue(10);
        rackNum.setMinValue(1);
        rackNum.setValue(1);
        rackNum.setFadingEdgeEnabled(true);// Set fading edge enabled
        rackNum.setScrollerEnabled(true);// Set scroller enabled
        rackNum.setWrapSelectorWheel(true);// Set wrap selector wheel
        rackNum.setAccessibilityDescriptionEnabled(true);/// Set accessibility description enabled
        rackNum.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if(oldVal!=newVal) {
                selectedRack = newVal;
                initData();
            }
        });

        shelfNum.setMaxValue(5);
        shelfNum.setMinValue(1);
        shelfNum.setValue(1);
        shelfNum.setFadingEdgeEnabled(true);// Set fading edge enabled
        shelfNum.setScrollerEnabled(true);// Set scroller enabled
        shelfNum.setWrapSelectorWheel(true);// Set wrap selector wheel
        shelfNum.setAccessibilityDescriptionEnabled(true);/// Set accessibility description enabled
        shelfNum.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if(oldVal!=newVal) {
                selectedShelf = newVal;
                initData();
            }
        });
    }

    private void onStorageItemClick(StorageItem storageItem) { }

    @Override
    public void onCloseDialog(Integer place, Integer book, Integer rack, Integer shelf) {
        Place p = new Place();
        p.id = place;
        p.bookID = book;
        p.bookcase = rack;
        p.shelf = shelf;
        Executors.newSingleThreadExecutor().execute(() -> LVM.updatePlace(p));
        new CountDownTimer(500, 1000) {
            public void onTick(long millisUntilFinished) {
                storageView.setVisibility(RecyclerView.INVISIBLE);
                pb.setVisibility(ProgressBar.VISIBLE);
            }
            public void onFinish() {
                pb.setVisibility(ProgressBar.INVISIBLE);
                storageView.setVisibility(RecyclerView.VISIBLE);
                initData();
            }
        }.start();
    }

    //////////////////////////////////////
    public final static class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private final static int TYPE_ITEM = 0;
        private final static int TYPE_MESSAGE = 1;

        private List<StorageItem> dataList = new ArrayList<>();
        private final OnItemClick onItemClick;

        public ItemAdapter(OnItemClick onItemClick) {this.onItemClick = onItemClick;}

        public void setDataList(List<StorageItem> list) {
            dataList = list;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            Log.d("mydb", "viewType " + viewType);
            if(viewType == TYPE_ITEM){
                View contactView = inflater.inflate(R.layout.storage_item, parent, false);
                return new ViewHolder(contactView, context, onItemClick);
            }
            else if (viewType == TYPE_MESSAGE){
                View contactView = inflater.inflate(R.layout.nostorage_item, parent, false);
                return new ViewHolderMessage(contactView, context, onItemClick);
            }
            throw new IllegalArgumentException("Invalid view type");
        }

        @Override
        public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, int position){
            StorageItem data = dataList.get(position);

            if(viewHolder instanceof ViewHolder){
                ((ViewHolder) viewHolder).bind(data);
            }
            else if (viewHolder instanceof ViewHolderMessage)
                ((ViewHolderMessage) viewHolder).bind((NoStorageItem) data);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public int getItemViewType(int position){
            StorageItem data = dataList.get(position);
            if(data instanceof NoStorageItem){
                return TYPE_MESSAGE;
            }
            return TYPE_ITEM;
        }

    }
    /////////////////////////////////////////////////////////////////
    public static class ViewHolder extends RecyclerView.ViewHolder implements ReplaceDialog.OnCompleteListener{
        private final Context context;
        private final OnItemClick onItemClick;
        private Integer bookId;
        private String bookName;
        private Integer placeId;
        private Integer rack;
        private Integer shelf;
        private final TextView book_name;
        private final TextView book_author;
        private final TextView book_rating;
        private final ImageButton replaceBtn;

        public ViewHolder(View itemView, Context context, OnItemClick onItemClick){
            super(itemView);
            this.context = context;
            this.onItemClick = onItemClick;
            book_name = itemView.findViewById(R.id.book_name);
            book_author = itemView.findViewById(R.id.book_author);
            book_rating = itemView.findViewById(R.id.book_rating);
            replaceBtn = itemView.findViewById(R.id.replaceBtn);

            replaceBtn.setOnClickListener(a -> onClickReplace());

        }

        public void bind(final StorageItem data){
            bookId = data.getId();
            bookName = data.getName();
            placeId = data.getPlace();
            rack = data.getRack();
            shelf = data.getShelf();
            book_name.setText(bookName);
            book_author.setText(data.getAuthor());
            book_rating.setText(String.valueOf(data.getRating()));
        }

        private void onClickReplace(){
            ReplaceDialog dialog = new ReplaceDialog();
            Bundle args = new Bundle();
            args.putInt("bookId", bookId);
            args.putString("bookName", bookName);
            args.putInt("placeId", placeId);
            args.putInt("rack", rack);
            args.putInt("shelf", shelf);
            dialog.setArguments(args);
            FragmentTransaction transaction = manager.beginTransaction();
            dialog.show(transaction, "dialog");
        }

        @Override
        public void onCloseDialog(Integer placeId, Integer bookId, Integer rack, Integer shelf) {
            //
        }
    }

    public static class ViewHolderMessage extends RecyclerView.ViewHolder {
        private final Context context;
        private final OnItemClick onItemClick;
        private final TextView message;

        public ViewHolderMessage(@NonNull View itemView, Context context, OnItemClick onItemClick){
            super(itemView);
            this.context = context;
            this.onItemClick = onItemClick;
            message = itemView.findViewById(R.id.noItemsLbl);
        }

        public void bind(final NoStorageItem data) {
            message.setText(data.getMessage());
        }

    }


    /////////////////////////////////////
    interface  OnItemClick{
        void onClick(StorageItem data);
    }
}
