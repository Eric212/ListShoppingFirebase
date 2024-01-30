package com.ericsospedra.listshoppingfirebase.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.ericsospedra.listshoppingfirebase.R;

public class DeletedProductBoxDialogFragment extends DialogFragment{

    private String item;
    public DeletedProductBoxDialogFragment(String s) {
        this.item = s;
    }

    public interface OnDeleteProductListener {
        void OnProductyDeleted(String item);
    }
    private OnDeleteProductListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_delete_list, null);
        final TextView tvDialogDelete = view.findViewById(R.id.tvDialogDelete);
        tvDialogDelete.setText("Quieres borrar la producto "+item);
        builder.setView(view)
                .setTitle("Borrar producto")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (!item.isEmpty() && listener != null) {
                            listener.OnProductyDeleted(item);
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

    public void setOnDeleteProduct(OnDeleteProductListener listener) {
        this.listener = listener;
    }
}
