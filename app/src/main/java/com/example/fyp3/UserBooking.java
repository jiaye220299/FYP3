package com.example.fyp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class UserBooking extends AppCompatActivity {

    String[] items ={"Sofa", "Table", "Electronic device", "Cupboard", "Others"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;

    private String EXTRA_MESSAGE;
    private String username;
    private String id;
    private EditText order_address, order_remark;
    private Button sub_btn;
    private String dev_name = " ", order_status = " ", estimate_date = " ";
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    BottomNavigationView bnv;

    FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://psm2-a0730-default-rtdb.firebaseio.com/");
    DatabaseReference reference = rootNode.getReference("userOrder");
    OrderClass orderClass;

    private boolean validateDetails(){
        String val = order_address.getText().toString();

        if (val.isEmpty()){
            order_address.setError("Field cannot be empty");
        }else{
            order_address.setError(null);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_booking);

        order_address= findViewById(R.id.address);
        order_remark= findViewById(R.id.remark);
        sub_btn=findViewById(R.id.button4);
        orderClass = new OrderClass();

        autoCompleteTxt=findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,items);

        Intent receive = getIntent();
        String username = receive.getStringExtra(EXTRA_MESSAGE);

        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String items = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Item:"+items,Toast.LENGTH_SHORT).show();
            }
        });

        sub_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (validateDetails()){
                    String address = order_address.getText().toString().toLowerCase(Locale.ROOT);
                    String remark = order_remark.getText().toString().toLowerCase(Locale.ROOT);
                    String item = autoCompleteTxt.getText().toString().toLowerCase(Locale.ROOT);
                    String order_status = "PENDING";
                    String dev_name = "";
                    String estimate_date = "";
                    String id = username.toString().trim();

                    Calendar calendar = Calendar.getInstance();
                    String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    String currentTime = DateFormat.getTimeInstance().format(calendar.getTime());


                    orderClass.setDate(currentDate);
                    orderClass.setTime(currentTime);
                    orderClass.setOrderAddress(address);
                    orderClass.setOrderRemark(remark);
                    orderClass.setOrderType(item);
                    orderClass.setOrder_status(order_status);
                    orderClass.setUsername(id);
                    orderClass.setDev_name(dev_name);
                    orderClass.setEstimate_date(estimate_date);

                    String ts = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

                    reference.child(ts).setValue(orderClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(UserBooking.this, "Order Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserBooking.this, "Order Failure", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Intent intents = new Intent(UserBooking.this, UserHome.class);
                    intents.putExtra(EXTRA_MESSAGE, username);
                    startActivity(intents);


                }
            }
        }

        );

        bnv = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.item1:
                        Intent intent = new Intent(UserBooking.this, UserHome.class);
                        intent.putExtra(EXTRA_MESSAGE, username);
                        startActivity(intent);
                        break;
                    case R.id.item2:

                        break;
                    case R.id.item3:
                        Intent receive = new Intent(UserBooking.this, Profile.class);
                        receive.putExtra(EXTRA_MESSAGE, username);
                        startActivity(receive);
                        break;
                    case R.id.item4:
                        Intent intents = new Intent(UserBooking.this, MainActivity.class);
                        Toast.makeText(UserBooking.this,"Log Out Successfully",Toast.LENGTH_SHORT).show();
                        intents.putExtra(EXTRA_MESSAGE, username);
                        startActivity(intents);
                        break;
                }

                return true;
            }
        });

    }


}