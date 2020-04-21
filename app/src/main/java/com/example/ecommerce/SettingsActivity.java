package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.security.PrivateKey;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity
{
    private CircleImageView profileImageView;
    private EditText name,phone_No,Useraddress;
    private TextView closeTextBtn,saveBtn;
    private ImageView profileChange;

    private Uri imageUri;
    private String myUri="";
    private StorageTask uploadTask;
    private StorageReference storageProfilepictureRef;
    private String checked="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        storageProfilepictureRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        profileImageView=findViewById(R.id.settings_profileimage);
        profileChange=findViewById(R.id.Change_Profile);
        name=findViewById(R.id.setting_Full_name);
        phone_No=findViewById(R.id.setting_phone);
        Useraddress=findViewById(R.id.setting_Address);
        closeTextBtn=findViewById(R.id.close_settings_btn);
        saveBtn=findViewById(R.id.update_account_settings_btn);

        userinfoDisplay(profileImageView,name,phone_No,Useraddress);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(checked.equals("clicked"))
                {
                    userInfosaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }

            }
        });
        profileChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                checked="clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);


            }
        });

    }

    private void updateOnlyUserInfo()
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("User");

        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("name",name.getText().toString());
        userMap.put("address",Useraddress.getText().toString());
        userMap.put("phone",phone_No.getText().toString());
        ref.child(Prevalent.currentonlineusers.getPhone()).updateChildren(userMap);


        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile Info Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();

            profileImageView.setImageURI(imageUri);

        }
        else
        {
            Toast.makeText(this, "Error Try Again", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }
    }

    private void userInfosaved()
    {
        if(TextUtils.isEmpty(name.getText().toString()))
        {
            Toast.makeText(this, "Name is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(Useraddress.getText().toString()))
        {
            Toast.makeText(this, "Address is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(phone_No.getText().toString()))
        {
            Toast.makeText(this, "Phone No is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(checked.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage()
    {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("update Profile");
        progressDialog.setMessage("Please Wait, While We are updating your Account Information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri!=null)
        {
            final StorageReference fileRef=storageProfilepictureRef.child(Prevalent.currentonlineusers.getPhone()+".jpg");
            uploadTask=fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if(!task.isSuccessful())
                    {
                        Log.e("task", "then: ",task.getException() );
                        throw task.getException();
                    }
                    return  fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if(task.isSuccessful())
                    {
                        Uri downloadUri=task.getResult();
                        myUri=downloadUri.toString();

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("User");

                        HashMap<String,Object> userMap=new HashMap<>();
                        userMap.put("name",name.getText().toString());
                        userMap.put("address",Useraddress.getText().toString());
                        userMap.put("phone",phone_No.getText().toString());
                        userMap.put("image",myUri);
                        ref.child(Prevalent.currentonlineusers.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();
                        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                        Toast.makeText(SettingsActivity.this, "Profile Info Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    //Toast.makeText(SettingsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            });

        }
        else
        {
            Toast.makeText(this, "Image is Not Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void userinfoDisplay(final CircleImageView profileImageView, final EditText name, EditText phone_no, final EditText Useraddress)
    {
        DatabaseReference UserRef= FirebaseDatabase.getInstance().getReference().child("User").child(Prevalent.currentonlineusers.getPhone());
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("image").exists())
                    {
                        String image=dataSnapshot.child("image").getValue().toString();
                        String fullname=dataSnapshot.child("name").getValue().toString();
                        String phone=dataSnapshot.child("phone").getValue().toString();
                        String address=dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        name.setText(fullname);
                        phone_No.setText(phone);
                        Useraddress.setText(address);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
