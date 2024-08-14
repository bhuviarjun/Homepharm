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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class OrderTrackingActivity extends AppCompatActivity {

    private static final String TAG = "OrderTrackingActivity";
    private TextView totalPriceTextView;
    private LinearLayout orderContainer;
    private String username;
    private List<OrderItem> orderItems = new ArrayList<>();
    private OrderManager orderManager;
    private Button home, map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        orderContainer = findViewById(R.id.order_container);
        totalPriceTextView = findViewById(R.id.total_price);
        home = findViewById(R.id.home);
        map = findViewById(R.id.map);

        home.setOnClickListener(v -> {
            Intent intent = new Intent(OrderTrackingActivity.this, Category.class);
            startActivity(intent);
        });

        map.setOnClickListener(v -> {
            Intent intent = new Intent(OrderTrackingActivity.this, Mapping.class);
            startActivity(intent);
        });


        username = getSharedPreferences("HomePharmPrefs", MODE_PRIVATE).getString("username", ""); // Default to empty if not found

        if (username.isEmpty()) {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        orderManager = OrderManager.getInstance(username);

        loadOrderItems();
    }

    private void loadOrderItems() {
        orderManager.getOrders().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                orderItems = task.getResult();
                Log.d(TAG, "Loaded " + orderItems.size() + " order items");
                orderContainer.removeAllViews(); // Clear existing views
                for (OrderItem item : orderItems) {
                    Log.d(TAG, "Adding order item: " + item.getName());
                    addOrderItemView(orderContainer, item);
                }
                updateTotalPrice();
            } else {
                Log.e(TAG, "Failed to load order items", task.getException());
                Toast.makeText(OrderTrackingActivity.this, "Failed to load order items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addOrderItemView(LinearLayout container, OrderItem item) {
        View orderItemView = LayoutInflater.from(this).inflate(R.layout.activity_order_item, container, false);

        ImageView itemImage = orderItemView.findViewById(R.id.item_image);
        TextView itemName = orderItemView.findViewById(R.id.item_name);
        TextView itemPrice = orderItemView.findViewById(R.id.item_price);
        TextView itemQuantity = orderItemView.findViewById(R.id.item_quantity);

        Glide.with(this)
                .load(item.getImageResId())
                .placeholder(R.drawable.bk1)
                .error(R.drawable.bk3)
                .into(itemImage);

        itemName.setText(item.getName());
        itemPrice.setText(String.format("₹%.2f", item.getTotalPrice()));
        itemQuantity.setText(String.format("Quantity: %d", item.getQuantity()));

        container.addView(orderItemView);
    }

    private void updateTotalPrice() {
        double totalPrice = 0.0;
        for (OrderItem item : orderItems) {
            totalPrice += item.getTotalPrice();
        }
        totalPriceTextView.setText(String.format("₹%.2f", totalPrice));
    }
}
