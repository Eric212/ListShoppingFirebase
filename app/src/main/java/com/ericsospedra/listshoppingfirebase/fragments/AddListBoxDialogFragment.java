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


public class AddListBoxDialogFragment extends DialogFragment {

    public interface OnListAddedListener {
        void onListAdded(String listName);
    }

    private OnListAddedListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_list, null);

        final EditText etListName = view.findViewById(R.id.etAddCategory);

        builder.setView(view)
                .setTitle("Nueva Lista")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String item = etListName.getText().toString().trim();
                        if (!item.isEmpty() && listener != null) {
                            listener.onListAdded(item);
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

    public void setOnListAddedListener(OnListAddedListener listener) {
        this.listener = listener;
    }
}
