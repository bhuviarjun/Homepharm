package com.example.homepharm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Injection extends AppCompatActivity {
    CardView cardInsulin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_injection);
        cardInsulin = findViewById(R.id.Insulin);
        cardInsulin.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(Injection.this, InSub.class);
                startActivity(intent);
                return true;
            }
        });
    }
}
