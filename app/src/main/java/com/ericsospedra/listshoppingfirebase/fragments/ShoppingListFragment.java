package com.ericsospedra.listshoppingfirebase.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ericsospedra.listshoppingfirebase.R;
import com.ericsospedra.listshoppingfirebase.adapters.ShoppingListAdapter;
import com.ericsospedra.listshoppingfirebase.models.ShoppingList;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class ShoppingListFragment extends Fragment {
    
    FirebaseFirestore firebase;
    ShoppingListAdapter adapter;

    public ShoppingListFragment() {
        super(R.layout.shoping_list_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebase = FirebaseFirestore.getInstance();
        RecyclerView rvShoppingList = view.findViewById(R.id.rvShoppingList);
        CollectionReference ref = firebase.collection("ShoppingList");
        FirestoreRecyclerOptions<ShoppingList> options = new FirestoreRecyclerOptions.Builder<ShoppingList>().setQuery(ref, ShoppingList.class).build();
        adapter = new ShoppingListAdapter(options);
        rvShoppingList.setAdapter(adapter);
        rvShoppingList.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        adapter.startListening();
    }
}
