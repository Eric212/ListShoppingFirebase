package com.ericsospedra.listshoppingfirebase.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ericsospedra.listshoppingfirebase.R;
import com.ericsospedra.listshoppingfirebase.adapters.CategoryAdapter;
import com.ericsospedra.listshoppingfirebase.adapters.ProductAdapter;
import com.ericsospedra.listshoppingfirebase.interfaces.IOnClickListener;
import com.ericsospedra.listshoppingfirebase.models.Category;
import com.ericsospedra.listshoppingfirebase.models.ShoppingList;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ProductFragment extends Fragment {

    public interface IOnAttach{
        String getCategoryId();
    }

    private FirebaseFirestore firebase;
    private ProductAdapter adapter;
    private IOnClickListener listener;

    private String categoryId;
    private FirebaseUser firebaseUser;

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
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        super.onViewCreated(view, savedInstanceState);
        firebase = FirebaseFirestore.getInstance();
        RecyclerView rvProducts = view.findViewById(R.id.rvProduct);
        CollectionReference ref = firebase.collection("Categories");
        Query query = ref.whereEqualTo(FieldPath.documentId(),categoryId);
        FirestoreRecyclerOptions<Category> options = new FirestoreRecyclerOptions.Builder<Category>().setQuery(query, new SnapshotParser<Category>() {
            @NonNull
            @Override
            public Category parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                if(snapshot.getId().equals(categoryId)){
                    Category c = snapshot.toObject(Category.class);
                    return c;
                }
                return null;
            }
        }).build();
        adapter = new ProductAdapter(options, listener);
        rvProducts.setAdapter(adapter);
        rvProducts.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttach iOnAttach = (IOnAttach) context;
        listener = (IOnClickListener) context;
        categoryId = iOnAttach.getCategoryId();
    }
}
