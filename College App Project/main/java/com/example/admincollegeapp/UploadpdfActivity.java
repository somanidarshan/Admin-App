package com.example.admincollegeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class  UploadpdfActivity extends AppCompatActivity {
    private ProgressDialog pd;

    CardView addpdf;
    private final int REQ=1;

    private Uri pdfData;
    EditText pdftitle;
    Button uploadpdfbtn;
    private String title;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    String downloadurl="";
    private TextView pdftextView;
    String pdfName;
    private ProgressDialog pddd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadpdf);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        pd= new ProgressDialog(this);


        addpdf=findViewById(R.id.addpdf);
        pdftitle=findViewById(R.id.pdfTitle);
        uploadpdfbtn=findViewById(R.id.uploadpdfBtn);
        pdftextView=findViewById(R.id.pdftextview);

        addpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        uploadpdfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=pdftitle.getText().toString();
                if(title.isEmpty()){
                    pdftitle.setError("Empty");
                    pdftitle.requestFocus();
                }else if(pdfData==null){
                    Toast.makeText(UploadpdfActivity.this, "Please Upload Pdf", Toast.LENGTH_SHORT).show();
                }else{
                    uploadpdf();
                }
            }
        });
    }

    private void uploadpdf() {
        pddd.setTitle("Please Wait....");
        pddd.setMessage("Upladeding pdf");
        pddd.show();


        StorageReference reference=storageReference.child("pdf/"+pdfName+"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uritask=taskSnapshot.getStorage().getDownloadUrl();
                while(!uritask.isComplete()){
                    Uri uri=uritask.getResult();
                    uploadData((String.valueOf(uri)));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadpdfActivity.this, "Something Went Wromg", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void uploadData(String valueOf) {
        String uniquekey=databaseReference.child("pdf").push().getKey();
        HashMap data=new HashMap();
        data.put("pdfTitle",title);
        data.put("pdfUrl",downloadurl);
        databaseReference.child("pdf").child(uniquekey).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pddd.dismiss();
                Toast.makeText(UploadpdfActivity.this, "PDF Uploaded Successfuly", Toast.LENGTH_SHORT).show();
                pdftitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadpdfActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                pddd.dismiss();
            }
        });
    }


    private void openGallery(){
       Intent intent=new Intent();
       intent.setType("pdf/docs/ppt");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(Intent.createChooser(intent,"Select Pdf File"),REQ);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode==RESULT_OK){
            pdfData=data.getData();

           if(pdfData.toString().startsWith("content://")){
               Cursor cursor=null;
               try {
                   cursor=UploadpdfActivity.this.getContentResolver().query(pdfData,null,null,null);

                   if(cursor!=null && cursor.moveToFirst()){
                       pdfName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }

           }else if(pdfData.toString().startsWith("file://")){
               pdfName=new File(pdfData.toString()).getName();
           }
           pdftextView.setText(pdfName);

        }
    }

}