package com.example.homepharm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static final String TAG = "CartManager";
    private static CartManager instance;
    private final String username;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CartManager(String username) {
        this.username = username;
    }

    public static CartManager getInstance(String username) {
        if (instance == null) {
            instance = new CartManager(username);
        }
        return instance;
    }

    public Task<List<CartItem>> getCartItems() {
        Log.d(TAG, "Fetching cart items for user: " + username);
        return db.collection("carts")
                .document(username)
                .collection("items")
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        List<CartItem> cartItems = new ArrayList<>();
                        QuerySnapshot result = task.getResult();
                        if (result != null) {
                            for (QueryDocumentSnapshot document : result) {
                                Log.d(TAG, "Document snapshot: " + document.getData());
                                String name = document.getString("name");
                                double basePrice = document.getDouble("basePrice");
                                String description = document.getString("description");
                                int imageResId = document.getLong("imageResId").intValue();
                                int quantity = document.getLong("quantity").intValue();
                                CartItem item = new CartItem(name, basePrice, description, imageResId, quantity);
                                cartItems.add(item);
                            }
                        }
                        Log.d(TAG, "Fetched " + cartItems.size() + " items from Firestore");
                        return cartItems;
                    } else {
                        Log.e(TAG, "Error getting documents.", task.getException());
                        throw task.getException();
                    }
                });
    }

    public void addItem(CartItem item) {
        Log.d(TAG, "Adding item to cart: " + item.getName());
        db.collection("carts")
                .document(username)
                .collection("items")
                .document(item.getName()) // Assuming item name is unique for simplicity
                .set(item.toMap())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Item successfully added"))
                .addOnFailureListener(e -> Log.e(TAG, "Error adding item", e));
    }

    public void updateCartItem(CartItem item) {
        Log.d(TAG, "Updating item in cart: " + item.getName());
        db.collection("carts")
                .document(username)
                .collection("items")
                .document(item.getName())
                .set(item.toMap())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Item successfully updated"))
                .addOnFailureListener(e -> Log.e(TAG, "Error updating item", e));
    }

    public void removeItem(CartItem item) {
        Log.d(TAG, "Removing item from cart: " + item.getName());
        db.collection("carts")
                .document(username)
                .collection("items")
                .document(item.getName())
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Item successfully removed"))
                .addOnFailureListener(e -> Log.e(TAG, "Error removing item", e));
    }
}
