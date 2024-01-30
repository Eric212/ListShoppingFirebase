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
import com.ericsospedra.listshoppingfirebase.models.Product;
import com.ericsospedra.listshoppingfirebase.utils.ResourceChecker;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ProductAdapter extends FirestoreRecyclerAdapter<Product, ProductAdapter.ProductViewHolder> {
    private final IOnClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProductAdapter(@NonNull FirestoreRecyclerOptions<Product> options, IOnClickListener listener) {
        super(options);
        this.listener = listener;
    }


    @Override
    protected void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position, @NonNull Product model) {
        holder.onBindProduct(model);
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false));
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

        public void onBindProduct(Product p) {
            if(ResourceChecker.existeRecursoPorNombre(itemView.getContext(),p.getImage(),"drawable")) {
                ivProduct.setImageResource(itemView.getContext().getResources().getIdentifier(p.getImage(), "drawable", itemView.getContext().getPackageName()));
            }else{
                ivProduct.setImageResource(R.drawable.default_food);
            }
            tvProduct.setText(p.getName());
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getSnapshots().getSnapshot(getBindingAdapterPosition()).toObject(Product.class).getName());
        }
    }
}
