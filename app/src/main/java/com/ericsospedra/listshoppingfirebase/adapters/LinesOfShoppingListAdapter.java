package com.ericsospedra.listshoppingfirebase.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ericsospedra.listshoppingfirebase.R;
import com.ericsospedra.listshoppingfirebase.interfaces.IOnLongClickListener;
import com.ericsospedra.listshoppingfirebase.models.LinesOfShoppingList;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class LinesOfShoppingListAdapter extends FirestoreRecyclerAdapter<LinesOfShoppingList, LinesOfShoppingListAdapter.LinesOfShoppingListViewHolder> {
    private IOnLongClickListener longListener;

    public LinesOfShoppingListAdapter(FirestoreRecyclerOptions<LinesOfShoppingList> options, IOnLongClickListener longListener) {
        super(options);
        this.longListener = longListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull LinesOfShoppingListAdapter.LinesOfShoppingListViewHolder holder, int position, @NonNull LinesOfShoppingList model) {
        holder.onBindLineOfShoppingList(model);
    }

    @NonNull
    @Override
    public LinesOfShoppingListAdapter.LinesOfShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinesOfShoppingListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line_of_shopping_list, parent, false));
    }

    public class LinesOfShoppingListViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private final ImageView ivLineOfShoppingList;
        private final TextView tvLineOfShoppingList;

        public LinesOfShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLineOfShoppingList = itemView.findViewById(R.id.ivLineOfShoppingList);
            tvLineOfShoppingList = itemView.findViewById(R.id.tvLineOfShoppingList);
            itemView.setOnLongClickListener(this);
        }

        public void onBindLineOfShoppingList(LinesOfShoppingList model) {
            ivLineOfShoppingList.setImageResource(itemView.getContext().getResources().getIdentifier(model.getImage(), "drawable", itemView.getContext().getPackageName()));
            tvLineOfShoppingList.setText(model.getName());
        }

        @Override
        public boolean onLongClick(View v) {
            if(longListener != null){
                longListener.onLongClick(getSnapshots().getSnapshot(getBindingAdapterPosition()).getId());
                return true;
            }else {
                return false;
            }
        }
    }
}
