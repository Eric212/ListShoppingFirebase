package com.ericsospedra.listshoppingfirebase.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ericsospedra.listshoppingfirebase.models.Category;
import com.ericsospedra.listshoppingfirebase.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductoDAO {
    private static final FirebaseFirestore DB = FirebaseFirestore.getInstance();

    public ProductoDAO() {
    }

    public Product findById(int id) {
        final Product[] p = new Product[1];
        DB.collection("products").whereEqualTo("id", String.valueOf(id)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        p[0] = document.toObject(Product.class);
                    }
                } else {
                    Log.e(ProductoDAO.class.getSimpleName(), "Error al recuperar el Producto con id:" + id);
                }
            }
        });
        return p[0];
    }

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        DB.collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documet : task.getResult()) {
                        products.add(documet.toObject(Product.class));
                    }
                } else {
                    Log.e(ProductoDAO.class.getSimpleName(), "Error al recuperar los Productos");
                }
            }
        });
        return products;
    }

    public List<Product> findBy(Map<String, String> conditions) {
        List<Product> products = new ArrayList<>();
        for (Map.Entry<String, String> entry : conditions.entrySet()) {
            DB.collection("products").whereEqualTo(entry.getKey(), entry.getValue()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            products.add(document.toObject(Product.class));
                        }
                    } else {
                        Log.e(ProductoDAO.class.getSimpleName(), "Error al recuperar el Producto con " + entry.getKey() + "y valor: " + entry.getValue());
                    }
                }
            });
        }
        return products;
    }
}
