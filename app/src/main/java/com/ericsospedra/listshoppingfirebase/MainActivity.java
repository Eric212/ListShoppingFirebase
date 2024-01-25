package com.ericsospedra.listshoppingfirebase;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ericsospedra.listshoppingfirebase.fragments.AddCategoryBoxDialogFragment;
import com.ericsospedra.listshoppingfirebase.fragments.AddListBoxDialogFragment;
import com.ericsospedra.listshoppingfirebase.fragments.AddProductBoxDialogFragment;
import com.ericsospedra.listshoppingfirebase.fragments.CategoryFragment;
import com.ericsospedra.listshoppingfirebase.fragments.LinesOfShoppingListFragment;
import com.ericsospedra.listshoppingfirebase.fragments.ShoppingListFragment;
import com.ericsospedra.listshoppingfirebase.interfaces.IOnClickListener;
import com.ericsospedra.listshoppingfirebase.models.Category;
import com.ericsospedra.listshoppingfirebase.models.Product;
import com.ericsospedra.listshoppingfirebase.models.ShoppingList;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;


public class MainActivity extends AppCompatActivity implements AddListBoxDialogFragment.OnListAddedListener, AddCategoryBoxDialogFragment.OnCategoryAddedListener, IOnClickListener, LinesOfShoppingListFragment.IOnAttach, AddProductBoxDialogFragment.OnProductAddedListener {
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    private FragmentManager manager;
    private String pipe;

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
        } else {
            Toast.makeText(this, "Usuario desconocido", Toast.LENGTH_SHORT).show();
            ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == RESULT_OK) {
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        Toast.makeText(MainActivity.this, "Bienvenido " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Acceso Denegado", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            resultLauncher.launch(AuthUI.getInstance().createSignInIntentBuilder().build());
        }
    }

    private void showAddListDialog() {
        Fragment f = manager.findFragmentById(R.id.fcvMain);
        if (f instanceof ShoppingListFragment) {
            AddListBoxDialogFragment dialog = new AddListBoxDialogFragment();
            dialog.setOnListAddedListener(this);
            dialog.show(getSupportFragmentManager(), "AddListDialogFragment");
        } else if (f instanceof LinesOfShoppingListFragment) {
            manager.beginTransaction().setReorderingAllowed(true).addToBackStack(null).replace(R.id.fcvMain, CategoryFragment.class,null).commit();
        } else if (f instanceof CategoryFragment) {
            AddCategoryBoxDialogFragment dialog = new AddCategoryBoxDialogFragment();
            dialog.setOnListAddedListener(this);
            dialog.show(getSupportFragmentManager(), "AddListDialogFragment");
        } else{
            AddProductBoxDialogFragment dialog = new AddProductBoxDialogFragment();
            dialog.setOnListAddedListener(this);
            dialog.show(getSupportFragmentManager(), "AddListDialogFragment");
        }
    }


    //TODO: Implements user filter for duplicate lists check
    @Override
    public void onListAdded(String listName) {
        ShoppingList list = new ShoppingList(listName, "shopping_list", new Date().getTime(), 0, null);
        db.collection("ShoppingLists").whereEqualTo("name", list.getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() == 0) {
                        db.collection("ShoppingLists").add(list).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(MainActivity.class.getSimpleName(), "Lista añadida correctamente:" + list.getName());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(MainActivity.class.getSimpleName(), "Error al añadir la lista");
                                Log.e(MainActivity.class.getSimpleName(), e.getMessage());
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Error al crear la lista, ya existe una lista con este nombre", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onCategoryAdd(String item) {
        Category c = new Category(item, item, null);
        db.collection("Categories").whereEqualTo("name", c.getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() == 0) {
                        db.collection("ShoppingLists").add(c).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(MainActivity.class.getSimpleName(), "Lista añadida correctamente:" + c.getName());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(MainActivity.class.getSimpleName(), "Error al añadir la lista");
                                Log.e(MainActivity.class.getSimpleName(), e.getMessage());
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Error al crear la categoria, ya existe una categoria con este nombre", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    public void onProductAdd(String item) {
        Product p = new Product(item,item);
        db.collection("Categories").whereEqualTo("name", p.getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() == 0) {
                        db.collection("ShoppingLists").add(c).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(MainActivity.class.getSimpleName(), "Lista añadida correctamente:" + c.getName());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(MainActivity.class.getSimpleName(), "Error al añadir la lista");
                                Log.e(MainActivity.class.getSimpleName(), e.getMessage());
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Error al crear la categoria, ya existe una categoria con este nombre", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    public void onClick(String s) {
        manager = getSupportFragmentManager();
        pipe = null;
        db.collection("ShoppingLists").document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getData().isEmpty()) {
                        pipe = s;
                        int f = findViewById(R.id.fcvMain).getId();
                        manager.beginTransaction().setReorderingAllowed(true).addToBackStack(null).replace(f, LinesOfShoppingListFragment.class, null).commit();
                    }
                }
            }
        });
        /*db.collection("Categories").document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getData().isEmpty()) {
                        pipe = s;
                    }
                }
            }
        });*/
        /*db.collection("Productos").document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getData().isEmpty()) {
                        pipe = s;
                    }
                }
            }
        });*/
    }

    @Override
    public String getListId() {
        return pipe;
    }
}