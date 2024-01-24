package com.ericsospedra.listshoppingfirebase.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ericsospedra.listshoppingfirebase.R;
import com.ericsospedra.listshoppingfirebase.models.Category;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CategoryAdapterDialog extends FirestoreRecyclerAdapter<Category, CategoryAdapterDialog.CategoryDialogViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private final EditText categoryName;

    public CategoryAdapterDialog(@NonNull FirestoreRecyclerOptions<Category> options, EditText categoryName) {
        super(options);
        this.categoryName = categoryName;
    }

    @Override
    protected void onBindViewHolder(@NonNull CategoryAdapterDialog.CategoryDialogViewHolder holder, int position, @NonNull Category model) {
        holder.onBindCategoryDialog(model);
    }

    @NonNull
    @Override
    public CategoryAdapterDialog.CategoryDialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryDialogViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false), categoryName);
    }

    public class CategoryDialogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView ivCategory;
        private final TextView tvCategory;

        public CategoryDialogViewHolder(@NonNull View itemView, EditText categoryName) {
            super(itemView);
            ivCategory = itemView.findViewById(R.id.ivCategory);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }

        public void onBindCategoryDialog(Category model) {
            ivCategory.setImageResource(itemView.getContext().getResources().getIdentifier(model.getImage(), "drawable", itemView.getContext().getPackageName()));
            tvCategory.setText(model.getName());
        }

        @Override
        public void onClick(View v) {
            TextView textView = v.findViewById(R.id.tvCategory);
            categoryName.setText(textView.getText());
        }
    }
}
