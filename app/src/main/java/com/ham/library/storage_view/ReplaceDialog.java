package com.ham.library.storage_view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
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

import java.util.concurrent.Executors;

public class ReplaceDialog extends DialogFragment {

    private Integer placeId = 0;
    private Integer bookId = 0;
    private String bookName;
    private NumberPicker rackNum;
    private NumberPicker shelfNum;
    private Integer selectedRack = 1;
    private Integer selectedShelf = 1;

    private Boolean isChanged = false;

    private LibraryViewModel LVM;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.replace_dialog, null);
        builder.setView(view);

        bookId = getArguments().getInt("bookId");
        bookName = getArguments().getString("bookName");
        placeId = getArguments().getInt("placeId");
        selectedRack = getArguments().getInt("rack");
        selectedShelf = getArguments().getInt("shelf");

        LVM = ViewModelProviders.of(this).get(LibraryViewModel.class);
        rackNum = view.findViewById(R.id.rackNum);
        shelfNum = view.findViewById(R.id.shelfNum);
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
        this.mListener.onCloseDialog(placeId, bookId, selectedRack, selectedShelf);
    }

    private void setNumPickers(){
        rackNum.setMaxValue(10);
        rackNum.setMinValue(1);
        rackNum.setValue(selectedRack);
        rackNum.setFadingEdgeEnabled(true);// Set fading edge enabled
        rackNum.setScrollerEnabled(true);// Set scroller enabled
        rackNum.setWrapSelectorWheel(true);// Set wrap selector wheel
        rackNum.setAccessibilityDescriptionEnabled(true);/// Set accessibility description enabled
        rackNum.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if(oldVal!=newVal) {
                isChanged=true;
                selectedRack = newVal;
            }
        });

        shelfNum.setMaxValue(5);
        shelfNum.setMinValue(1);
        shelfNum.setValue(selectedShelf);
        shelfNum.setFadingEdgeEnabled(true);// Set fading edge enabled
        shelfNum.setScrollerEnabled(true);// Set scroller enabled
        shelfNum.setWrapSelectorWheel(true);// Set wrap selector wheel
        shelfNum.setAccessibilityDescriptionEnabled(true);/// Set accessibility description enabled
        shelfNum.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if(oldVal!=newVal) {
                isChanged=true;
                selectedShelf = newVal;
            }
        });
    }

    ////////////////
    public interface OnCompleteListener {
        void onCloseDialog(Integer placeId, Integer bookId, Integer rack, Integer shelf);
    }

    private OnCompleteListener mListener;

    // make sure the Activity implemented it
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }
}
