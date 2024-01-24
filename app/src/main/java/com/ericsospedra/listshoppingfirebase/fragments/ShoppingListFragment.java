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
import com.ericsospedra.listshoppingfirebase.adapters.ShoppingListAdapter;
import com.ericsospedra.listshoppingfirebase.interfaces.IOnClickListener;
import com.ericsospedra.listshoppingfirebase.models.ShoppingList;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ShoppingListFragment extends Fragment {
    private FirebaseFirestore firebase;
    private ShoppingListAdapter adapter;
    private IOnClickListener listener;

    public ShoppingListFragment() {
        super(R.layout.shoping_list_fragment);
    }

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
        firebase = FirebaseFirestore.getInstance();
        RecyclerView rvShoppingList = view.findViewById(R.id.rvShoppingList);
        CollectionReference ref = firebase.collection("ShoppingLists");
        Query query = ref.orderBy("name");
        FirestoreRecyclerOptions<ShoppingList> options = new FirestoreRecyclerOptions.Builder<ShoppingList>().setQuery(query, ShoppingList.class).build();
        adapter = new ShoppingListAdapter(options, listener);
        rvShoppingList.setAdapter(adapter);
        rvShoppingList.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (IOnClickListener) context;
    }
}
