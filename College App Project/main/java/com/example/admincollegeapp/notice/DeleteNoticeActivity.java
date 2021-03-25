package com.example.admincollegeapp.notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admincollegeapp.NoticeData;
import com.example.admincollegeapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeleteNoticeActivity extends AppCompatActivity {

   private  RecyclerView deleteNoticerecyclerview;
    private ProgressBar progressbar;
    private ArrayList<NoticeData> list;
    private NoticeAdapter adapter;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notice);
        deleteNoticerecyclerview=findViewById(R.id.deleteNoticerecyclerview);
        progressbar=findViewById(R.id.progressbar);
        reference= FirebaseDatabase.getInstance().getReference().child("Notice");
        deleteNoticerecyclerview.setLayoutManager(new LinearLayoutManager(this));
        deleteNoticerecyclerview.setHasFixedSize(true);
        getNotice();


    }

    private void getNotice() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list=new ArrayList<>();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    NoticeData data=dataSnapshot.getValue(NoticeData.class);
                    list.add(data);

                }

                adapter=new NoticeAdapter(DeleteNoticeActivity.this,list);
                adapter.notifyDataSetChanged();
                progressbar.setVisibility(View.GONE);

                deleteNoticerecyclerview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(DeleteNoticeActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}