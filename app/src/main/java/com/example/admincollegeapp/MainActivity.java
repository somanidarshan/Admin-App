package com.example.admincollegeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.admincollegeapp.faculty.AddTeacher;
import com.example.admincollegeapp.faculty.UpdateFaculty;
import com.example.admincollegeapp.notice.DeleteNoticeActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    CardView card1,card2,card3,card4,card5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout linearLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable =(AnimationDrawable)linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(6000);
        animationDrawable.start();


        card1 = findViewById(R.id.addNotice);
        card2 = findViewById(R.id.addgalleryImage);
        card3 = findViewById(R.id.addebook);

        card5 = findViewById(R.id.deleteNotice);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card5.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.addNotice:
                i=new Intent(MainActivity.this,UploadNotice.class);
                startActivity(i);
                overridePendingTransition(R.anim.left_slide_in,R.anim.right_slide_out);
                break;
            case R.id.addgalleryImage:
                i=new Intent(MainActivity.this,UploadImage.class);
                startActivity(i);
                overridePendingTransition(R.anim.left_slide_in,R.anim.right_slide_out);
                break;
            case R.id.addebook:
                i=new Intent(MainActivity.this,UploadpdfActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.left_slide_in,R.anim.right_slide_out);
                break;
            case R.id.deleteNotice:
                i=new Intent(MainActivity.this, DeleteNoticeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.left_slide_in,R.anim.right_slide_out);
                break;

        }

    }
}