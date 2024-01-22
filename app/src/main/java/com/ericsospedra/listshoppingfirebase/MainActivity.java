package com.ericsospedra.listshoppingfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ericsospedra.listshoppingfirebase.db.ProductoDAO;
import com.ericsospedra.listshoppingfirebase.models.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        for(int i = 0; i < 10; i++) {
//            Product p = new Product(i,"Producto"+i,"imagen"+i,i);
//            db.collection("products").add(p).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                @Override
//                public void onSuccess(DocumentReference documentReference) {
//                    Log.d(MainActivity.class.getSimpleName(), "Producto añadido correctamente con id:" + documentReference.getId());
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d(MainActivity.class.getSimpleName(), "Error al añadir el producto");
//                    Log.e(MainActivity.class.getSimpleName(), e.getMessage());
//                }
//            });
//        }
        ProductoDAO productoDAO = new ProductoDAO();
        productoDAO.findAll();
        List<Product> products = productoDAO.findAll();
        for(Product p : products){
            Log.d(MainActivity.class.getSimpleName(), p.toString());
        }
    }

}