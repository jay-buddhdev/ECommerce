package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountbtn;
    private EditText InputName,InputPhoneNumber,InputPassword,InputAddress,InputEmailAdress;
    private ProgressDialog loadingBar;
    private StorageReference ProfileImageRef;
    private CircleImageView Register_image;
    private ImageView Change_Image,back_arrow;
    private String productRandomKey,downloadImageUrl,saveCurrentDate,saveCurrentTime;
    private Uri imageUri;
    private StorageReference ProductImageRef;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth= FirebaseAuth.getInstance();
        ProfileImageRef= FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        createAccountbtn=(Button)findViewById(R.id.register_btn);
        InputName=(EditText) findViewById(R.id.register_username_input);
        InputPhoneNumber=(EditText) findViewById(R.id.register_phone_number_input);
        InputPassword=(EditText) findViewById(R.id.register_password_input);
        InputAddress=(EditText)findViewById(R.id.register_address_input);
        InputEmailAdress=findViewById(R.id.register_email_input);
        back_arrow=findViewById(R.id.back_arrow_register);

        loadingBar=new ProgressDialog(this);
        Register_image=findViewById(R.id.Register_profileimage);
        Change_Image=findViewById(R.id.Change_Profile_Register);
        Change_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(RegisterActivity.this);
            }
        });

        createAccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();

            Register_image.setImageURI(imageUri);

        }
        else
        {
            Toast.makeText(this, "Error Try Again", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(RegisterActivity.this,RegisterActivity.class));
            finish();
        }
    }

    private void CreateAccount()
    {
        SaveProfileImage();

    }

    private void SaveProfileImage()
    {
        final String name=InputName.getText().toString();
        final String phone=InputPhoneNumber.getText().toString();
        final String password=InputPassword.getText().toString();
        final String address=InputAddress.getText().toString();
        final String email=InputEmailAdress.getText().toString();
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please Write Your name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please Write Your Phone Number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please Write Your password",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(address))
        {
            Toast.makeText(this,"Please Write Your Address",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please Write Your E-mail Address",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please Wait, We are Checking the Credentails");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

        }
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentDate.format(calendar.getTime());

        productRandomKey=saveCurrentDate+saveCurrentTime;

        final StorageReference filepath=ProfileImageRef.child(imageUri.getLastPathSegment()+productRandomKey+".jpg");

        final UploadTask uploadTask=filepath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message=e.toString();
                Toast.makeText(RegisterActivity.this, "Error :"+message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
              //  Toast.makeText(RegisterActivity.this, "Product Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if(!task.isSuccessful())
                        {
                            throw  task.getException();
                        }
                        downloadImageUrl=filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl=task.getResult().toString();
                    //        Toast.makeText(RegisterActivity.this, "Getting Product image URL Successfull", Toast.LENGTH_SHORT).show();
                            if(downloadImageUrl!=null)
                            {
                                ValidatephoneNumber(name, phone, password, address, downloadImageUrl,email);
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this, "Please Select the Image", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void ValidatephoneNumber(final String name, final String phone, final String password, final String address, final String finalImage, final String email)
    {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    final DatabaseReference RootRef;
                    RootRef= FirebaseDatabase.getInstance().getReference();
                    RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!(dataSnapshot.child("Users").child(phone).exists()))
                            {
                                HashMap<String,Object> userdataMap=new HashMap<>();
                                userdataMap.put("phone",phone);
                                userdataMap.put("password",password);
                                userdataMap.put("name",name);
                                userdataMap.put("address",address);
                                userdataMap.put("email",email);
                                userdataMap.put("image",finalImage);
                                userdataMap.put("UID",FirebaseAuth.getInstance().getCurrentUser().getUid());


                                RootRef.child("User").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(RegisterActivity.this,"Your Account has Been Created",Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                            Intent  i=new Intent(RegisterActivity.this,LoginActivity.class);
                                            startActivity(i);
                                        }
                                        else
                                        {
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Network Error Please Try Again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this,"This "+phone+"Already Exist",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent i=new Intent(RegisterActivity.this,IntroActivity.class);
                                startActivity(i);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }
}
