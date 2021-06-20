package com.ham.library.books_view;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ham.library.BaseActivity;
import com.ham.library.R;
import com.ham.library.dao.entity.BookEntity;
import com.ham.library.dao.model.Book;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class BooksActivity extends BaseActivity {
    private RecyclerView booksRecyclerView;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        this.booksRecyclerView = findViewById(R.id.booksRecyclerView);
        this.booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.booksRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        this.adapter = new ItemAdapter();
        this.booksRecyclerView.setAdapter(this.adapter);

        initData();
    }

    public void initData(){
        this.mainViewModel.getBooks().observe(this, new Observer<List<BookEntity>>(){

            @Override
            public void onChanged(List<BookEntity> bookEntities) {
                ArrayList<Book> booksFromBD = new ArrayList<>();
                for (BookEntity listEntity : bookEntities) {
                    booksFromBD.add(new Book(listEntity.id, listEntity.title, listEntity.author, listEntity.rating));
                }
                adapter.setDataList(booksFromBD);
            }
        });
    }

    public final static class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private ArrayList<Book> dataList = new ArrayList<>();

        public static class ViewHolder extends RecyclerView.ViewHolder{
            private TextView name;
            private TextView author;
            private TextView rating;
            public ViewHolder(View itemView){
                super(itemView);
                this.name = itemView.findViewById(R.id.ItemBookName);
                this.author = itemView.findViewById(R.id.ItemBookAuthor);
                this.rating = itemView.findViewById(R.id.ItemBookRating);
            }

            public void bind(final Book data){
                this.name.setText(data.getTitle());
                this.author.setText(data.getAuthor());
                this.rating.setText("Рейтинг:" + data.getRating());
            }
        }

        public void setDataList(ArrayList<Book> dataList) {
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
            View contactView = inflater.inflate(R.layout.book_item_layout, parent, false);
            return new ViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
            Book data = this.dataList.get(position);
            ((ViewHolder) holder).bind(data);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }
}
