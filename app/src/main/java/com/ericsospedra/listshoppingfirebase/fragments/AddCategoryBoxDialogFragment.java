package com.ericsospedra.listshoppingfirebase.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.ericsospedra.listshoppingfirebase.R;


public class AddCategoryBoxDialogFragment extends DialogFragment {

    public interface OnCategoryAddedListener {

        void onCategoryAdd(String item);
    }

    private OnCategoryAddedListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_category, null);

        final EditText eCategoryName = view.findViewById(R.id.etAddCategory);

        builder.setView(view)
                .setTitle("Nueva Categoria")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String item = eCategoryName.getText().toString().trim();
                        if (!item.isEmpty() && listener != null) {
                            listener.onCategoryAdd(item);
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    public void setOnListAddedListener(OnCategoryAddedListener listener) {
        this.listener = listener;
    }
}
