package com.example.fyp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

public class Profile extends AppCompatActivity {

    private String LoginID, EXTRA_MESSAGE;
    private Button edit_btn, image_btn;
    private Uri ImageUri;
    private ImageView userImage;
    private EditText user_phone, user_mail, user_pass;
    private DatabaseReference reference;
    private int clickcount = 0;
    StorageReference storageReference;
    FirebaseStorage storage;
    Uri image_uri;
    private static final int PERMISSION_CODE = 1000;
    BottomNavigationView bnv;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    Pattern pat = Pattern.compile(emailRegex);

    private boolean validatePass() {
        String val =  user_pass.getText().toString();

        if (val.isEmpty()) {
            user_pass.setError("Field cannot be empty");
            user_pass.requestFocus();
        } else if ( user_pass.length() <= 5) {
            user_pass.setError("Password Should at least 6 character");
            user_pass.requestFocus();
        } else if ( user_pass.length() >= 16) {
            user_pass.setError("Password Should not more than 15 character");
            user_pass.requestFocus();
        } else {
            user_pass.setError(null);
            return true;
        }
        return false;
    }

    private boolean validatePhone() {
        String val = user_phone.getText().toString();

        if (val.isEmpty()) {
            user_phone.setError("Field cannot be empty");
            user_phone.requestFocus();
        } else {
            user_phone.setError(null);
            return true;
        }
        return false;
    }

    private boolean validateEmail() {
        String val = user_mail.getText().toString();

        if (val.isEmpty()) {
            user_mail.setError("Field cannot be empty");
            user_mail.requestFocus();
        } else if (!pat.matcher(val).matches()) {
            user_mail.setError("Please Enter a valid Email");
            user_mail.requestFocus();
        } else {
            user_mail.setError(null);
            return true;
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        LoginID = intent.getStringExtra(EXTRA_MESSAGE);

        user_phone = (EditText) findViewById(R.id.userphone_et);
        user_mail = (EditText) findViewById(R.id.usermail_et);
        user_pass = (EditText) findViewById(R.id.userpassword_et);
        userImage = (ImageView) findViewById(R.id.profileImage);

        bnv = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.item1:
                        Intent intent = new Intent(Profile.this, UserHome.class);
                        intent.putExtra(EXTRA_MESSAGE,  LoginID);
                        startActivity(intent);
                        break;
                    case R.id.item2:

                        break;
                    case R.id.item3:
                        Intent receive = new Intent(Profile.this, Profile.class);
                        receive.putExtra(EXTRA_MESSAGE,  LoginID);
                        startActivity(receive);
                        break;
                    case R.id.item4:
                        Intent intents = new Intent(Profile.this, MainActivity.class);
                        Toast.makeText(Profile.this,"Log Out Successfully",Toast.LENGTH_SHORT).show();
                        intents.putExtra(EXTRA_MESSAGE,  LoginID);
                        startActivity(intents);
                        break;
                }

                return true;
            }
        });


        reference = FirebaseDatabase.getInstance("https://psm2-a0730-default-rtdb.firebaseio.com/").getReference("Profile");

        try {
            reference.child(LoginID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserHelperClass helperClass = snapshot.getValue(UserHelperClass.class);
                    String userPhoneFromDB = helperClass.getPhone();
                    String userMailFromDB = helperClass.getMail();
                    String userPassFromDB = helperClass.getPass();
                    String userImageAddressFromDB = helperClass.getStorageReference();

                    String cur = userImageAddressFromDB;
                    String[] sep = cur.split("UserProfile/");

                    StorageReference sRef = FirebaseStorage.getInstance("gs://psm2-a0730.appspot.com/").getReference("UserProfile").child(sep[1].replace("%3A", ":"));
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).resize(125, 125).centerCrop().into(userImage);
                        }
                    });


                    user_phone.setText(userPhoneFromDB);
                    user_mail.setText(userMailFromDB);
                    user_pass.setText(userPassFromDB);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        edit_btn = (Button) findViewById(R.id.edit_profile_btn);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateEmail() && validatePass() && validatePhone() ){
                String phone = user_phone.getText().toString().trim().toLowerCase(Locale.ROOT);
                String mail = user_mail.getText().toString().trim().toLowerCase(Locale.ROOT);
                String pass = user_pass.getText().toString().trim().toLowerCase(Locale.ROOT);

                HashMap hashMap = new HashMap();
                hashMap.put("phone", phone);
                hashMap.put("mail", mail);
                hashMap.put("pass", pass);
                if (clickcount == 0) {
                } else {
                    hashMap.put("storageReference", storageReference.toString());
                }
                reference.child(LoginID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(Profile.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "Update Failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }}
        });

        image_btn = (Button) findViewById(R.id.captureProfile);
        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        openCamera();
                    }
                } else {
                    openCamera();
                }
            }
        });
    }

    public void SelectImage(View v) {
        clickcount = clickcount + 1;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 3);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            ImageView imageView = findViewById(R.id.profileImage);
            imageView.setImageURI(selectedImage);

            storageReference = FirebaseStorage.getInstance("gs://psm2-a0730.appspot.com/").getReference("UserProfile").child(selectedImage.getLastPathSegment());
            try {
                storageReference.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Profile.this, "Uploaded", Toast.LENGTH_SHORT);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "Not Uploaded", Toast.LENGTH_SHORT);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, 3);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }





}