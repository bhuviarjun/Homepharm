package com.example.homepharm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText rName, rEmail, rUsername, rPassword;
    TextView textr;
    Button bRegister;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rName = findViewById(R.id.editTextName);
        rEmail = findViewById(R.id.editTextEmail);
        rUsername = findViewById(R.id.editTextUsernamee);
        rPassword = findViewById(R.id.editTextPassword);
        bRegister = findViewById(R.id.btnRegister);
        textr = findViewById(R.id.textViewNeww);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("users");
                    String name = rName.getText().toString();
                    String email = rEmail.getText().toString();
                    String username = rUsername.getText().toString();
                    String password = rPassword.getText().toString();
                    HelperClass helperClass = new HelperClass(name, email, username, password);
                    reference.child(username).setValue(helperClass);

                    Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        textr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateFields() {
        String name = rName.getText().toString().trim();
        String email = rEmail.getText().toString().trim();
        String username = rUsername.getText().toString().trim();
        String password = rPassword.getText().toString().trim();

        if (name.isEmpty()) {
            rName.setError("Name cannot be empty");
            return false;
        } else {
            rName.setError(null);
        }

        if (email.isEmpty()) {
            rEmail.setError("Email cannot be empty");
            return false;
        } else {
            rEmail.setError(null);
        }

        if (username.isEmpty()) {
            rUsername.setError("Username cannot be empty");
            return false;
        } else {
            rUsername.setError(null);
        }

        if (password.isEmpty()) {
            rPassword.setError("Password cannot be empty");
            return false;
        } else if (password.length() < 8) {
            rPassword.setError("Password must be at least 8 characters");
            return false;
        } else {
            rPassword.setError(null);
        }

        return true;
    }
}
