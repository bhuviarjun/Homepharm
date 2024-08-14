package com.example.homepharm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class Tab_sub extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_sub);

        Intent intent = getIntent();
        String selectedCard = intent.getStringExtra("selected_card");
        String username = "username";

        highlightSelectedCard(selectedCard);

        setupAddToCartButtons(username);
    }

    private void highlightSelectedCard(String selectedCard) {
        if ("dolo".equals(selectedCard)) {
            highlightCardViewById(R.id.Dolo650);
        } else if ("antacid".equals(selectedCard)) {
            highlightCardViewById(R.id.Antacid);
        } else if ("antihistamine".equals(selectedCard)) {
            highlightCardViewById(R.id.Antihistamine);
        }
    }

    private void highlightCardViewById(int cardViewId) {
        CardView cardView = findViewById(cardViewId);
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
        cardView.requestFocus();
    }

    private void setupAddToCartButtons(final String username) {
        Button addToCartPara = findViewById(R.id.addToCartPara);
        addToCartPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart("Paracetamol", 50.0, "Paracetamol, also known as acetaminophen, is a widely used over-the-counter pain reliever and fever reducer.", R.drawable.para, username);
            }
        });

        Button addToCartDolo650 = findViewById(R.id.addToCartDolo650);
        addToCartDolo650.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart("Dolo 650", 20.0, "Dolo 650 is a popular over-the-counter medication used to relieve pain and reduce fever. It contains 650 mg of paracetamol.", R.drawable.dolo, username);
            }
        });

        Button addToCartAntacid = findViewById(R.id.addToCartAntacid);
        addToCartAntacid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart("Antacid", 30.0, "Antacid tablets are used to relieve heartburn, acid indigestion, and upset stomach.", R.drawable.antacid, username);
            }
        });

        Button addToCartAntihistamine = findViewById(R.id.addToCartAntihistamine);
        addToCartAntihistamine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart("Antihistamine", 25.0, "Antihistamine tablets are used to relieve allergy symptoms such as itching, sneezing, and runny nose.", R.drawable.antihistamine, username);
            }
        });
    }

    private void addItemToCart(String itemName, double price, String description, int imageResource, String username) {
        CartItem item = new CartItem(itemName, price, description, imageResource, 1);
        CartManager.getInstance(username).addItem(item); // Use addItem instead of addItemToCart
        Toast.makeText(this, "Item added to cart: " + itemName, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Tab_sub.this, Cardview.class);
        startActivity(intent);
    }
}
