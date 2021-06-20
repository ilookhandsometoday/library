package com.ham.library.storage_view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.ham.library.R;
import com.ham.library.dao.LibraryViewModel;
import com.ham.library.dao.entity.PlaceEntity;

import java.util.ArrayList;
import java.util.List;

public class AddBookDialog extends DialogFragment {
    private String[] booksList;
    private Integer chosenBook = -1;

    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        booksList = getArguments().getStringArray("books");
        builder.setTitle(getString(R.string.SA_addBookdialod_name))
                // добавляем переключатели
                .setSingleChoiceItems(booksList, -1,
                        (dialog, item) -> chosenBook = item)
                .setPositiveButton(getString(R.string.SA_dialog_subm), (dialog, id) -> {
                    if(chosenBook != -1) {
                        mListener.onCloseAddingDialog(chosenBook);
                        dialog.cancel();
                    }
                    else  Toast.makeText(getContext(), getString(R.string.SA_noBooksChosen),
                            Toast.LENGTH_LONG).show();
                })
                .setNeutralButton(getString(R.string.SA_dialog_canc), (dialog, id) -> dialog.cancel());

        return builder.create();
    }

    ////////////////
    public interface OnCompleteAddingListener {
        void onCloseAddingDialog(Integer bookId);
    }

    private OnCompleteAddingListener mListener;

    // make sure the Activity implemented it
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteAddingListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }
}
