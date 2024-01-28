package com.ericsospedra.listshoppingfirebase.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.ericsospedra.listshoppingfirebase.R;


public class AddProductBoxDialogFragment extends DialogFragment {

    public interface OnProductAddedListener {
        void onProductAdd(String item);
    }

    private OnProductAddedListener listener;
    private EditText eProductName;


    public AddProductBoxDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_product, null);

        eProductName = view.findViewById(R.id.eProductName);

        builder.setView(view)
                .setTitle("Nueva Producto")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String item = eProductName.getText().toString().trim();
                        if (!item.isEmpty() && listener != null) {
                            listener.onProductAdd(item);
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

    public void setOnListAddedListener(OnProductAddedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
