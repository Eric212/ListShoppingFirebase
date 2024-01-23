package com.ericsospedra.listshoppingfirebase.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ericsospedra.listshoppingfirebase.R;
import com.ericsospedra.listshoppingfirebase.models.ShoppingList;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShoppingListAdapter extends FirestoreRecyclerAdapter<ShoppingList, ShoppingListAdapter.ShoppingListViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ShoppingListAdapter(@NonNull FirestoreRecyclerOptions<ShoppingList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ShoppingListAdapter.ShoppingListViewHolder holder, int position, @NonNull ShoppingList model) {
        holder.onBindShoppingList(model);
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivShoppingList;
        private final TextView tvShoppingList;
        private final TextView tvDate;
        private final TextView tvCantidad;

        public ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);
            ivShoppingList = itemView.findViewById(R.id.ivShoppingList);
            tvShoppingList = itemView.findViewById(R.id.tvShoppingList);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
        }

        public void onBindShoppingList(ShoppingList model) {
            ivShoppingList.setImageResource(itemView.getContext().getResources().getIdentifier(model.getImage(), "drawable",itemView.getContext().getPackageName()));
            tvShoppingList.setText(model.getName());
            tvDate.setText(model.getDate());
            tvCantidad.setText(String.valueOf(model.getCantidadProductos()));
        }
    }
}
