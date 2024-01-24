package com.ericsospedra.listshoppingfirebase.fragments;/*
@author sheila j. nieto 
@version 0.1 2024 -01 - 13
*/

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ericsospedra.listshoppingfirebase.R;
import com.ericsospedra.listshoppingfirebase.adapters.CategoryAdapterDialog;
import com.ericsospedra.listshoppingfirebase.interfaces.IOnClickListenerProduct;
import com.ericsospedra.listshoppingfirebase.models.Category;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class AddProductBoxDialogFragment extends DialogFragment implements IOnClickListenerProduct {

    public interface OnProductAddedListener {

        void onProductAdd(String item);
    }

    private OnProductAddedListener listener;

    public AddProductBoxDialogFragment() {
    }

    private EditText eProductName;
    private EditText eProductCategory;

    private CategoryAdapterDialog adapter;


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvProductDialog = view.findViewById(R.id.rvProductDialog);
        FirebaseFirestore firebase = FirebaseFirestore.getInstance();
        CollectionReference ref = firebase.collection("Categories");
        Query query = ref.orderBy("name");
        FirestoreRecyclerOptions<Category> options = new FirestoreRecyclerOptions.Builder<Category>().setQuery(query, Category.class).build();

        adapter = new CategoryAdapterDialog(options, eProductCategory);

        rvProductDialog.setAdapter(adapter);
        rvProductDialog.setLayoutManager(new GridLayoutManager(view.getContext(), 4, GridLayoutManager.VERTICAL, false));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_product, null);

        eProductName = view.findViewById(R.id.eProductName);
        eProductCategory = view.findViewById(R.id.eProductCategory);

        builder.setView(view)
                .setTitle("Nueva Producto")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String item = eProductName.getText().toString().trim();
                        String item2 = eProductCategory.getText().toString().trim();
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
    public void onClick(String name) {
        eProductCategory.setText(name);
    }
}
