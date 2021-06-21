package com.ham.library;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ham.library.dao.entity.BookEntity;
import com.ham.library.dao.entity.ReviewEntity;
import com.ham.library.dao.model.Book;
import com.ham.library.dao.model.Review;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Compilations extends BaseActivity{

    Spinner spinner;
    List<Book> booksList = new ArrayList<>();
    Boolean check = true;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    ArrayAdapter<Options> adapterOps;
    private RatingBar ratingBar;
    int ratingValue =0;
    int mode=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compilation_activity);

        spinner = findViewById(R.id.spinner);
        List<Options> groups = new ArrayList<>();
        initOptionsList(groups);

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new ItemAdapter(this::onBookItemClick);


        ratingBar = findViewById(R.id.ratingBar2);
        addListenerOnRatingBar();

        adapterOps = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapterOps.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapterOps);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View itemSelected,
                                       int selectedItemPosition, long id) {
                onSpinnerItemSelected(selectedItemPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        initData();
        recyclerView.setAdapter(adapter);
    }

    private void onBookItemClick(Book scheduleItem) { }

    public void addListenerOnRatingBar() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ratingValue = (int)rating;
                initData();
            }
        });
    }

    private void initData(){
        mainViewModel.getBooks().observe(this, new Observer<List<BookEntity>>() {
            @Override
            public void onChanged(List<BookEntity> books) {
                booksList = getItems(books);
                adapter.setDataList(booksList);
                recyclerView.setAdapter(adapter);
            }
        });

    }

    private List<Book> getItems(List<BookEntity> BooksEntities) {
        List<Book> list = new ArrayList<>();
        for (BookEntity book : BooksEntities) {
            if(book.rating>=ratingValue) {
                checkDate(book.id);
                if(check) {
                    list.add(new Book(book.id, book.title, book.author, book.rating));
                }
            }
        }

        return list;
    }

    private void checkDate (int id) {
        mainViewModel.getLastReview(id).observe(this, new Observer<List<ReviewEntity>>() {
            @Override
            public void onChanged(List<ReviewEntity> reviewEntities) {
                for (ReviewEntity rev: reviewEntities){
                    switch (mode){
                        case 1: {
                            if (rev.reviewEntity.timestamp.after(ceilWeek(new Date()))){
                                check = true;
                            }else {check = false;}
                        }break;
                        case 2: {
                            if (rev.reviewEntity.timestamp.after(ceilMonth(new Date()))){
                                check = true;
                            }else {check = false;}
                        }break;
                        case 3: {
                            if (rev.reviewEntity.timestamp.after(ceilYear(new Date()))){
                                check = true;
                            }else {check = false;}
                        }break;
                    }
                }

            }
        });
    }
//
    private Date ceilMonth(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        calendar.add(Calendar.MONTH, -1);

        return calendar.getTime();
    }

    private Date ceilYear(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        calendar.add(Calendar.YEAR, -1);

        return calendar.getTime();
    }

    private Date ceilWeek(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        calendar.add(Calendar.DATE, -7);

        return calendar.getTime();
    }

    private void onSpinnerItemSelected(int selected) {
        mode = selected;
        initData();
    }

    private void initOptionsList(List<Options> list) {
        list.add(new Options(1, "неделю"));
        list.add(new Options(2, "месяц"));
        list.add(new Options(3, "год"));
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
    static class Options {
        int id;
        String name;

        public Options(int id, String name) {
            this.id = id;
            this.name = name;
        }
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setId(Integer id){
            this.id = id;
        }

        @Override
        public String toString() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    public final static class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<Book> dataList = new ArrayList<>();
        private OnItemClick onItemClick;

        public ItemAdapter(OnItemClick onItemClick) {this.onItemClick = onItemClick;}

        public void setDataList(List<Book> list) {
            dataList = list;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.book_item_layout, parent, false);
            return new ViewHolder(contactView, context, onItemClick);
        }


        @Override
        public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, int position){
            Book data = dataList.get(position);
            if(viewHolder instanceof ViewHolder){
                ((ViewHolder) viewHolder).bind(data);
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private OnItemClick onItemClick;
        private TextView name;
        private TextView author;
        private TextView rating;

        public ViewHolder(View itemView, Context context, OnItemClick onItemClick){
            super(itemView);
            this.context = context;
            this.onItemClick = onItemClick;
            name = itemView.findViewById(R.id.ItemBookName);
            author = itemView.findViewById(R.id.ItemBookAuthor);
            rating = itemView.findViewById(R.id.ItemBookRating);
        }

        public void bind(final Book data){
            name.setText('"'+data.getTitle()+'"');
            author.setText(data.getAuthor());
            rating.setText("Рейтинг:"+data.getRating());
        }
    }

    interface  OnItemClick{
        void onClick(Book data);
    }
}
