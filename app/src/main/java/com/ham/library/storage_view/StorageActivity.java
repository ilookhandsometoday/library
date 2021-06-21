package com.ham.library.storage_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ham.library.BaseActivity;
import com.ham.library.R;
import com.ham.library.dao.LibraryViewModel;
import com.ham.library.dao.entity.BookEntity;
import com.ham.library.dao.entity.Place;
import com.ham.library.dao.entity.PlaceEntity;
import com.shawnlin.numberpicker.NumberPicker;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

public class StorageActivity extends BaseActivity implements ReplaceDialog.OnCompleteReplaceListener, AddBookDialog.OnCompleteAddingListener {

    private NumberPicker rackNum;
    private NumberPicker shelfNum;
    private static Integer selectedRack = 1;
    private static Integer selectedShelf = 1;
    private static FragmentManager manager;

    private ProgressBar pb;
    private Button addBookBtn;
    private RecyclerView storageView;
    private ItemAdapter adapter;
    private List<StorageItem> list;
    private List<BookEntity> booksNoShelfList;

    private Button plusRack;
    private Button minusRack;
    private Button plusShelf;
    private Button minusShelf;

    private static List<Integer[]> shelves;

    private Integer maxId = null;

    private ArrayList<Toast> allToasts = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        loadData();

        rackNum = findViewById(R.id.rackNum);
        rackNum.setFadingEdgeEnabled(true);// Set fading edge enabled
        rackNum.setScrollerEnabled(true);// Set scroller enabled
        rackNum.setWrapSelectorWheel(true);// Set wrap selector wheel
        rackNum.setAccessibilityDescriptionEnabled(true);/// Set accessibility description enabled
        rackNum.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if(oldVal!=newVal) {
                selectedRack = newVal;
                setNumberPickers();
                initData();

            }
        });
        shelfNum = findViewById(R.id.shelfNum);
        shelfNum.setFadingEdgeEnabled(true);// Set fading edge enabled
        shelfNum.setScrollerEnabled(true);// Set scroller enabled
        shelfNum.setWrapSelectorWheel(true);// Set wrap selector wheel
        shelfNum.setAccessibilityDescriptionEnabled(true);/// Set accessibility description enabled
        shelfNum.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if(oldVal!=newVal) {
                selectedShelf = newVal;
                setNumberPickers();
                initData();
            }
        });
        pb = findViewById(R.id.progressBar);
        addBookBtn = findViewById(R.id.addBookBtn);
        addBookBtn.setOnClickListener(a -> onAddBookClick());
        manager = getSupportFragmentManager();

        storageView = findViewById(R.id.storageView);
        storageView.setLayoutManager(new LinearLayoutManager(this));
        storageView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new ItemAdapter(this::onStorageItemClick);

        plusRack = findViewById(R.id.rackPlusBtn);
        plusRack.setOnClickListener(a -> onPlusClick(1));
        minusRack = findViewById(R.id.rackMinusBtn);
        minusRack.setOnClickListener(a -> onRackMinusClick());
        plusShelf = findViewById(R.id.shelfPlusBtn);
        plusShelf.setOnClickListener(a -> onPlusClick(0));
        minusShelf = findViewById(R.id.shelfMinusBtn);
        minusShelf.setOnClickListener(a -> onShelfMinusClick());

        setNumberPickers();
        initData();
    }

    private void createNewShelves(){
        shelves= new ArrayList<>();
        for(int i = 1; i<5; i++) {
            Integer[] mas = {i, 3};
            shelves.add(mas);
        }
        saveData();
    }

    private void initData(){
        list = new ArrayList<>();
        getBookList();
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                if(allToasts.size()!=0) killAllToast();
            }
        }.start();
    }

    private void onPlusClick(int i) {
        Integer[] item;
        if(i == 1) {
            item = new Integer[]{getMaxRack() + 1, 1};
            shelves.add(item);
            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.SA_rackAdded),
                    Toast.LENGTH_LONG);
            allToasts.add(t);
            t.show();
        }
        else {
            int newMaxShelf =  getMaxShelfByRack(selectedRack) + 1;
            for (Integer[] it: shelves) {
                if(it[0] == selectedRack) it[1] = newMaxShelf;
            }
            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.SA_shelfAdded),
                    Toast.LENGTH_LONG);
            allToasts.add(t);
            t.show();
        }
        setNumberPickers();
        saveData();
        initData();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onRackMinusClick() {
        if(getMaxRack() == 1) {
            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.SA_lastRack),
                    Toast.LENGTH_LONG);
            allToasts.add(t);
            t.show();
            return;
        }
        int rack = getMaxRack();
        int maxshelf = getMaxShelfByRack(rack);
        List<Place> places = new ArrayList<>();
        for (int i = 1; i < maxshelf + 1; i++)
            mainViewModel.getPlacesByBookcaseAndShelf(rack, i).observe(this, bookEntities -> {
                if (bookEntities.size() > 0)
                    for (PlaceEntity pl : bookEntities) {
                        places.add(pl.placeEntity);
                    }
            });
        new CountDownTimer(500, 1000) {
            public void onTick(long millisUntilFinished) {
                storageView.setVisibility(RecyclerView.INVISIBLE);
                pb.setVisibility(ProgressBar.VISIBLE);
            }
            public void onFinish() {
                if (places.size() != 0) {
                    for (Place it : places)
                        Executors.newSingleThreadExecutor().execute(() -> mainViewModel.deletePlace(it));
                    new CountDownTimer(500, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        public void onFinish() {
                            storageView.setVisibility(RecyclerView.VISIBLE);
                            pb.setVisibility(ProgressBar.INVISIBLE);
                            shelves.removeIf(value -> value[0] == rack && value[1] == maxshelf);
                            if (getMaxRack() < selectedRack) {
                                selectedRack = getMaxRack();
                                selectedShelf = 1;
                            }
                            setNumberPickers();
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.SA_rackDeleted),
                                    Toast.LENGTH_LONG);
                            allToasts.add(t);
                            t.show();
                            saveData();
                            initData();
                        }
                    }.start();
                }
                else {
                    storageView.setVisibility(RecyclerView.VISIBLE);
                    pb.setVisibility(ProgressBar.INVISIBLE);
                    removeRack(rack, maxshelf);
                }
            }
        }.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void removeRack(int rack, int maxShelf){
        for(int i = 1; i<maxShelf+1; i++) {
            int finalI = i;
            shelves.removeIf(value -> value[0] == rack && value[1] == finalI);
        }
        if (getMaxRack() < selectedRack) {
            selectedRack = getMaxRack();
            selectedShelf = 1;
        }
        setNumberPickers();
        Toast t = Toast.makeText(getApplicationContext(), getString(R.string.SA_rackDeleted),
                Toast.LENGTH_LONG);
        allToasts.add(t);
        t.show();
        saveData();
        initData();
    }

    private void onShelfMinusClick() {
        if(getMaxShelfByRack(selectedRack) == 1) {
            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.SA_lastShelf),
                    Toast.LENGTH_LONG);
            allToasts.add(t);
            t.show();
            return;
        }
        int maxShelf = getMaxShelfByRack(selectedRack);
        List<Place> places = new ArrayList<>();
            mainViewModel.getPlacesByBookcaseAndShelf(selectedRack, maxShelf).observe(this, bookEntities -> {
                if (bookEntities.size() > 0)
                    for (PlaceEntity pl : bookEntities) {
                        places.add(pl.placeEntity);
                    }
            });
        new CountDownTimer(500, 1000) {
            public void onTick(long millisUntilFinished) {
                storageView.setVisibility(RecyclerView.INVISIBLE);
                pb.setVisibility(ProgressBar.VISIBLE);
            }
            public void onFinish() {
                if (places.size() != 0) {
                    for (Place it : places)
                        Executors.newSingleThreadExecutor().execute(() -> mainViewModel.deletePlace(it));

                    new CountDownTimer(500, 1000) {
                        public void onTick(long millisUntilFinished) { }
                        public void onFinish() { removeShelf(); }
                    }.start();
                }
                else removeShelf();
            }
        }.start();
    }

    private void removeShelf(){
        int shelf = getMaxShelfByRack(selectedRack) - 1;
        for (Integer[] i : shelves) {
            if (i[0] == selectedRack) i[1] = shelf;
        }
        if (getMaxShelfByRack(selectedRack) < selectedShelf)
            selectedShelf = getMaxShelfByRack(selectedRack);
        setNumberPickers();
        storageView.setVisibility(RecyclerView.VISIBLE);
        pb.setVisibility(ProgressBar.INVISIBLE);
        Toast t = Toast.makeText(getApplicationContext(), getString(R.string.SA_shelfDeleted),
                Toast.LENGTH_LONG);
        allToasts.add(t);
        t.show();
        saveData();
        initData();
    }

    private void setNumberPickers(){
        rackNum.setMaxValue(getMaxRack());
        rackNum.setMinValue(1);
        rackNum.setValue(selectedRack);
        String[] racks = new String[getMaxRack()];
        for (int i = 0; i<racks.length; i++) {
            racks[i]= String.valueOf(i + 1);
        }
        rackNum.setDisplayedValues(racks);

        shelfNum.setMaxValue(getMaxShelfByRack(selectedRack));
        shelfNum.setMinValue(1);
        shelfNum.setValue(selectedShelf);
        String[] shelvesMas = new String[getMaxShelfByRack(selectedRack)];
        for (int i = 0; i<shelvesMas.length; i++) {
            shelvesMas[i]= String.valueOf(i + 1);
        }
        shelfNum.setDisplayedValues(shelvesMas);
    }



    private void onAddBookClick(){
        mainViewModel.getAllBooksNoShelf().observe(this, bookEntities -> {
            booksNoShelfList = new ArrayList<>();
            if(bookEntities.size()!=0) {
                for (BookEntity bookEnt:bookEntities) {
                    BookEntity be = new BookEntity();
                    be.id=bookEnt.id;
                    be.author=bookEnt.author;
                    be.title=bookEnt.title;
                    booksNoShelfList.add(be);
                }
                String[] booksNameList = new String[booksNoShelfList.size()];
                int i = 0;
                for(BookEntity b : booksNoShelfList) {
                    booksNameList[i] = b.author + " «" + b.title + "»";
                    i++;
                }

                AddBookDialog dialog = new AddBookDialog();
                Bundle args = new Bundle();
                args.putStringArray("books", booksNameList);
                dialog.setArguments(args);
                FragmentTransaction transaction = manager.beginTransaction();
                dialog.show(transaction, "add book dialog");
            }
            else {
                Toast t = Toast.makeText(getApplicationContext(), getString(R.string.SA_noBooksToAdd),
                        Toast.LENGTH_LONG);
                allToasts.add(t);
                t.show();
            }
        });
    }


    private void getBookList(){
        mainViewModel.getPlacesByBookcaseAndShelf(selectedRack,selectedShelf).observe(this, placeEntities -> {
            for(PlaceEntity plEnt: placeEntities) {
                if (plEnt != null) list.add(createItem(plEnt));
            }
            if(list.size() == 0) {
                NoStorageItem noItems = new NoStorageItem();
                noItems.setMessage(getString(R.string.SA_nobooks));
                list.add(noItems);
            }
            adapter.setDataList(list);
            storageView.setAdapter(adapter);
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

    private Integer getMaxRack(){
        Integer max = -1;
        for (Integer[] i : shelves) {
            if(max == -1) max = i[0];
            else if (i[0] > max) max=i[0];
        }
        return max;
    }

    private Integer getMaxShelfByRack(Integer rack){
        for (Integer[] i:shelves) {
            if (i[0] == rack) return i[1];
        }
        return 0;
    }


    private void onStorageItemClick(StorageItem storageItem) { }

    @Override
    public void onCloseReplaceDialog(Integer place, Integer book, Integer rack, Integer shelf) {
        Log.d("mydb", "got Rack "+rack+" and Shelf "+ shelf);
        Place p = new Place();
        p.id = place;
        p.bookID = book;
        p.bookcase = rack;
        p.shelf = shelf;
        Executors.newSingleThreadExecutor().execute(() -> mainViewModel.updatePlace(p));
        startTimer();
    }

    @Override
    public void onCloseAddingDialog(Integer bookId) {
        mainViewModel.getAllPlaces().observe(this, places -> {
            int counter = 0;
            for(Place pl: places) {
                if (pl != null && counter == 0) {
                    maxId = pl.id;
                    counter++;
                }
                else if(pl != null && maxId < pl.id) maxId=pl.id;
            }
        });

        new CountDownTimer(200, 1000) {
            public void onTick(long millisUntilFinished) {
                storageView.setVisibility(RecyclerView.INVISIBLE);
                pb.setVisibility(ProgressBar.VISIBLE);
            }
            public void onFinish() {
                Place p = new Place();
                if(maxId == null) p.id = 1;
                else p.id = maxId + 1;
                p.bookID = booksNoShelfList.get(bookId).id;
                p.bookcase = selectedRack;
                p.shelf = selectedShelf;
                Executors.newSingleThreadExecutor().execute(() -> mainViewModel.insertPlace(p));
                startTimer();
            }
        }.start();
    }

    private void startTimer(){
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

    private void killAllToast(){
        for(Toast t:allToasts){
            if(t!=null) {
                t.cancel();
            }
        }
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
    public static class ViewHolder extends RecyclerView.ViewHolder implements ReplaceDialog.OnCompleteReplaceListener{
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
            args.putIntArray("racks", createRacksArr());
            args.putIntArray("shelves", createShelvesArr());
            dialog.setArguments(args);
            FragmentTransaction transaction = manager.beginTransaction();
            dialog.show(transaction, "dialog");
        }

        private int[] createShelvesArr(){
            int[] shelvesArr = new int[shelves.size()];
            int counter = 0;
            for (Integer[] item : shelves){
                shelvesArr[counter]=item[1];
                counter++;
            }
            return shelvesArr;
        }

        private int[] createRacksArr(){
            int[] racksArr = new int[shelves.size()];
            int counter = 0;
            for (Integer[] item : shelves){
                racksArr[counter]=item[0];
                counter++;
            }
            return racksArr;
        }


        @Override
        public void onCloseReplaceDialog(Integer placeId, Integer bookId, Integer rack, Integer shelf) {
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


    /////////////////////////////////////////////
    private void saveData() {
        if(sharedPreferences.contains("shelves"))
            removeValue("shelves");
        SharedPreferences.Editor editor = sharedPreferences.edit(); // creating a variable for editor to store data in shared preferences.
        Gson gson = new Gson(); // creating a new variable for gson.
        String json = gson.toJson(shelves); // getting data from gson and storing it in a string.
        editor.putString("shelves", json); // below line is to save data in shared prefs in the form of string.
        editor.apply(); // below line is to apply changes and save data in shared prefs.
    }

    private  void removeValue(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    private void loadData() {
        Gson gson = new Gson();// creating a variable for gson.
        String json = sharedPreferences.getString("shelves", null); // below line is to get to string present from our shared prefs if not present setting it as null.
        Type type = new TypeToken<List<Integer[]>>() {}.getType();// below line is to get the type of our array list.
        shelves = gson.fromJson(json, type); // in below line we are getting data from gson and saving it to our array list
        if (shelves == null) { // checking below if the array list is empty or not
            createNewShelves();
        }
    }
}
