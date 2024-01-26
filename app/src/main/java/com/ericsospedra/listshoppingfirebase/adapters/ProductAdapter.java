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
import com.ericsospedra.listshoppingfirebase.models.Product;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ProductAdapter extends FirestoreRecyclerAdapter<Category,ProductAdapter.ProductViewHolder> {
    private final IOnClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProductAdapter(@NonNull FirestoreRecyclerOptions<Category> options,IOnClickListener listener) {
        super(options);
        this.listener = listener;
    }


    @Override
    protected void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position, @NonNull Category model) {
        if (model.getProductList() != null) {
            holder.onBindProduct(model.getProductList().get(position), model);
        }
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false));
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivProduct;
        private TextView tvProduct;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvProduct = itemView.findViewById(R.id.tvProduct);
            itemView.setOnClickListener(this);
        }

        public void onBindProduct(Product product,Category model) {
            ivProduct.setImageResource(itemView.getContext().getResources().getIdentifier(model.getImage(),"drawable",itemView.getContext().getPackageName()));
            tvProduct.setText(product.getName());
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getSnapshots().getSnapshot(getBindingAdapterPosition()).toObject(Product.class).getName());
        }
    }
}
