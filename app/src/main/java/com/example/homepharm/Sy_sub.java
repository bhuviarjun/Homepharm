package com.example.homepharm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class Sy_sub extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sy_sub);

        Intent intent = getIntent();
        String selectedSyrup = intent.getStringExtra("selected_syrup");
        String username = "username";

        highlightSelectedSyrup(selectedSyrup);

        setupAddToCartButtons(username);
    }

    private void highlightSelectedSyrup(String selectedSyrup) {
        if ("benadryl".equals(selectedSyrup)) {
            highlightCardViewById(R.id.Benadryl);
        } else if ("scofsils".equals(selectedSyrup)) {
            highlightCardViewById(R.id.Scofsils);
        } else if ("greenmine".equals(selectedSyrup)) {
            highlightCardViewById(R.id.GreenMine);
        } else if ("paracip".equals(selectedSyrup)) {
            highlightCardViewById(R.id.Paracip);
        } else if ("ulzyme".equals(selectedSyrup)) {
            highlightCardViewById(R.id.Ulzyme);
        }
    }

    private void highlightCardViewById(int cardViewId) {
        CardView cardView = findViewById(cardViewId);
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
        cardView.requestFocus();
    }

    private void setupAddToCartButtons(final String username) {
        Button addToCartBenadryl = findViewById(R.id.addToCartBenadryl);
        addToCartBenadryl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart("Benadryl", 120.0, "Benadryl is an antihistamine used to relieve symptoms of allergy, hay fever, and the common cold.", R.drawable.benadryl, username);
            }
        });

        Button addToCartScofsils = findViewById(R.id.addToCartScofsils);
        addToCartScofsils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart("Scofsils", 120.0, "Scofsils is a syrup used to relieve cough and cold symptoms.", R.drawable.cof, username);
            }
        });

        Button addToCartGreenMine = findViewById(R.id.addToCartGreenMine);
        addToCartGreenMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart("GreenMine", 150.0, "GreenMine is a natural supplement designed to boost your immune system.", R.drawable.greenmine, username);
            }
        });

        Button addToCartParacip = findViewById(R.id.addToCartParacip);
        addToCartParacip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart("Paracip", 50.0, "Paracip is used for pain relief and reducing fever.", R.drawable.paracip, username);
            }
        });

        Button addToCartUlzyme = findViewById(R.id.addToCartUlzyme);
        addToCartUlzyme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart("Ulzyme", 90.0, "Ulzyme helps in digestion and provides relief from bloating and gas.", R.drawable.ulzyme, username);
            }
        });
    }

    private void addItemToCart(String itemName, double price, String description, int imageResource, String username) {
        CartItem item = new CartItem(itemName, price, description, imageResource, 1);
        CartManager.getInstance(username).addItem(item); // Use addItem instead of addItemToCart
        Toast.makeText(this, "Item added to cart: " + itemName, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Sy_sub.this, Cardview.class);
        startActivity(intent);
    }
}
