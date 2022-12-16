package com.example.fyp3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

        public void UserLogin(View v) {
            Intent intents = new Intent(this, UserLogin.class);
            startActivity(intents);
        }

         public void StaffLogin(View v) {
             Intent intents = new Intent(this, StaffLogin.class);
             startActivity(intents);
         }

        public void ToRegister(View v) {
             Intent intents = new Intent(this, Registration.class);
             startActivity(intents);
        }


}