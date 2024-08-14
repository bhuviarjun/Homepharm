package com.example.homepharm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Tablets extends AppCompatActivity {

    CardView cardParacetamol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tablets);
        cardParacetamol = findViewById(R.id.Para);
        cardParacetamol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tablets.this,Tab_sub.class);
                startActivity(intent);
            }
        });
        CardView doloCardView = findViewById(R.id.Dolo);
        doloCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tablets.this,Tab_sub.class);
                intent.putExtra("selected_card", "dolo");
                startActivity(intent);
            }
        });

        CardView antacidCardView = findViewById(R.id.Antacid);
        antacidCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tablets.this, Tab_sub.class);
                intent.putExtra("selected_card", "antacid"); // Pass the selected card identifier
                startActivity(intent);
            }
        });

        CardView antihistamineCardView = findViewById(R.id.Antihistamine);
        antihistamineCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tablets.this, Tab_sub.class);
                intent.putExtra("selected_card", "antihistamine"); // Pass the selected card identifier
                startActivity(intent);
            }
        });


    }
}