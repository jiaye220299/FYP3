package com.example.fyp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class UserHome extends AppCompatActivity {

    private String EXTRA_MESSAGE;
    private String username;

    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        TextView unames;
        unames = findViewById(R.id.textView12);
        Intent receive = getIntent();
        username = receive.getStringExtra(EXTRA_MESSAGE);
        unames.setText(" Hi " + username);


        bnv = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.item1:
                        Intent intent = new Intent(UserHome.this, UserHome.class);
                        intent.putExtra(EXTRA_MESSAGE, username);
                        startActivity(intent);
                        break;
                    case R.id.item2:

                        break;
                    case R.id.item3:
                        Intent receive = new Intent(UserHome.this, Profile.class);
                        receive.putExtra(EXTRA_MESSAGE, username);
                        startActivity(receive);
                        break;
                    case R.id.item4:
                        Intent intents = new Intent(UserHome.this, MainActivity.class);
                        Toast.makeText(UserHome.this,"Log Out Successfully",Toast.LENGTH_SHORT).show();
                        intents.putExtra(EXTRA_MESSAGE, username);
                        startActivity(intents);
                        break;
                }

                return true;
            }
        });


    }


    public void books(View view) {
        Intent intent = new Intent(UserHome.this, UserBooking.class);
        intent.putExtra(EXTRA_MESSAGE, username);
        startActivity(intent);

    }

    public void truckss(View view) {
        Intent intent = new Intent(UserHome.this, UserTracking.class);
        intent.putExtra(EXTRA_MESSAGE, username);
        startActivity(intent);
    }

    public void maps(View view) {
        Intent intent = new Intent(UserHome.this, MapsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, username);
        startActivity(intent);
    }
}
