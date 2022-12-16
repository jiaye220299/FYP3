package com.example.fyp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Locale;

public class StaffLogin extends AppCompatActivity {

    private EditText Sname, Spass;
    private final String role = "Staff";
    private Button login_btn;
    DatabaseReference reference;
    private String EXTRA_MESSAGE;


    public void loginStaff(View view) {
        Sname=findViewById(R.id.Sname);
        Spass=findViewById(R.id.Spass);
        reference = FirebaseDatabase.getInstance("https://psm2-a0730-default-rtdb.firebaseio.com/").getReference("Profile");
        String name = Sname.getText().toString().toLowerCase(Locale.ROOT);
        String password = Spass.getText().toString().toLowerCase(Locale.ROOT);

        try {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(name)) {
                        reference.child(name).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UserHelperClass UserhelperClass = snapshot.getValue(UserHelperClass.class);
                                if (role.equals(UserhelperClass.getRole())) {
                                    if (password.equals(UserhelperClass.getPass())) {
                                        Intent intent = new Intent(StaffLogin.this, StaffHome.class);
                                        intent.putExtra(EXTRA_MESSAGE, name);
                                        Toast.makeText(StaffLogin.this, "Login Success", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(StaffLogin.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(StaffLogin.this, "You are not Registered As Staff", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    } else {
                        Toast.makeText(StaffLogin.this, "Staff Doesn't Exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        login_btn = findViewById(R.id.button2);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginStaff(view);
            }
        });
    }
}