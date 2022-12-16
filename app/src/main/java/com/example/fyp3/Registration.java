package com.example.fyp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.Locale;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {

    private EditText regName, regCPassword, regPassword, regPhone,regEmail;
    private RadioGroup radioGroup;
    private RadioButton regRoles;
    private Button regBtn;
    private ImageView regImage;
    private StorageTask uploadTask;
    StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    UserHelperClass UserHelperClass;
    ProgressDialog progressDialog;
    Button mCaptureBtn;
    Uri image_uri;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;


    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    Pattern pat = Pattern.compile(emailRegex);


    String usernameVal = "^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=\\S+$)" +           //no white spaces
            ".{5,}" ;             //at least 6 characters



    private boolean validateUsername(){
        String val = regName.getText().toString();

        if (val.isEmpty()) {
            regName.setError("Field cannot be empty");
            regName.requestFocus();
        } else if (val.length() >= 15) {
            regName.setError("Username too long");
            regName.requestFocus();
        }  else if (val.length() <= 6) {
        regName.setError("Username too short");
        regName.requestFocus();
        }else if (!val.matches(usernameVal)) {
            regName.setError("Username is too simple (at least 1 digit + any letter)");
            regName.requestFocus();
        } else {
            regName.setError(null);
            return true;
        }
        return false;
    }

    private boolean validatePass() {
        String val = regPassword.getText().toString();

        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            regPassword.requestFocus();
        } else if (regPassword.length() <= 5) {
            regPassword.setError("Password Should at least 6 character");
            regPassword.requestFocus();
        } else if (regPassword.length() >= 16) {
            regPassword.setError("Password Should not more than 15 character");
            regPassword.requestFocus();
        } else {
            regPassword.setError(null);
            return true;
        }
        return false;
    }

    private boolean validatePhone() {
        String val = regPhone.getText().toString();

        if (val.isEmpty()) {
            regPhone.setError("Field cannot be empty");
            regPhone.requestFocus();
        } else {
            regPhone.setError(null);
            return true;
        }
        return false;
    }

    private boolean validateCPassword() {
        String val = regCPassword.getText().toString();
        String val1 = regPassword.getText().toString();

        if (val.isEmpty()) {
            regCPassword.setError("Field cannot be empty");
            regCPassword.requestFocus();
        } else if (!val.equals(val1)) {
            regCPassword.setError("Password doesn't matches");
        } else {
            regCPassword.setError(null);
            return true;
        }
        return false;
    }

    private boolean validateEmail() {
        String val = regEmail.getText().toString();

        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            regEmail.requestFocus();
        } else if (!pat.matcher(val).matches()) {
            regEmail.setError("Please Enter a valid Email");
            regEmail.requestFocus();
        } else {
            regEmail.setError(null);
            return true;
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        regName=findViewById(R.id.Name);
        regCPassword=findViewById(R.id.CPassword);
        regPassword=findViewById(R.id.Password);
        regPhone=findViewById(R.id.PhoneNo);
        regEmail=findViewById(R.id.Email);
        radioGroup = findViewById(R.id.radioGroup1);
        regImage = (ImageView) findViewById(R.id.select_image);
        regBtn=findViewById(R.id.Register);
        progressDialog = new ProgressDialog(Registration.this);
        UserHelperClass = new UserHelperClass();


        rootNode= FirebaseDatabase.getInstance("https://psm2-a0730-default-rtdb.firebaseio.com/");
        reference =rootNode.getReference("Profile");

        mCaptureBtn = findViewById(R.id.captureImage);
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
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

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateCPassword() && validateEmail() && validatePass() && validatePhone() && validateUsername() ){
                int radioID = radioGroup.getCheckedRadioButtonId();
                regRoles = findViewById(radioID);


                    String name = regName.getText().toString().toLowerCase(Locale.ROOT);
                    String pass = regPassword.getText().toString().toLowerCase(Locale.ROOT);
                    String phone = regPhone.getText().toString().toLowerCase(Locale.ROOT);
                    String mail = regEmail.getText().toString().toLowerCase(Locale.ROOT);
                    String role = regRoles.getText().toString();


                    //reference.push().setValue(name);
                    addDataToFirebase(name,pass,phone,mail,role);
                }
              }

            private void addDataToFirebase(String name, String pass, String phone, String mail, String role) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(name)){

                            Toast.makeText(Registration.this,"User Already Exist",Toast.LENGTH_SHORT).show();
                        }else {
                            UserHelperClass.setName(name);
                            UserHelperClass.setPass(pass);
                            UserHelperClass.setPhone(phone);
                            UserHelperClass.setMail(mail);
                            UserHelperClass.setRole(role);
                            UserHelperClass.setStorageReference(storageReference.toString());


                            reference.child(name).setValue(UserHelperClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Registration.this, "Registration Successfully", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Registration.this, "Registration Failure", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intents = new Intent(Registration.this, MainActivity.class);
                            startActivity(intents);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            });

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
    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    public void SelectImage(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 3);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            ImageView imageView = findViewById(R.id.select_image);
            imageView.setImageURI(selectedImage);

            storageReference = FirebaseStorage.getInstance("gs://psm2-a0730.appspot.com/").getReference("UserProfile").child(selectedImage.getLastPathSegment());
            storageReference.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Registration.this, "Uploaded", Toast.LENGTH_SHORT);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Registration.this, "Not Uploaded", Toast.LENGTH_SHORT);
                }
            });
        }
    }


}