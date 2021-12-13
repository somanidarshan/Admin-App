package com.example.admincollegeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    EditText email,password;
    Button signin,login;
    FirebaseAuth mfirebaseauth;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        email=findViewById(R.id.emailtext);
        login=findViewById(R.id.loginbtn);
        signin=findViewById(R.id.signinbtn);
        mfirebaseauth=FirebaseAuth.getInstance();
        password=findViewById(R.id.pwd);
        awesomeValidation= new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(LoginPage.this, R.id.emailtext, "[a-zA-Z0-9._-]+@spit+\\.+ac.in+", R.string.err_email);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String upemail = email.getText().toString();
                String uppassword = password.getText().toString();
                if (awesomeValidation.validate()) {
                    if (upemail.isEmpty()) {
                        email.setError("Please Enter the Email id ");
                        email.requestFocus();
                    } else if (uppassword.isEmpty()) {
                        password.setError("Please Enter the password");
                    } else if (upemail.isEmpty() && uppassword.isEmpty()) {
                        Toast.makeText(LoginPage.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
                    } else if (!(upemail.isEmpty() && uppassword.isEmpty())) {
                        mfirebaseauth.createUserWithEmailAndPassword(upemail, uppassword).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(LoginPage.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(LoginPage.this, MainActivity.class));

                                }
                            }
                        });


                    } else {
                        Toast.makeText(LoginPage.this, "Error Occurred! During Sign UP", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(LoginPage.this, "Invalid ID", Toast.LENGTH_SHORT).show();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginPage.this, " Please Login!!", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(LoginPage.this,LoginActivity.class);
                startActivity(i);
            }
        });


    }
}