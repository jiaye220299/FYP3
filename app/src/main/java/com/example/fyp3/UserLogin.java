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

public class UserLogin extends AppCompatActivity {

    private EditText Uname, Upass;
    private String EXTRA_MESSAGE;
    private final String role = "User";
    private Button login_btn;
    DatabaseReference reference;


    public void loginUser(View view) {
        Uname=findViewById(R.id.uname);
        Upass=findViewById(R.id.upass);
        reference = FirebaseDatabase.getInstance("https://psm2-a0730-default-rtdb.firebaseio.com/").getReference("Profile");
        String username = Uname.getText().toString().toLowerCase(Locale.ROOT);
        String userpassword = Upass.getText().toString().toLowerCase(Locale.ROOT);

        try {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(username)) {
                        reference.child(username).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UserHelperClass UserhelperClass = snapshot.getValue(UserHelperClass.class);
                                if (role.equals(UserhelperClass.getRole())) {
                                    if (userpassword.equals(UserhelperClass.getPass())) {
                                        Intent intent = new Intent(UserLogin.this, UserHome.class);
                                        intent.putExtra(EXTRA_MESSAGE, username);
                                        Toast.makeText(UserLogin.this, "Login Success", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(UserLogin.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(UserLogin.this, "You are not Registered As User", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    } else {
                        Toast.makeText(UserLogin.this, "User Doesn't Exist", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_user_login);

        login_btn = findViewById(R.id.button3);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(view);
            }
        });
    }
}