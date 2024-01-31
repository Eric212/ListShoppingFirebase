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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ericsospedra.listshoppingfirebase.fragments.AddCategoryBoxDialogFragment;
import com.ericsospedra.listshoppingfirebase.fragments.AddListBoxDialogFragment;
import com.ericsospedra.listshoppingfirebase.fragments.AddProductBoxDialogFragment;
import com.ericsospedra.listshoppingfirebase.fragments.CategoryFragment;
import com.ericsospedra.listshoppingfirebase.fragments.DeleteCategoryBoxDialogFragment;
import com.ericsospedra.listshoppingfirebase.fragments.DeleteShoppingListBoxDialogFragment;
import com.ericsospedra.listshoppingfirebase.fragments.DeletedProductBoxDialogFragment;
import com.ericsospedra.listshoppingfirebase.fragments.LinesOfShoppingListFragment;
import com.ericsospedra.listshoppingfirebase.fragments.ProductFragment;
import com.ericsospedra.listshoppingfirebase.fragments.ShoppingListFragment;
import com.ericsospedra.listshoppingfirebase.interfaces.IOnClickListener;
import com.ericsospedra.listshoppingfirebase.interfaces.IOnLongClickListener;
import com.ericsospedra.listshoppingfirebase.models.Category;
import com.ericsospedra.listshoppingfirebase.models.LinesOfShoppingList;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;


public class MainActivity extends AppCompatActivity implements AddListBoxDialogFragment.OnListAddedListener,
        AddCategoryBoxDialogFragment.OnCategoryAddedListener,
        IOnClickListener, LinesOfShoppingListFragment.IOnAttach,
        AddProductBoxDialogFragment.OnProductAddedListener,
        ProductFragment.IOnAttach,
        IOnLongClickListener,
        DeleteShoppingListBoxDialogFragment.OnDeleteListListener,
        DeleteCategoryBoxDialogFragment.OnDeleteCategoryListener,
        DeletedProductBoxDialogFragment.OnDeleteProductListener {
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    private FragmentManager manager;
    private String listId;
    private String categoryId;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        manager = getSupportFragmentManager();
        toolbar = findViewById(R.id.tbMenu);
        toolbar.setTitle("Shopping List");

        if (firebaseUser != null) {
            Toast.makeText(this, "Bienvenido " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
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
            dialog.show(manager, "AddListDialogFragment");
        } else if (f instanceof LinesOfShoppingListFragment) {
            manager.beginTransaction().setReorderingAllowed(true).addToBackStack(null).replace(R.id.fcvMain, CategoryFragment.class, null).commit();
        } else if (f instanceof CategoryFragment) {
            AddCategoryBoxDialogFragment dialog = new AddCategoryBoxDialogFragment();
            dialog.setOnCategoryAddedListener(this);
            dialog.show(manager, "AddListDialogFragment");
        } else {
            AddProductBoxDialogFragment dialog = new AddProductBoxDialogFragment();
            dialog.setOnProductAddedListener(this);
            dialog.show(getSupportFragmentManager(), "AddListDialogFragment");
        }
    }

    private void showDeleteListDialog(String s) {
        if (manager.findFragmentById(R.id.fcvMain) instanceof ShoppingListFragment) {
            DeleteShoppingListBoxDialogFragment dialog = new DeleteShoppingListBoxDialogFragment(s);
            dialog.setOnDeleteList(this);
            dialog.show(manager, "AddListDialogFragment");
        } else if (manager.findFragmentById(R.id.fcvMain) instanceof CategoryFragment) {
            DeleteCategoryBoxDialogFragment dialog = new DeleteCategoryBoxDialogFragment(s);
            dialog.setOnDeleteCategory(this);
            dialog.show(manager, "AddListDialogFragment");
        } else {
            DeletedProductBoxDialogFragment dialog = new DeletedProductBoxDialogFragment(s);
            dialog.setOnDeleteProduct(this);
            dialog.show(manager, "AddListDialogFragment");
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
                    if (task.getResult().size()==0) {
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
        Category c = new Category(item, item.toLowerCase());
        db.collection("Categories").whereEqualTo("name", c.getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() == 0) {
                        db.collection("Categories").add(c).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(MainActivity.class.getSimpleName(), "Categoria añadida correctamente: " + c.getName());
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(MainActivity.class.getSimpleName(), "Error al añadir la categoria");
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
        Product p = new Product(item, null);
        db.collection("Categories").document(categoryId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Category c = task.getResult().toObject(Category.class);
                    p.setImage(c.getImage());
                    db.collection("Categories").document(categoryId).collection("Products").whereEqualTo("name",p.getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() == 0) {
                                    db.collection("Categories").document(categoryId).collection("Products").add(p).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(MainActivity.class.getSimpleName(), "Producto añadida correctamente: " + p.getName());
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity.this, "Error al añadir el producto", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(String s) {
        if (manager.findFragmentById(R.id.fcvMain) instanceof ShoppingListFragment) {
            db.collection("ShoppingLists").document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().getData() != null) {
                            listId = s;
                            int f = findViewById(R.id.fcvMain).getId();
                            manager.beginTransaction().setReorderingAllowed(true).addToBackStack(null).replace(f, LinesOfShoppingListFragment.class, null).commit();
                            toolbar.setTitle(task.getResult().toObject(ShoppingList.class).getName());
                        }
                    }
                }
            });
        } else if (manager.findFragmentById(R.id.fcvMain) instanceof CategoryFragment) {
            db.collection("Categories").document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().getData() != null) {
                            categoryId = s;
                            manager.beginTransaction().setReorderingAllowed(true).addToBackStack(null).replace(R.id.fcvMain, ProductFragment.class, null).commit();
                            toolbar.setTitle(task.getResult().toObject(Category.class).getName());
                        }
                    }
                }
            });
        } else if (manager.findFragmentById(R.id.fcvMain) instanceof ProductFragment) {
            db.collection("Categories").document(categoryId).collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Product p = document.toObject(Product.class);
                        if (p.getName().equals(s)) {
                            LinesOfShoppingList line = new LinesOfShoppingList(document.getId(), p.getName(), p.getImage(), false);
                            db.collection("ShoppingLists").document(listId).collection("LinesOfShoppingList").add(line).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(MainActivity.class.getSimpleName(), "Producto añadido a la lista");
                                    updateCantidadProductos();
                                    manager.beginTransaction().setReorderingAllowed(true).addToBackStack(null).replace(R.id.fcvMain, LinesOfShoppingListFragment.class, null).commit();
                                    db.collection("ShoppingLists").document(listId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                toolbar.setTitle(task.getResult().toObject(ShoppingList.class).getName());
                                            }
                                        }
                                    });
                                    db.collection("ShoppingLists").document(listId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            toolbar.setTitle(task.getResult().toObject(ShoppingList.class).getName());
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Error al añadir el producto a la lista", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            });
        } else {
            String[] values = s.split(" ");
            boolean checked = Boolean.parseBoolean(values[0]);
            db.collection("ShoppingLists").document(listId).collection("LinesOfShoppingList").document(values[1]).update("buy",checked).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(MainActivity.class.getSimpleName(), "Linea de compra actualizada");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(MainActivity.class.getSimpleName(), "Error al actualizar la linea de compra");
                }
            });
        }
    }

    @Override
    public void OnShoppingListDeleted(String item) {
        db.collection("ShoppingLists").whereEqualTo("name", item).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        db.collection("ShoppingLists").document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(MainActivity.class.getSimpleName(), "Lista eliminado con exito");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error al eliminar la lista", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void OnCategoryDeleted(String item) {
        db.collection("Categories").whereEqualTo("name", item).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        db.collection("Categories").document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(MainActivity.class.getSimpleName(), "Categoria eliminado con exito");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error al eliminar la categoria", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void OnProductyDeleted(String item) {
        db.collection("Categories").document(categoryId).collection("Products").whereEqualTo("name", item).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        db.collection("Categories").document(categoryId).collection("Products").document(document.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }
            }
        });

    }

    private void updateCantidadProductos() {
        db.collection("ShoppingLists").document(listId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    ShoppingList list = task.getResult().toObject(ShoppingList.class);
                    db.collection("ShoppingLists").document(listId).collection("LinesOfShoppingList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                list.setCantidadProductos(task.getResult().size());
                                db.collection("ShoppingLists").document(listId).update("cantidadProductos", task.getResult().size()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(MainActivity.class.getSimpleName(), "Cantidad de produuctos actualizada");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(MainActivity.class.getSimpleName(), "Error al actualizar la cantidad de productos");
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public String getListId() {
        return listId;
    }

    @Override
    public String getCategoryId() {
        return categoryId;
    }

    @Override
    public void onLongClick(String s) {
        if (manager.findFragmentById(R.id.fcvMain) instanceof LinesOfShoppingListFragment) {
            db.collection("ShoppingLists").document(listId).collection("LinesOfShoppingList").document(s).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    updateCantidadProductos();
                    Log.d(MainActivity.class.getSimpleName(), "Producto eliminado con exito de la lista de la compra");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Error al eliminar el producto de la lista", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            showDeleteListDialog(s);
        }
    }
}
