package com.example.fyp3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StaffHome extends AppCompatActivity {

    private String LoginID;
    private String EXTRA_MESSAGE,EXTRA_MESSAGE2;
    private List<OrderClass> userList;
    StorageReference storageReference;
    private DatabaseReference reference,reference2;
    FirebaseDatabase firebaseDatabase2;

    private orderAdapter orderAdapter;
    Query databaseReference,re,re3;
    ListView listview;
    int val = 0;
    TextView date;
    TextView time;
    TextView ordertype;
    TextView remark;
    TextView address;
    TextView orderstatus;
    TextView user_name,phone;
    private String oid;
    ConstraintLayout blogger_layout;
    Button btn_accept;
    Vibrator vibrator;
    BottomNavigationView bnv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);


    Intent receive = getIntent();
    LoginID = receive.getStringExtra(EXTRA_MESSAGE);

    listview = findViewById(R.id.listview_id);
    blogger_layout = findViewById(R.id.blogger_layout_id2);
    blogger_layout.setVisibility(View.INVISIBLE);
    btn_accept = findViewById(R.id.btn_a);
    vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
    btn_accept.setVisibility(View.INVISIBLE);

    date = findViewById(R.id.date_id);
    time = findViewById(R.id.time_id);
    ordertype = findViewById(R.id.ordertype_id);
    remark = findViewById(R.id.remark_id);
    address = findViewById(R.id.address_id);


    bnv = (BottomNavigationView) findViewById(R.id.bottom_navigation2);
    bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.item5:
                        Intent intent = new Intent(StaffHome.this, StaffProfile.class);
                        intent.putExtra(EXTRA_MESSAGE, LoginID);
                        startActivity(intent);
                        break;
                    case R.id.item6:
                        Intent receive = new Intent(StaffHome.this, MainActivity.class);
                        receive.putExtra(EXTRA_MESSAGE, LoginID);
                        startActivity(receive);
                        break;
                }

                return true;
            }
        });


    reference = FirebaseDatabase.getInstance("https://psm2-a0730-default-rtdb.firebaseio.com/").getReference("userOrder");
    reference2 = FirebaseDatabase.getInstance("https://psm2-a0730-default-rtdb.firebaseio.com/").getReference("Profile");


    databaseReference = FirebaseDatabase.getInstance("https://psm2-a0730-default-rtdb.firebaseio.com/").getReference("userOrder").orderByChild("order_status").equalTo("PENDING");
    userList = new ArrayList<>();
    orderAdapter = new orderAdapter(StaffHome.this,userList);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            OrderClass orderClass = (OrderClass) listview.getItemAtPosition(position);
            listview.setVisibility(View.GONE);
            blogger_layout.setVisibility(View.VISIBLE);
            btn_accept.setVisibility(View.VISIBLE);
            val =1;
            String datee = orderClass.getDate().toString();
            String timee = orderClass.getTime().toString();
            String ordertypee = orderClass.getOrderType().toString();
            String remarkss = orderClass.getOrderRemark().toString();
            String usernamee = orderClass.getUsername().toString();
            String addressess = orderClass.getOrderAddress().toString();
            String orderstatuss = oid;

            date.setText(datee);
            time.setText(timee);
            ordertype.setText(ordertypee);
            remark.setText(remarkss);
            address.setText(addressess);


            re3 = FirebaseDatabase.getInstance("https://psm2-a0730-default-rtdb.firebaseio.com/").getReference("Profile").orderByChild("name").equalTo(usernamee);
           re = FirebaseDatabase.getInstance("https://psm2-a0730-default-rtdb.firebaseio.com/").getReference("userOrder").orderByChild("time").equalTo(timee);

            re.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                        oid = objSnapshot.getKey();
                    }
                }
                @Override
                public void onCancelled(DatabaseError firebaseError) {
                }
            });

            btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String STATUS = "ACCEPTED";

                    //Set Vibration Time in Millisecond;
                    vibrator.vibrate(1000);

                    HashMap hashMap = new HashMap();
                    hashMap.put("dev_name", LoginID);
                    hashMap.put("order_status", STATUS);
                    reference.child(oid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(StaffHome.this, "Edit Successfully", Toast.LENGTH_SHORT).show();
                            Intent intents = new Intent(StaffHome.this, StaffHome.class);
                            intents.putExtra(EXTRA_MESSAGE, LoginID);
                            startActivity(intents);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(StaffHome.this, "Edit Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    });
}



    public void DeliveryChoose(View v) {
        Intent intents = new Intent(this, StaffHome.class);
        intents.putExtra(EXTRA_MESSAGE, LoginID);
        startActivity(intents);
    }

    public void DeliveryOrder(View v) {
        Intent intents = new Intent(this, StaffOrder.class);
        intents.putExtra(EXTRA_MESSAGE, LoginID);
        startActivity(intents);
    }



    @Override
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
                listview.setAdapter(orderAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StaffHome.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        super.onStart();
    }


    @Override
    public void onBackPressed() {
        if(val ==1)
        {
            listview.setVisibility(View.VISIBLE);
            blogger_layout.setVisibility(View.GONE);
            btn_accept.setVisibility(View.INVISIBLE);
            val = 0;
        }
        else if (val == 0)
        {
            super.onBackPressed();
        }
    }
}

