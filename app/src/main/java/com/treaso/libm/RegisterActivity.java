package com.treaso.libm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mvc.imagepicker.ImagePicker;

import java.io.File;

public class RegisterActivity extends AppCompatActivity {

    ImageView imageView;
    EditText editText;
    Button button,register;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    Uri file;
    Uri downloadUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        imageView = (ImageView)findViewById(R.id.imageView3);
        editText = (EditText)findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button3);
        register = (Button) findViewById(R.id.button4);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference riversRef = mStorageRef.child(editText.getText().toString()+"/logo/logo.jpg");

                riversRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                downloadUrl = taskSnapshot.getDownloadUrl();
                                mDatabase.child(editText.getText().toString()).child("logourl").setValue(downloadUrl);

                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickImage(v);

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        imageView.setImageBitmap(bitmap);
        file = data.getData();

        // TODO do something with the bitmap
    }

    public void onPickImage(View view) {
        // Click on image button
        ImagePicker.pickImage(this, "Select your image:");
    }
}
