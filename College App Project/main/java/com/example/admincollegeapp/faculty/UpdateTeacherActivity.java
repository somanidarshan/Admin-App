package com.example.admincollegeapp.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admincollegeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateTeacherActivity extends AppCompatActivity {

    private static final int REQ =1 ;
    private ImageView updateteacherimage;
    private TextView updateteachername;
    private TextView updateteacheremail;
    private TextView updateteacherpost;
    private Button updateteacherbtn,deleteteacherbtn;
    private String name,email,post,image;
    private Bitmap bitmap=null;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private ProgressDialog pd;
    private String downloadUrl;
    private String category,uniquekey;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);
        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        image=getIntent().getStringExtra("image");
        post=getIntent().getStringExtra("post");

        reference= FirebaseDatabase.getInstance().getReference().child("Teacher");
        storageReference= FirebaseStorage.getInstance().getReference();
        pd=new ProgressDialog(this);

        updateteacherimage=findViewById(R.id.updateteacherimage);
        updateteachername=findViewById(R.id.updateteachername);
        updateteacheremail=findViewById(R.id.updateteacheremail);
        updateteacherpost=findViewById(R.id.updateteacherpost);
        updateteacherbtn=findViewById(R.id.updateTeacherBtn);
        deleteteacherbtn=findViewById(R.id.deleteTeacherBtn);
         uniquekey=getIntent().getStringExtra("key");
         category=getIntent().getStringExtra("category");


        try {
            Picasso.get().load(image).into(updateteacherimage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateteacheremail.setText(email);
        updateteachername.setText(name);
        updateteacherpost.setText(post);

        updateteacherimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        updateteacherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=updateteachername.getText().toString();
                email=updateteacheremail.getText().toString();
                post=updateteacherpost.getText().toString();
                checkValidation();
            }
        });
        deleteteacherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });

    }

    private void checkValidation() {

        if(name.isEmpty()){
            updateteachername.setError("Empty");
            updateteachername.requestFocus();

        }else if(email.isEmpty()){
            updateteacheremail.setError("Empty");
            updateteacheremail.requestFocus();
        }else if(post.isEmpty()){
            updateteacherpost.setError("Empty");
            updateteacherpost.requestFocus();
        }else if(bitmap==null){
            updateData(image);

        }else{
            uploadImage();
        }

    }

    private void updateData(String s) {

        HashMap hp=new HashMap();
        hp.put("name",name);
        hp.put("email",email);
        hp.put("post",post);
        hp.put("image",s);


        reference.child(category).child(uniquekey).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateTeacherActivity.this, "Teacher Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(UpdateTeacherActivity.this,UpdateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateTeacherActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
        });





    }
    private void uploadImage() {
        pd.setTitle("Uploading");
        pd.show();


        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimage=baos.toByteArray();
        final StorageReference filepath;
        filepath=storageReference.child("Teacher").child(finalimage+"jpg");
        final UploadTask uploadTask=filepath.putBytes(finalimage);
        uploadTask.addOnCompleteListener(UpdateTeacherActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl=String.valueOf(uri);
                                    updateData(downloadUrl);

                                }
                            });
                        }
                    });
                }else{
//                    pd.dismiss();

                    Toast.makeText(UpdateTeacherActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    private void deleteData() {
        reference.child(category).child(uniquekey).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UpdateTeacherActivity.this, "Teacher Deleted Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(UpdateTeacherActivity.this,UpdateFaculty.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateTeacherActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void openGallery() {
        Intent pickimage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickimage, REQ);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateteacherimage.setImageBitmap(bitmap);
        }
    }
}