package com.ericsospedra.listshoppingfirebase;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.ericsospedra.listshoppingfirebase.fragments.AddListBoxDialogFragment;
import com.ericsospedra.listshoppingfirebase.interfaces.IOnClickListener;
import com.ericsospedra.listshoppingfirebase.models.ShoppingList;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements AddListBoxDialogFragment.OnListAddedListener {
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            Toast.makeText(this, "Bienvenido " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
            db = FirebaseFirestore.getInstance();
            FloatingActionButton btAdd = findViewById(R.id.btAdd);
            btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddListDialog();
                }
            });
        }else {
            Toast.makeText(this, "Usuario desconocido", Toast.LENGTH_SHORT).show();
            ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode() == RESULT_OK){
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        Toast.makeText(MainActivity.this, "Bienvenido "+firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, "Acceso Denegado", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            resultLauncher.launch(AuthUI.getInstance().createSignInIntentBuilder().build());
        }
    }
    private void showAddListDialog() {
        AddListBoxDialogFragment dialog = new AddListBoxDialogFragment();
        dialog.setOnListAddedListener(this);
        dialog.show(getSupportFragmentManager(), "AddListDialogFragment");
    }

    @Override
    public void onListAdded(String listName) {
        ShoppingList list = new ShoppingList(listName,"shopping_list", new Date(),0);
        db.collection("ShoppingLists").add(list).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(MainActivity.class.getSimpleName(), "Producto añadido correctamente con id:" + list.getName());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(MainActivity.class.getSimpleName(), "Error al añadir la lista");
                Log.e(MainActivity.class.getSimpleName(), e.getMessage());
            }
        });
    }
}