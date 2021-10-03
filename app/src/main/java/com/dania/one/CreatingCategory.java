package com.dania.one;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreatingCategory extends AppCompatActivity {

    private ImageView add;
    private SimpleDraweeView catImg;
    private EditText catName;
    private Uri dataUri;
    private Dialog dialog_loading;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private Bitmap bitmap;
    final int PICK_DATA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_category);
        add = findViewById(R.id.add);
        catImg = (SimpleDraweeView) findViewById(R.id.catImg);
        catName = findViewById(R.id.catName);
        db = FirebaseFirestore.getInstance();
        dialog_loading = new Dialog(this);
        dialog_loading.setContentView(R.layout.dialog_loading);
        dialog_loading.setCanceledOnTouchOutside(false);

        catImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGallaryIntent();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = catName.getText().toString().trim();
                if (dataUri!=null && !name.equals("")){
                    uploadCategory(name);
                }
            }
        });

    }

    private void uploadCategory(final String name) {
        dialog_loading.show();
        storageReference = FirebaseStorage.getInstance().getReference("Categories");
        int random = new Random().nextInt();
        final int timestamp = Math.round(new Date().getTime() / 1000);
        final String file_name = "CategoriesC"+name+timestamp+random;
        final StorageReference fileReference = storageReference.child(file_name
                + ".jpg");
        UploadTask uploadTask = fileReference.putFile(dataUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {

                    Uri downloadUri = (Uri) task.getResult();
                    assert downloadUri != null;
                    String mUri = downloadUri.toString();


                    HashMap<String, Object> map = new HashMap<>();
                    map.put("uri", mUri);
                    map.put("catName",name);
                    db.collection("Categories").document(file_name).set(map);
                    dialog_loading.dismiss();
                    catName.setText("");
                    Toast.makeText(CreatingCategory.this, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    dialog_loading.dismiss();
                    catName.setText("");
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog_loading.dismiss();
                catName.setText("");
                Toast.makeText(getApplicationContext(),"Error adding category : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getGallaryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_DATA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DATA && resultCode == RESULT_OK && data != null) {
            String path = data.getData().getPath();
            if (path.contains("/video")){
                Toast.makeText(this, "Videos can't be added to categories icon.", Toast.LENGTH_SHORT).show();
            } else if (path.contains("/image") || path.contains("/file")){
                dataUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dataUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catImg.setImageURI(dataUri);
            }else {
                finish();
            }
        }
        else {
            finish();
        }

    }
}