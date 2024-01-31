package com.ericsospedra.listshoppingfirebase.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ericsospedra.listshoppingfirebase.R;
import com.ericsospedra.listshoppingfirebase.adapters.ProductAdapter;
import com.ericsospedra.listshoppingfirebase.interfaces.IOnClickListener;
import com.ericsospedra.listshoppingfirebase.interfaces.IOnLongClickListener;
import com.ericsospedra.listshoppingfirebase.models.Category;
import com.ericsospedra.listshoppingfirebase.models.Product;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.Objects;


public class ProductFragment extends Fragment {

    private IOnLongClickListener longListener;

    public interface IOnAttach{
        String getCategoryId();
    }

    private FirebaseFirestore firebase;
    private ProductAdapter adapter;
    private IOnClickListener listener;

    private String categoryId;

    public ProductFragment() {
        super(R.layout.product_fragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.stopListening();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebase = FirebaseFirestore.getInstance();
        RecyclerView rvProducts = view.findViewById(R.id.rvProduct);
        CollectionReference ref = firebase.collection("Categories").document(categoryId).collection("Products");
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>().setQuery(ref, new SnapshotParser<Product>() {
            @NonNull
            @Override
            public Product parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                if(snapshot.getData()!=null){
                    return snapshot.toObject(Product.class);
                }
                return null;
            }
        }).build();
        adapter = new ProductAdapter(options, listener,longListener);
        rvProducts.setAdapter(adapter);
        rvProducts.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttach iOnAttach = (IOnAttach) context;
        listener = (IOnClickListener) context;
        longListener = (IOnLongClickListener) context;
        categoryId = iOnAttach.getCategoryId();
    }
}
