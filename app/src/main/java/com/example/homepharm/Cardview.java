package com.example.homepharm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class Cardview extends AppCompatActivity {

    private static final String TAG = "Cardview";
    private TextView priceTextView;
    private LinearLayout cartContainer;
    private String username;
    private List<CartItem> cartItems = new ArrayList<>();
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardview);

        cartContainer = findViewById(R.id.cart_container);
        priceTextView = findViewById(R.id.price);


        username = getSharedPreferences("HomePharmPrefs", MODE_PRIVATE).getString("username", ""); // Default to empty if not found

        if (username.isEmpty()) {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            return;
        }


        cartManager = CartManager.getInstance(username);

        loadCartItems();

        Button orderButton = findViewById(R.id.orderbtn);
        orderButton.setOnClickListener(v -> {
            CartItem firstItem = getFirstCartItem();
            if (firstItem != null) {
                Intent intent = new Intent(Cardview.this, Order.class);
                intent.putExtra("ORDER_NAME", firstItem.getName());
                intent.putExtra("ORDER_IMAGE", firstItem.getImageResId());
                intent.putExtra("PRODUCT_PRICE", firstItem.getBasePrice());
                intent.putExtra("QUANTITY", firstItem.getQuantity());
                startActivity(intent);
            } else {
                Toast.makeText(Cardview.this, "No items in cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCartItems() {
        cartManager.getCartItems().addOnCompleteListener(new OnCompleteListener<List<CartItem>>() {
            @Override
            public void onComplete(@NonNull Task<List<CartItem>> task) {
                if (task.isSuccessful()) {
                    cartItems = task.getResult();
                    Log.d(TAG, "Loaded " + cartItems.size() + " cart items");
                    cartContainer.removeAllViews(); // Clear existing views
                    for (CartItem item : cartItems) {
                        addCartItemView(cartContainer, item);
                    }
                    updateTotalPrice();
                } else {
                    Log.e(TAG, "Failed to load cart items", task.getException());
                    Toast.makeText(Cardview.this, "Failed to load cart items", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addCartItemView(LinearLayout container, CartItem item) {
        View cartItemView = LayoutInflater.from(this).inflate(R.layout.activity_cart_item, container, false);

        ImageView itemImage = cartItemView.findViewById(R.id.item_image);
        TextView itemName = cartItemView.findViewById(R.id.item_name);
        TextView itemPrice = cartItemView.findViewById(R.id.item_price);
        TextView itemDescription = cartItemView.findViewById(R.id.item_description);
        TextView itemQty = cartItemView.findViewById(R.id.tv_qty);
        Button btnDecreaseQty = cartItemView.findViewById(R.id.btn_decrease_qty);
        Button btnIncreaseQty = cartItemView.findViewById(R.id.btn_increase_qty);
        Button btnRemove = cartItemView.findViewById(R.id.btn_remove);

        itemImage.setImageResource(item.getImageResId());
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());

        int initialQty = item.getQuantity();
        itemQty.setText(String.valueOf(initialQty));
        itemPrice.setText(String.format("₹%.2f", item.getTotalPrice()));

        btnDecreaseQty.setOnClickListener(v -> {
            int currentQty = item.getQuantity();
            if (currentQty > 1) {
                currentQty--;
                item.setQuantity(currentQty);
                itemQty.setText(String.valueOf(currentQty));
                itemPrice.setText(String.format("₹%.2f", item.getTotalPrice()));
                updateTotalPrice();
                cartManager.updateCartItem(item);
            } else {
                Toast.makeText(Cardview.this, "Quantity cannot be less than 1", Toast.LENGTH_SHORT).show();
            }
        });

        btnIncreaseQty.setOnClickListener(v -> {
            int currentQty = item.getQuantity();
            currentQty++;
            item.setQuantity(currentQty);
            itemQty.setText(String.valueOf(currentQty));
            itemPrice.setText(String.format("₹%.2f", item.getTotalPrice()));
            updateTotalPrice();
            cartManager.updateCartItem(item);
        });

        btnRemove.setOnClickListener(v -> {
            cartManager.removeItem(item);
            container.removeView(cartItemView);
            Toast.makeText(Cardview.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
            updateTotalPrice();
        });

        container.addView(cartItemView);
    }

    private void updateTotalPrice() {
        double totalPrice = 0.0;
        for (CartItem item : cartItems) {
            totalPrice += item.getTotalPrice();
        }
        priceTextView.setText(String.format("₹%.2f", totalPrice));
    }

    private CartItem getFirstCartItem() {
        if (!cartItems.isEmpty()) {
            return cartItems.get(0);
        }
        return null;
    }
}
