package com.ericsospedra.listshoppingfirebase.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ericsospedra.listshoppingfirebase.R;
import com.ericsospedra.listshoppingfirebase.interfaces.IOnClickListener;
import com.ericsospedra.listshoppingfirebase.models.ShoppingList;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ShoppingListAdapter extends FirestoreRecyclerAdapter<ShoppingList, ShoppingListAdapter.ShoppingListViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private IOnClickListener listener;

    public ShoppingListAdapter(@NonNull FirestoreRecyclerOptions<ShoppingList> options, IOnClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ShoppingListAdapter.ShoppingListViewHolder holder, int position, @NonNull ShoppingList model) {
        holder.onBindShoppingList(model);
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShoppingListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_list, parent, false));
    }

    public class ShoppingListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            itemView.setOnClickListener(this);
        }

        public void onBindShoppingList(ShoppingList model) {
            ivShoppingList.setImageResource(itemView.getContext().getResources().getIdentifier(model.getImage(), "drawable", itemView.getContext().getPackageName()));
            tvShoppingList.setText(model.getName());
            tvDate.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(new Date(model.getDate())));
            tvCantidad.setText(String.valueOf(model.getCantidadProductos()));
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick(getSnapshots().getSnapshot(getBindingAdapterPosition()).getId().toString());
            }
        }
    }
}
