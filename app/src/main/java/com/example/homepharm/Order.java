package com.example.homepharm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Order extends AppCompatActivity {
    private static final String CHANNEL_ID = "My Channel";
    private static final int NOTIFICATION_ID = 1;
    private static final int REQUEST_CODE_POST_NOTIFICATION = 1;

    private EditText customerNameEditText, mobileEditText, addressEditText, cityEditText, stateEditText;
    private TextView orderNameTextView, productPriceTextView, quantityTextView;
    private ImageView orderImageView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        customerNameEditText = findViewById(R.id.editTextName);
        mobileEditText = findViewById(R.id.editTextMobile);
        addressEditText = findViewById(R.id.editTextAddress);
        cityEditText = findViewById(R.id.editTextCity);
        stateEditText = findViewById(R.id.editTextState);
        orderNameTextView = findViewById(R.id.order_name);
        productPriceTextView = findViewById(R.id.product_price);
        quantityTextView = findViewById(R.id.quantity);
        orderImageView = findViewById(R.id.order_image);


        Intent intent = getIntent();
        if (intent != null) {
            String orderName = intent.getStringExtra("ORDER_NAME");
            double productPrice = intent.getDoubleExtra("PRODUCT_PRICE", 0.0);
            int quantity = intent.getIntExtra("QUANTITY", 0);
            int orderImageResId = intent.getIntExtra("ORDER_IMAGE", R.drawable.bk1);


            orderNameTextView.setText(orderName);
            productPriceTextView.setText(String.format("₹%.2f", productPrice));
            quantityTextView.setText(String.format("Quantity: %d", quantity));
            orderImageView.setImageResource(orderImageResId);
        }


        Button placeOrderButton = findViewById(R.id.placeOrderButton);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customerName = customerNameEditText.getText().toString().trim();
                String mobile = mobileEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                String city = cityEditText.getText().toString().trim();
                String state = stateEditText.getText().toString().trim();

                if (!customerName.isEmpty() && !mobile.isEmpty() && !address.isEmpty() && !city.isEmpty() && !state.isEmpty()) {

                    Intent intent = getIntent();
                    String orderName = intent.getStringExtra("ORDER_NAME");
                    double productPrice = intent.getDoubleExtra("PRODUCT_PRICE", 0.0);
                    int quantity = intent.getIntExtra("QUANTITY", 0);


                    OrderItem orderItem = new OrderItem(orderName, productPrice, "", R.drawable.bk1, quantity); // Adjust imageResId as needed


                    saveOrderToFirestore(customerName, mobile, address, city, state, orderItem);
                } else {

                    Toast.makeText(Order.this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveOrderToFirestore(String customerName, String mobile, String address, String city, String state, OrderItem orderItem) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String username = "username";


        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("customerName", customerName);
        orderMap.put("mobile", mobile);
        orderMap.put("address", address);
        orderMap.put("city", city);
        orderMap.put("state", state);
        orderMap.put("orderName", orderItem.getName());
        orderMap.put("productPrice", orderItem.getBasePrice());
        orderMap.put("quantity", orderItem.getQuantity());
        orderMap.put("totalPrice", orderItem.getTotalPrice());


        db.collection("orders")
                .document(username)
                .collection("items")
                .document(orderItem.getName()) // Using item name as document ID for simplicity
                .set(orderMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Order.this, "Order placed successfully", Toast.LENGTH_SHORT).show();

                    Intent trackingIntent = new Intent(Order.this, OrderTrackingActivity.class);
                    trackingIntent.putExtra("CUSTOMER_NAME", customerName);
                    startActivity(trackingIntent);


                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                        createNotification();
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATION);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Order.this, "Error placing order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void createNotification() {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, null);
        Bitmap largeIcon = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            largeIcon = bitmapDrawable.getBitmap();
        } else if (drawable instanceof VectorDrawable) {
            VectorDrawable vectorDrawable = (VectorDrawable) drawable;
            largeIcon = convertVectorDrawableToBitmap(vectorDrawable);
        }

        if (largeIcon == null) {
            Toast.makeText(this, "Unable to convert drawable to bitmap", Toast.LENGTH_SHORT).show();
            return;
        }

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText("Order Placed")
                    .setSubText("Your order has been placed successfully")
                    .setChannelId(CHANNEL_ID)
                    .build();
            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "Order Notifications", NotificationManager.IMPORTANCE_HIGH));
        } else {
            notification = new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText("Order Placed")
                    .setSubText("Your order has been placed successfully")
                    .build();
        }

        Log.d("Order", "Creating notification");
        nm.notify(NOTIFICATION_ID, notification);
    }

    private Bitmap convertVectorDrawableToBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_POST_NOTIFICATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createNotification();
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
