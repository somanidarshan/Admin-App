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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button loginbtn;
    EditText emailid,password;
    FirebaseAuth mfirebaseauth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mfirebaseauth=FirebaseAuth.getInstance();
        emailid=findViewById(R.id.emailtext);
        password=findViewById(R.id.password);
        awesomeValidation= new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(LoginActivity.this, R.id.emailtext, "[a-zA-Z0-9._-]+@spit+\\.+ac.in+", R.string.err_email);
        loginbtn=findViewById(R.id.loginbtn);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mfirebaseUser = mfirebaseauth.getCurrentUser();
                if (mfirebaseUser != null) {
                    Toast.makeText(LoginActivity.this, "You are Logged In", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String upemail = emailid.getText().toString();
                String uppassword = password.getText().toString();
                if (upemail.isEmpty()) {
                    emailid.setError("Please Enter the Email id ");
                    emailid.requestFocus();
                } else if (uppassword.isEmpty()) {
                    password.setError("Please Enter the password");
                } else if (upemail.isEmpty() && uppassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
                } else if (!(upemail.isEmpty() && uppassword.isEmpty())) {
                    mfirebaseauth.signInWithEmailAndPassword(upemail, uppassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Error, Please Login Again", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(i);
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Error Occurred! During Sign UP", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}