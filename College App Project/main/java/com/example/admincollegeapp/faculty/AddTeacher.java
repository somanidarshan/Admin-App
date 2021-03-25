package com.example.admincollegeapp.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admincollegeapp.NoticeData;
import com.example.admincollegeapp.R;
import com.example.admincollegeapp.UploadNotice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTeacher extends AppCompatActivity {

    private ImageView addTeacherImage;
    private EditText addteachername,addteacheremail,addteacherpost;
    private Spinner addTeacherCategory;
    private Button addteacherbtn;
    private final int REQ = 1;
    private Bitmap bitmap=null;
    private String category;
    private String name,email,post,downloadUrl="";
    private ProgressDialog pd;
    private StorageReference storageReference;
    private DatabaseReference reference,dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);
        addTeacherImage=findViewById(R.id.addteacherimage);
        addteachername=findViewById(R.id.addteachername);
        addteacheremail=findViewById(R.id.addteacheremailid);
        addteacherpost=findViewById(R.id.addteacherPost);
        addTeacherCategory=findViewById(R.id.addteachercategory);
        addteacherbtn=findViewById(R.id.addteacherbtn);
        reference= FirebaseDatabase.getInstance().getReference().child("Teacher");
        storageReference= FirebaseStorage.getInstance().getReference();
        pd=new ProgressDialog(this);


        String[] items = new String[]{"Select Category", "Computer Science", "Information Technology", "Electronics and Telecommunications","Electronics"};
        addTeacherCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items));

        addTeacherCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=addTeacherCategory.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        addTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addteacherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chechVakidation();
            }
        });

    }

    private void chechVakidation() {
        name=addteachername.getText().toString();
        email=addteacheremail.getText().toString();
        post=addteacherpost.getText().toString();

        if(name.isEmpty()){
            addteachername.setError("Empty");
            addteachername.requestFocus();
        }
        else if(email.isEmpty()){
            addteacheremail.setError("Empty");
            addteacheremail.requestFocus();
        }else if(post.isEmpty()){
            addteacherpost.setError("Empty");
            addteacherpost.requestFocus();
        }else if(category.equals("Select Category")){
            Toast.makeText(this, "Please provide Teacher Category", Toast.LENGTH_SHORT).show();
        }else if(bitmap==null){
            uploaddata();
        }else{
            uploadimage();
        }





    }

        private void uploadimage() {
            pd.setTitle("Uploading");
            pd.show();


            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
            byte[] finalimage=baos.toByteArray();
            final StorageReference filepath;
            filepath=storageReference.child("Teacher").child(finalimage+"jpg");
            final UploadTask uploadTask=filepath.putBytes(finalimage);
            uploadTask.addOnCompleteListener(AddTeacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                        uploaddata();

                                    }
                                });
                            }
                        });
                    }else{
                        pd.dismiss();

                        Toast.makeText(AddTeacher.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }



    private void uploaddata() {
        dbRef=reference.child("Notice");

        final String uniquekey=dbRef.push().getKey();

        TeacherData teacherData=new TeacherData(name,email,post,downloadUrl,uniquekey);
        dbRef.child(uniquekey).setValue(teacherData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(AddTeacher.this, "Teacher Added Successfully", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddTeacher.this, "Process Not Successful", Toast.LENGTH_SHORT).show();
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
            addTeacherImage.setImageBitmap(bitmap);
        }
    }
}