package com.example.shoppingmanagementhod;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class ShopActivity extends AppCompatActivity {

    private EditText editTextProduct;
    private EditText editTextProductAmount;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // Initialize Firebase components
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get a reference to the "products" node in your database
        if (currentUser != null) {
            String encodedEmail = encodeEmail(currentUser.getEmail());
            Toast.makeText(this, encodedEmail, Toast.LENGTH_SHORT).show();
            productsRef = database.getReference("users").child(encodedEmail).child("products");
        }

        // Initialize views
        editTextProduct = findViewById(R.id.editTextProduct);
        editTextProductAmount = findViewById(R.id.editTextProductAmount);
        Button buttonAddProduct = findViewById(R.id.buttonAddProduct);
        Button buttonRemoveProduct = findViewById(R.id.buttonRemoveProduct);

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        buttonRemoveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the product name to remove
                String productName = editTextProduct.getText().toString().trim();
                if (!productName.isEmpty()) {
                    removeProduct(productName);
                } else {
                    Toast.makeText(ShopActivity.this, "Please enter a product name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (currentUser != null && currentUser.getEmail() != null) {
            String userEmail = currentUser.getEmail();
            // Set the email address to the TextView
            TextView textWelcome = findViewById(R.id.textWelcome);
            textWelcome.setText("Welcome, " + encodeEmail(userEmail));
        } else {
            // Handle the case where user is not authenticated or email is null
            // For example, you can display a default welcome message
            TextView textWelcome = findViewById(R.id.textWelcome);
            textWelcome.setText("Welcome!");
        }
    }

    // Method to encode email
    private String encodeEmail(String email) {
        // Split the email address at the "@" symbol
        String[] parts = email.split("@");

        // Get the username part (part before the "@" symbol)
        String username = parts[0];

        // Capitalize the first letter of the username
        String encodedEmail = username.substring(0, 1).toUpperCase() + username.substring(1);

        return encodedEmail;
    }


    private void addProduct() {
        // Retrieve product name and total product amount
        String productName = editTextProduct.getText().toString().trim();
        String totalAmountStr = editTextProductAmount.getText().toString().trim();

        // Validate product name
        if (productName.isEmpty()) {
            Toast.makeText(this, "Please enter a product name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate total product amount
        if (totalAmountStr.isEmpty()) {
            Toast.makeText(this, "Please enter the total product amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse total product amount to integer
        int totalProductAmount = Integer.parseInt(totalAmountStr);

        // Create a HashMap to represent the product data
        HashMap<String, Object> productData = new HashMap<>();
        productData.put("name", productName);
        productData.put("amount", totalProductAmount); // Use totalProductAmount

        // Save the product data to the database
        productsRef.child(productName).setValue(productData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ShopActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShopActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeProduct(String productName) {
        // Get a reference to the product in the database
        DatabaseReference productRef = productsRef.child(productName);

        // Remove the product from the database
        productRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ShopActivity.this, "Product removed successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShopActivity.this, "Failed to remove product", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
