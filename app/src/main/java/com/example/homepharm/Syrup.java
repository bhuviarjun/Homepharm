package com.example.homepharm;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.View;
import androidx.core.content.ContextCompat;

public class Syrup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syrup);

        CardView benadrylCardView = findViewById(R.id.Benadryl);
        benadrylCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSubActivity("benadryl");
            }
        });

        CardView scofsilsCardView = findViewById(R.id.Scofsils);
        scofsilsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSubActivity("scofsils");
            }
        });

        CardView greenmineCardView = findViewById(R.id.Greenmine);
        greenmineCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSubActivity("greenmine");
            }
        });
        CardView paracipCardView = findViewById(R.id.Paracip);
        paracipCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSubActivity("paracip");
            }
        });
        CardView ulzymeCardView = findViewById(R.id.Ulzyme);
        ulzymeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSubActivity("Ulzyme");
            }
        });
        CardView vevitCardView = findViewById(R.id.Vevit);
        vevitCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSubActivity("vevit");
            }
        });
    }

    private void startSubActivity(String selectedSyrup) {
        Intent intent = new Intent(Syrup.this, Sy_sub.class);
        intent.putExtra("selected_syrup", selectedSyrup);
        startActivity(intent);
    }
}
