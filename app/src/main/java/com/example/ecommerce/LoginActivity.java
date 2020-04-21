package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Models.Users;
import com.example.ecommerce.Prevalent.AdminCategoryActivity;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText InputPhoneNumber,InputPassword;
    private Button Loginbtn;
    private ProgressDialog loadingBar;
    private TextView AdminLink,NotAdminLink;
    private ImageView back_arrow;
    private String parentDbName="User";
    private CheckBox chkBoxRemeberme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Loginbtn=(Button)findViewById(R.id.login_btn);
        InputPassword=(EditText) findViewById(R.id.login_password_input);
        InputPhoneNumber=(EditText) findViewById(R.id.login_phone_number_input);
        AdminLink=(TextView)findViewById(R.id.admin_panel_link);
        NotAdminLink=(TextView)findViewById(R.id.not_admin_panel_link);
        back_arrow=findViewById(R.id.back_arrow_login);
        loadingBar=new ProgressDialog(this);
        chkBoxRemeberme=(CheckBox)findViewById(R.id.remeber_me_chkb);
        Paper.init(this);
        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
       AdminLink.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Loginbtn.setText("Login Admin");
                       AdminLink.setVisibility(View.INVISIBLE);
                       NotAdminLink.setVisibility(View.VISIBLE);
                       parentDbName="Admins";


                   }
               });
       NotAdminLink.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Loginbtn.setText("Login");
               AdminLink.setVisibility(View.VISIBLE);
               NotAdminLink.setVisibility(View.INVISIBLE);
               parentDbName="User";
           }
       });
       back_arrow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i=new Intent(LoginActivity.this,IntroActivity.class);
               startActivity(i);
           }
       });
    }

    private void LoginUser() {
        String phone=InputPhoneNumber.getText().toString();
        String password=InputPassword.getText().toString();

        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please Write Your Phone Number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please Write Your password",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please Wait, We are Checking the Credentails");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            AllowAccessToAccount(phone,password);
            
        }
    }

    private void AllowAccessToAccount(final String phone, final String password) {


        if(chkBoxRemeberme.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(phone).exists())
                {


                    Users userData=dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                    if(userData.getPhone().equals(phone))
                    {

                        if(userData.getPassword().equals(password))
                        {
                            if(parentDbName.equals("Admins"))
                            {
                                Toast.makeText(LoginActivity.this, "Welcome Admin,Logged in Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent i=new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(i);
                            }else if(parentDbName.equals("User"))
                            {
                                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent i=new Intent(LoginActivity.this,HomeActivity.class);
                                Prevalent.currentonlineusers=userData;
                                startActivity(i);
                            }


                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Your Password is Incorrect", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent i=new Intent(LoginActivity.this,LoginActivity.class);
                            startActivity(i);
                        }
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"Hello", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with this"+phone+"Number Do not Exits", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
