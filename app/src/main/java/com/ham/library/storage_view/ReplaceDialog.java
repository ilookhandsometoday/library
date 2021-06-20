package com.ham.library.storage_view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.ham.library.R;
import com.ham.library.dao.LibraryViewModel;
import com.ham.library.dao.entity.Place;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class ReplaceDialog extends DialogFragment {

    private Integer placeId = 0;
    private Integer bookId = 0;
    private String bookName;
    private NumberPicker rackNum;
    private NumberPicker shelfNum;
    private Integer selectedRack = 1;
    private Integer selectedShelf = 1;
    private int[] racks;
    private int[] shelves;

    private Boolean isChanged = false;



    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.replace_dialog, null);
        builder.setView(view);

        bookId = getArguments().getInt("bookId");
        bookName = getArguments().getString("bookName");
        placeId = getArguments().getInt("placeId");
        selectedRack = getArguments().getInt("rack");
        selectedShelf = getArguments().getInt("shelf");
        racks = getArguments().getIntArray("racks");
        shelves = getArguments().getIntArray("shelves");

        rackNum = view.findViewById(R.id.rackNum);
        rackNum.setFadingEdgeEnabled(true);// Set fading edge enabled
        rackNum.setScrollerEnabled(true);// Set scroller enabled
        rackNum.setWrapSelectorWheel(true);// Set wrap selector wheel
        rackNum.setAccessibilityDescriptionEnabled(true);/// Set accessibility description enabled
        rackNum.setOnValueChangedListener((picker, oldVal, newVal) -> {
                selectedRack = newVal;
                isChanged = true;
                setNumPickers();
        });
        shelfNum = view.findViewById(R.id.shelfNum);
        shelfNum.setFadingEdgeEnabled(true);// Set fading edge enabled
        shelfNum.setScrollerEnabled(true);// Set scroller enabled
        shelfNum.setWrapSelectorWheel(true);// Set wrap selector wheel
        shelfNum.setAccessibilityDescriptionEnabled(true);/// Set accessibility description enabled
        shelfNum.setOnValueChangedListener((picker, oldVal, newVal) -> {
                selectedShelf = newVal;
                isChanged = true;
                setNumPickers();
        });

        setNumPickers();
        String title=getString(R.string.SA_dialog_name_1) + bookName + getString(R.string.SA_dialog_name_2);
        String true_message=getString(R.string.SA_dialog_truemes_1) +
                bookName + getString(R.string.SA_dialog_truemes_2);
        String false_message=getString(R.string.SA_dialog_falsemes_1) +
                bookName + getString(R.string.SA_dialog_falsemes_2);
        builder.setTitle(title)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.SA_dialog_subm),
                        (dialog, id) -> {
                    if(isChanged){
                        replaceBook();
                        Toast.makeText(getContext(), true_message,
                                Toast.LENGTH_LONG).show();
                    }
                    else Toast.makeText(getContext(), false_message,
                            Toast.LENGTH_LONG).show();
                    dialog.cancel();
                })
                .setNeutralButton(getString(R.string.SA_dialog_canc),
                        (dialog, id) -> dialog.cancel());
        return builder.create();
    }

    private void replaceBook(){
        this.mListener.onCloseReplaceDialog(placeId, bookId, selectedRack, selectedShelf);
    }

    private void setNumPickers(){
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

    private Integer getMaxRack(){
        Integer max = -1;
        for (int i:racks) {
           // Log.d("mydb", "rack " + i);
            if(max == -1) max = i;
            else if (max < i) max = i;
        }
        return max;
    }

    private Integer getMaxShelfByRack(Integer rack){
        Integer max = -1;
        int pos = -1;
        //Log.d("mydb", "selectedRack "+ rack);
        for (int i = 0; i<racks.length; i++){
          //  Log.d("mydb", "rack[" + i +"] = " + racks[i] );
            if(racks[i] == rack) pos = i;
        }
        for (int i = 0; i<shelves.length; i++){
            //Log.d("mydb", "shelf[" + i +"] = " + shelves[i] );
            if(i == pos) max = shelves[i];
        }
        return max;
    }

    ////////////////
    public interface OnCompleteReplaceListener {
        void onCloseReplaceDialog(Integer placeId, Integer bookId, Integer rack, Integer shelf);
    }

    private OnCompleteReplaceListener mListener;

    // make sure the Activity implemented it
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteReplaceListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }
}
