package com.example.fyp3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class StaffOrder extends AppCompatActivity {


    private String LoginID;
    private String EXTRA_MESSAGE;
    private List<OrderClass> userList;
    StorageReference storageReference;
    private DatabaseReference reference,reference2;
    FirebaseDatabase firebaseDatabase2;
    Calendar calendar;
    private orderAdapter orderAdapter;
    Query databaseReference,re,re3;
    ListView listview;
    private TextView ddates;
    DatePickerDialog datePickerDialog;
    int val = 0;
    TextView collect;
    TextView ordertype;
    TextView remark;
    TextView address;
    TextView orderstatus;
    TextView phone;
    TextView estimate;
    private String oid;
    ConstraintLayout blogger_layout;
    Button btn_accept;
    private Button sub_btn, btn_out;
    BottomNavigationView bnv;


    private boolean validateDate(){
        String val = ddates.getText().toString();

        if (val.equals("Select Date")){
            Toast.makeText(StaffOrder.this,"Please Select Date",Toast.LENGTH_SHORT).show();
        }else{
            ddates.setError(null);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_order);


        Intent receive = getIntent();
        LoginID = receive.getStringExtra(EXTRA_MESSAGE);

        listview = findViewById(R.id.listview_id);
        blogger_layout = findViewById(R.id.blogger_layout_id3);
        blogger_layout.setVisibility(View.INVISIBLE);
        btn_accept = findViewById(R.id.btn_c);
        btn_accept.setVisibility(View.INVISIBLE);
        btn_out = findViewById(R.id.btn_o);
        btn_out.setVisibility(View.INVISIBLE);

        collect = findViewById(R.id.collect_id);
        phone = findViewById(R.id.phone_id);
        ordertype = findViewById(R.id.ordertype_id);
        remark = findViewById(R.id.remark_id);
        address = findViewById(R.id.address_id);
        estimate = findViewById(R.id.textView23);

        bnv = (BottomNavigationView) findViewById(R.id.bottom_navigation2);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.item5:
                        Intent intent = new Intent(StaffOrder.this, StaffProfile.class);
                        intent.putExtra(EXTRA_MESSAGE, LoginID);
                        startActivity(intent);
                        break;
                    case R.id.item6:
                        Intent receive = new Intent(StaffOrder.this, MainActivity.class);
                        receive.putExtra(EXTRA_MESSAGE, LoginID);
                        startActivity(receive);
                        break;
                }

                return true;
            }
        });






        re = FirebaseDatabase.getInstance("https://psm2-a0730-default-rtdb.firebaseio.com/").getReference("userOrder").orderByChild("order_status").equalTo("Accepted");
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


        reference = FirebaseDatabase.getInstance("https://psm2-a0730-default-rtdb.firebaseio.com/").getReference("userOrder");

        databaseReference = FirebaseDatabase.getInstance("https://psm2-a0730-default-rtdb.firebaseio.com/").getReference("userOrder").orderByChild("dev_name").equalTo(LoginID);

        userList = new ArrayList<>();
        orderAdapter = new orderAdapter(StaffOrder.this,userList);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderClass orderClass = (OrderClass) listview.getItemAtPosition(position);
                listview.setVisibility(View.GONE);
                blogger_layout.setVisibility(View.VISIBLE);
                btn_accept.setVisibility(View.VISIBLE);
                btn_out.setVisibility(View.VISIBLE);
                val = 1;
                String timee = orderClass.getTime().toString();
                String ordertypee = orderClass.getOrderType().toString();
                String remarkss = orderClass.getOrderRemark().toString();
                String usernamee = orderClass.getUsername().toString();
                String addressess = orderClass.getOrderAddress().toString();
                String estimate_date = orderClass.getEstimate_date().toString();
                String orderstatuss = orderClass.getOrder_status().toString();

                ordertype.setText(ordertypee);
                remark.setText(remarkss);
                address.setText(addressess);
                collect.setText(usernamee);
                estimate.setText(estimate_date);



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
                FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://psm2-a0730-default-rtdb.firebaseio.com/");
                DatabaseReference reference4 = rootNode.getReference("userOrder");

                ddates = findViewById(R.id.ddate);
                ddates.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DATE);
                        datePickerDialog = new DatePickerDialog(StaffOrder.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month = month +1;
                                String date = makeDateString(day,month,year);
                                ddates.setText(date);

                                HashMap hashMap = new HashMap();
                                hashMap.put("estimate_date", date);
                                reference.child(oid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(StaffOrder.this, "Estimated Collection Date Is Set", Toast.LENGTH_SHORT).show();
                                        Intent intents = new Intent(StaffOrder.this, StaffOrder.class);
                                        intents.putExtra(EXTRA_MESSAGE, LoginID);
                                        startActivity(intents);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(StaffOrder.this, "Failure To Set", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        },year,month,day);
                        datePickerDialog.show();
                    }
                });





                reference2 = FirebaseDatabase.getInstance("https://psm2-a0730-default-rtdb.firebaseio.com/").getReference("Profile");
                try {
                    reference2.child(usernamee).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserHelperClass helperClass = snapshot.getValue(UserHelperClass.class);
                            String userPhoneFromDB = helperClass.getPhone();

                            phone.setText(userPhoneFromDB);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String STATUS = "DONE";

                        HashMap hashMap = new HashMap();
                        hashMap.put("order_status", STATUS);
                        reference.child(oid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(StaffOrder.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                                Intent intents = new Intent(StaffOrder.this, StaffOrder.class);
                                intents.putExtra(EXTRA_MESSAGE, LoginID);
                                startActivity(intents);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(StaffOrder.this, "Update Failure", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });




                btn_out.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String STATUS = "OUT FOR COLLECTION";

                        HashMap hashMap = new HashMap();
                        hashMap.put("order_status", STATUS);
                        reference.child(oid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(StaffOrder.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                                Intent intents = new Intent(StaffOrder.this, StaffOrder.class);
                                intents.putExtra(EXTRA_MESSAGE, LoginID);
                                startActivity(intents);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(StaffOrder.this, "Update Failure", Toast.LENGTH_SHORT).show();
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

    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month)+" "+day+" "+year;
    }

    private String getMonthFormat(int month){
        if (month == 1){
            return "JAN";
        }
        if (month == 2){
            return "FEB";
        }
        if (month == 3){
            return "MAR";
        }
        if (month == 4){
            return "APR";
        }
        if (month == 5){
            return "MAY";
        }
        if (month == 6){
            return "JUN";
        }
        if (month == 7){
            return "JUL";
        }
        if (month == 8){
            return "AUG";
        }
        if (month == 9){
            return "SEP";
        }
        if (month == 10){
            return "OCT";
        }
        if (month == 11){
            return "NOV";
        }
        if (month == 12){
            return "DEC";
        }
        return "JAN";
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
                Toast.makeText(StaffOrder.this,error.getMessage(), Toast.LENGTH_SHORT).show();
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
