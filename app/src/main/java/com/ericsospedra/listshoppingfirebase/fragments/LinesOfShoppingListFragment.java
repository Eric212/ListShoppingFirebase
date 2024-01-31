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
import com.ericsospedra.listshoppingfirebase.adapters.LinesOfShoppingListAdapter;
import com.ericsospedra.listshoppingfirebase.interfaces.IOnClickListener;
import com.ericsospedra.listshoppingfirebase.interfaces.IOnLongClickListener;
import com.ericsospedra.listshoppingfirebase.models.LinesOfShoppingList;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class LinesOfShoppingListFragment extends Fragment {
    private IOnLongClickListener longListener;
    private IOnClickListener listener;

    public interface IOnAttach {
        String getListId();
    }

    private String shoppingListId;
    private FirebaseFirestore firebase;

    private LinesOfShoppingListAdapter adapter;

    public LinesOfShoppingListFragment() {
        super(R.layout.line_of_shopping_list_fragment);
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
        RecyclerView rvLineOfShoppingList = view.findViewById(R.id.rvLineOfShoppingList);
        CollectionReference ref = firebase.collection("ShoppingLists").document(shoppingListId).collection("LinesOfShoppingList");
        FirestoreRecyclerOptions<LinesOfShoppingList> options = new FirestoreRecyclerOptions.Builder<LinesOfShoppingList>().setQuery(ref, LinesOfShoppingList.class).build();
        adapter = new LinesOfShoppingListAdapter(options,longListener,listener);
        rvLineOfShoppingList.setAdapter(adapter);
        rvLineOfShoppingList.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttach iOnAttach = (IOnAttach) context;
        shoppingListId = iOnAttach.getListId();
        listener = (IOnClickListener) context;
        longListener = (IOnLongClickListener) context;
    }
}
