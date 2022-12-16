package com.example.fyp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserTracking extends AppCompatActivity {

    private String LoginID;
    private String EXTRA_MESSAGE;
    private List<OrderClass> userList;


    private orderAdapter2 orderAdapter2;
    Query databaseReference;
    ListView listview;
    TextView date;
    TextView time;
    TextView ordertype;
    TextView remarks;
    TextView addresses;
    TextView order_status;
    TextView dev;
    TextView estimate;
    ConstraintLayout blogger_layout;
    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tracking);

        Intent intent = getIntent();
        LoginID = intent.getStringExtra(EXTRA_MESSAGE);

        listview = findViewById(R.id.listview_id2);
        blogger_layout = findViewById(R.id.blogger_layout_id2);

        date = findViewById(R.id.date_id2);
        time = findViewById(R.id.time_id2);
        ordertype = findViewById(R.id.ordertype_id2);
        remarks = findViewById(R.id.remark_id2);
        addresses = findViewById(R.id.address_id2);
        order_status = findViewById(R.id.orderstatus_id2);
        dev = findViewById(R.id.dev_id2);
        estimate = findViewById(R.id.estimate_id2);



        databaseReference = FirebaseDatabase.getInstance("https://psm2-a0730-default-rtdb.firebaseio.com/").getReference("userOrder").orderByChild("username").equalTo(LoginID);
        userList = new ArrayList<>();
        orderAdapter2 = new orderAdapter2(UserTracking.this, userList);


        bnv = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.item1:
                        Intent intent = new Intent(UserTracking.this, UserHome.class);
                        intent.putExtra(EXTRA_MESSAGE, LoginID);
                        startActivity(intent);
                        break;
                    case R.id.item2:

                        break;
                    case R.id.item3:
                        Intent receive = new Intent(UserTracking.this, Profile.class);
                        receive.putExtra(EXTRA_MESSAGE, LoginID);
                        startActivity(receive);
                        break;
                    case R.id.item4:
                        Intent intents = new Intent(UserTracking.this, MainActivity.class);
                        Toast.makeText(UserTracking.this,"Log Out Successfully",Toast.LENGTH_SHORT).show();
                        intents.putExtra(EXTRA_MESSAGE, LoginID);
                        startActivity(intents);
                        break;
                }

                return true;
            }
        });
    }

    protected void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    OrderClass orderClass = dataSnapshot1.getValue(OrderClass.class);
                    userList.add(orderClass);
                }
                listview.setAdapter(orderAdapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserTracking.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        super.onStart();
    }
}