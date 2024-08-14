package com.example.homepharm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    EditText lUsername, lPassword;
    TextView textl;
    Button btnl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("HomePharmPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, Category.class);
            startActivity(intent);
            finish();
            return;
        }

        textl = findViewById(R.id.textViewNew);
        lUsername = findViewById(R.id.editTextUsername);
        lPassword = findViewById(R.id.editTextPassword);
        btnl = findViewById(R.id.btnLogin);
        btnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUsername() | !validatePassword()) {
                } else {
                    checkUser();
                }
            }
        });

        textl.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });
    }

    public Boolean validateUsername() {
        String val = lUsername.getText().toString();
        if (val.isEmpty()) {
            lUsername.setError("Username cannot be empty");
            return false;
        } else {
            lUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = lPassword.getText().toString();
        if (val.isEmpty()) {
            lPassword.setError("Password cannot be empty");
            return false;
        } else {
            lPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUsername = lUsername.getText().toString().trim();
        String userPassword = lPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    lUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (passwordFromDB == null) {
                        Toast.makeText(MainActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!Objects.equals(passwordFromDB, userPassword)) {
                        lPassword.setError("Invalid Credentials");
                        lPassword.requestFocus();
                    } else {
                        SharedPreferences sharedPreferences = getSharedPreferences("HomePharmPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("username", userUsername);
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this, Category.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    }

                } else {
                    lUsername.setError("User does not exist");
                    lUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    abstract class DoubleClickListener implements View.OnClickListener {
        private static final long DOUBLE_CLICK_TIME_DELTA = 300;
        long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                onDoubleClick(v);
            }
            lastClickTime = clickTime;
        }

        public abstract void onDoubleClick(View v);
    }
}
