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
import com.ericsospedra.listshoppingfirebase.models.Category;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CategoryAdapter extends FirestoreRecyclerAdapter<Category,CategoryAdapter.CategoryViewHolder> {
    private final IOnClickListener listener;

    public CategoryAdapter(FirestoreRecyclerOptions<Category> options,IOnClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position, @NonNull Category model) {
        holder.onBindCategory(model);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView ivCategory;
        private final TextView tvCategory;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategory = itemView.findViewById(R.id.ivCategory);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            itemView.setOnClickListener(this);
        }

        public void onBindCategory(Category model) {
            if (model.getImage().equals("bebidas alcoholicas")){
                ivCategory.setImageResource(itemView.getContext().getResources().getIdentifier("bebidas_alcoholicas","drawable",itemView.getContext().getPackageName()));
            }else {
                ivCategory.setImageResource(itemView.getContext().getResources().getIdentifier(model.getImage(), "drawable", itemView.getContext().getPackageName()));
            }
            tvCategory.setText(model.getName());
        }

        @Override
        public void onClick(View v) {
            if(listener != null){
                listener.onClick(getSnapshots().getSnapshot(getBindingAdapterPosition()).getId());
            }
        }
    }
}
