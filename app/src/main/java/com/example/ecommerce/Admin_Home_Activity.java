package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_Home_Activity extends AppCompatActivity {
    private StorageReference CategoryImageRef;
    private CircleImageView Category_image;
    private ImageView Change_Image;
    private Uri imageUri;
    private ProgressDialog loadingBar;
    private String productRandomKey, downloadImageUrl, saveCurrentDate, saveCurrentTime;
    private Button Add_Category;
    private EditText Category_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__home);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navgation);
        bottomNavigationView.setSelectedItemId(R.id.home_dashboard);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_Products:
                        startActivity(new Intent(getApplicationContext(), Admin_add_product_Activity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home_dashboard:
                        return true;
                    case R.id.view_Products:
                        startActivity(new Intent(getApplicationContext(), Admin_edit_product_Activity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Orders:
                        startActivity(new Intent(getApplicationContext(), Admin_Orders_Activity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }


                return false;
            }
        });


        loadingBar = new ProgressDialog(this);
        CategoryImageRef = FirebaseStorage.getInstance().getReference().child("Category Pictures");
        Add_Category = findViewById(R.id.add_category_btn);
        Category_name = findViewById(R.id.add_category_name);
        Category_image = findViewById(R.id.Category_profileimage);
        Change_Image = findViewById(R.id.Change_Profile_Category);

        Change_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(Admin_Home_Activity.this);
            }
        });

        Add_Category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savetoDatabase();
            }
        });

    }

    private void savetoDatabase() {
        final String name = Category_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please Write Category name", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please Wait, We are Checking the Credentails");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filepath = CategoryImageRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filepath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(Admin_Home_Activity.this, "Error :" + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Admin_Home_Activity.this, "Product Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(Admin_Home_Activity.this, "Getting Product image URL Successfull", Toast.LENGTH_SHORT).show();
                            if (downloadImageUrl != null) {
                                addtoDatabase(name, downloadImageUrl);
                            } else {
                                Toast.makeText(Admin_Home_Activity.this, "Please Select the Image", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Admin_Home_Activity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Admin_Home_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void addtoDatabase(final String name, final String downloadImageUrl) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("Category").child(name).exists()) {
                    HashMap<String, Object> CategoryMap = new HashMap<>();
                    CategoryMap.put("CategoryName", name);
                    CategoryMap.put("image", downloadImageUrl);
                    RootRef.child("Category").child(name).updateChildren(CategoryMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Admin_Home_Activity.this, "Your Account has Been Created", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(Admin_Home_Activity.this, "Network Error Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(Admin_Home_Activity.this, "This " + name + "Already Exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            Category_image.setImageURI(imageUri);

        } else {
            Toast.makeText(this, "Error Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Admin_Home_Activity.this, Admin_Home_Activity.class));
            finish();
        }
    }
}
