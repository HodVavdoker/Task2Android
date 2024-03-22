package com.example.shoppingmanagementhod;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        mAuth = FirebaseAuth.getInstance();
    }

    public void regFunc(View view) {

        String email = ((EditText) findViewById(R.id.textEmailAddress)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.textPassword)).getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "reg ok", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this , MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "reg failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void addButtonFunc(View view) {

        String email = ((EditText) findViewById(R.id.textEmailAddress)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.textPutPassword)).getText().toString().trim();
        String phone = ((EditText) findViewById(R.id.editTextPhone)).getText().toString().trim();
      //  Data data = new Data(email , password, phone );
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(email);
      //  myRef.setValue(data);
    }

    public void readFunc(View view){

        String email = ((EditText) findViewById(R.id.textEmailAddress)).getText().toString().trim();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(email);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Data value = dataSnapshot.getValue(Data.class);
                Toast.makeText(RegisterActivity.this , value.toString() , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void registryFunc(View view) {
        String email = ((EditText) findViewById(R.id.textEmailAddressReg)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.textPutPassword)).getText().toString().trim();
        String phone = ((EditText) findViewById(R.id.editTextPhone)).getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, email, Toast.LENGTH_LONG).show();

                            if (email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                                Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                            } else {
                                // If all fields are filled, proceed with intent
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
    }

}
