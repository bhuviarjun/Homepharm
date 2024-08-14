package com.example.homepharm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InSub extends AppCompatActivity {
    TextView expiryInsulin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_sub);


        String username = "username";

        expiryInsulin = findViewById(R.id.expiry_insulin);

        expiryInsulin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        expiryInsulin.setTextColor(Color.BLUE);
                        break;
                    case MotionEvent.ACTION_UP:
                        expiryInsulin.setTextColor(Color.BLACK);
                        expiryInsulin.performClick();
                        break;
                }
                return true;
            }
        });

        expiryInsulin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InSub.this, "Insulin expiry clicked", Toast.LENGTH_SHORT).show();
            }
        });

        Button addToCartButton = findViewById(R.id.add_to_cart_button);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart("Insulin", 500.0, "Insulin is a vital hormone in the body that plays a key role in regulating blood sugar levels.", R.drawable.insulin, username);
            }
        });
    }

    private void addItemToCart(String itemName, double price, String description, int imageResource, String username) {
        CartItem item = new CartItem(itemName, price, description, imageResource, 1);
        CartManager.getInstance(username).addItem(item); // Ensure this matches your CartManager method
        Toast.makeText(this, "Item added to cart: " + itemName, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(InSub.this, Cardview.class);
        startActivity(intent);
    }
}
