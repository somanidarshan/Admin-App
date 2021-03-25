package com.example.admincollegeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.admincollegeapp.faculty.AddTeacher;
import com.example.admincollegeapp.faculty.UpdateFaculty;
import com.example.admincollegeapp.notice.DeleteNoticeActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    CardView card1,card2,card3,card4,card5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        card1=findViewById(R.id.addNotice);
        card2=findViewById(R.id.addgalleryImage);
        card3=findViewById(R.id.addebook);
        card4=findViewById(R.id.addFaculty);
        card5=findViewById(R.id.deleteNotice);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        card5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.addNotice:
                i=new Intent(MainActivity.this,UploadNotice.class);
                startActivity(i);
                break;
            case R.id.addgalleryImage:
                i=new Intent(MainActivity.this,UploadImage.class);
                startActivity(i);
                break;
            case R.id.addebook:
                i=new Intent(MainActivity.this,UploadpdfActivity.class);
                startActivity(i);
                break;
            case R.id.addFaculty:
                i=new Intent(MainActivity.this,UpdateFaculty.class);
                startActivity(i);
                break;
            case R.id.deleteNotice:
                i=new Intent(MainActivity.this, DeleteNoticeActivity.class);
                startActivity(i);
                break;




        }

    }
}