package com.example.homepharm;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderManager {
    private static OrderManager instance;
    private String username;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private OrderManager(String username) {
        this.username = username;
    }

    public static synchronized OrderManager getInstance(String username) {
        if (instance == null) {
            instance = new OrderManager(username);
        }
        return instance;
    }

    public Task<List<OrderItem>> getOrders() {
        CollectionReference ordersRef = db.collection("users").document(username).collection("orders");

        return ordersRef.get().continueWith(task -> {
            List<OrderItem> orderItems = new ArrayList<>();
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                for (DocumentSnapshot document : snapshot.getDocuments()) {
                    Map<String, Object> data = document.getData();
                    String name = (String) data.get("name");
                    double basePrice = (double) data.get("basePrice");
                    String description = (String) data.get("description");
                    int imageResId = (data.get("imageResId") != null) ? ((Long) data.get("imageResId")).intValue() : R.drawable.bk1;
                    int quantity = ((Long) data.get("quantity")).intValue();

                    OrderItem item = new OrderItem(name, basePrice, description, imageResId, quantity);
                    orderItems.add(item);
                }
            } else {
                throw task.getException();
            }
            return orderItems;
        });
    }
}
