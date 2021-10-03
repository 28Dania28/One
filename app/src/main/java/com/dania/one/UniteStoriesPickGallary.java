package com.dania.one;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FieldValue;
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

import pl.droidsonroids.gif.GifImageView;

public class UniteStoriesPickGallary extends AppCompatActivity {

    private VideoView vv;
    private SimpleDraweeView iv;
    private ImageView next, cancel,crop;
    final int PICK_DATA = 1;
    Uri dataUri;
    GifImageView dialog_earth_loading;
    Dialog dialog_loading;
    TextView dialog_title;
    private FirebaseFirestore db;
    Button dialog_retake;
    private StorageReference storageReference;
    Bitmap bitmap;
    String my_id;
    int data_code = 0;
    private int stopPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unite_stories_pick_gallary);
        vv = findViewById(R.id.vv);
        iv = (SimpleDraweeView) findViewById(R.id.iv);
        next = findViewById(R.id.next);
        cancel = findViewById(R.id.cancel);
        crop = findViewById(R.id.crop);
        dialog_loading = new Dialog(this);
        dialog_loading.setContentView(R.layout.ff_camera_loading_dialog);
        dialog_loading.setCanceledOnTouchOutside(false);
        dialog_earth_loading = dialog_loading.findViewById(R.id.earth_loading);
        dialog_title = dialog_loading.findViewById(R.id.dialog_title);
        dialog_retake = dialog_loading.findViewById(R.id.retake);
        my_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        getGallaryIntent();

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCrop();
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data_code==1){
                    /*Common.capture_image_uri = dataUri;
                    Intent i = new Intent(getApplicationContext(),ImageEditing.class);
                    startActivity(i);
                    finish();
                   */
                    Intent i = new Intent(getApplicationContext(),PublishStoryProt.class);
                    i.putExtra("dataUri", dataUri.toString());
                    Pair<View, String> p1 = Pair.create((View)iv, "iv_tranisition");
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(UniteStoriesPickGallary.this, p1);
                    startActivity(i, options.toBundle());
                    //uploadImage();
                }else if (data_code==28){
                    uploadVideo();
                }else {

                }

            }


        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        vv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPause();
            }
        });



    }

    private void getCrop() {
        CropImage.activity(dataUri)
                .start(UniteStoriesPickGallary.this);
    }

    private void uploadImage() {
        dialog_loading.show();
        dialog_title.setText("Setting up your story.");
        dialog_earth_loading.setVisibility(View.VISIBLE);
        if (dataUri!=null){
            storageReference = FirebaseStorage.getInstance().getReference("Stories");
            int random = new Random().nextInt();
            final int timestamp = Math.round(new Date().getTime() / 1000);
            final String file_name = my_id+"Story"+timestamp+random;
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


                        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Stories").child(my_id).child(file_name);
                        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("Users").child(my_id).child("basicInfo");
                        String date = new SimpleDateFormat("d MMM, yyyy").format(new Date());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("uri", mUri);
                        map.put("uid", my_id);
                        map.put("date", date);
                        map.put("type", "image");
                        map.put("views", 0);
                        map.put("story_id",file_name);
                        map.put("timestamp",timestamp);
                        //reference.updateChildren(map);
                        db.collection("Stories").document(file_name).set(map);
                        user_ref.child("last_story").setValue(timestamp).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog_loading.dismiss();
                                Toast.makeText(UniteStoriesPickGallary.this, "Story added!!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog_earth_loading.setVisibility(View.GONE);
                    dialog_title.setText("Failed to SetUp! Retry Again!");
                    dialog_retake.setVisibility(View.VISIBLE);

                }
            });


        }else {
            dialog_earth_loading.setVisibility(View.GONE);
            dialog_title.setText("Failed to SetUp! Retry Again!");
            dialog_retake.setVisibility(View.VISIBLE);
        }

    }

    private void uploadVideo() {
        dialog_loading.show();
        dialog_title.setText("Setting up your story.");
        dialog_earth_loading.setVisibility(View.VISIBLE);
        if (dataUri!=null){
            storageReference = FirebaseStorage.getInstance().getReference("Stories");
            int random = new Random().nextInt();
            final int timestamp = Math.round(new Date().getTime() / 1000);
            final String file_name = my_id+"Story"+timestamp+random;
            final StorageReference fileReference = storageReference.child(file_name
                    + ".mp4");
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
                        String type = null;
                        if (data_code == 1){
                            type = "image";
                        }else {
                            type = "video";
                        }

                        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Stories").child(my_id).child(file_name);
                        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("Users").child(my_id).child("basicInfo");
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("uri", mUri);
                        map.put("uid", my_id);
                        map.put("date", date);
                        map.put("type", type);
                        map.put("views", 0);
                        map.put("story_id",file_name);
                        map.put("timestamp",timestamp);
                        //reference.updateChildren(map);
                        db.collection("Stories").document(file_name).set(map);
                        dialog_loading.dismiss();
                        Toast.makeText(UniteStoriesPickGallary.this, "Story added!!", Toast.LENGTH_SHORT).show();
                        finish();
                        user_ref.child("stories_no").setValue(FieldValue.increment(1));
                        user_ref.child("last_story").setValue(timestamp).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog_loading.dismiss();
                                Toast.makeText(UniteStoriesPickGallary.this, "Story added!!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),Unite2.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog_earth_loading.setVisibility(View.GONE);
                    dialog_title.setText("Failed to SetUp! Retry Again!");
                    dialog_retake.setVisibility(View.VISIBLE);

                }
            });


        }else {
            dialog_earth_loading.setVisibility(View.GONE);
            dialog_title.setText("Failed to SetUp! Retry Again!");
            dialog_retake.setVisibility(View.VISIBLE);
        }

    }

    private void getGallaryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/* video/*");
        String[] mimetypes = {"image/*","video/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimetypes);
        startActivityForResult(intent,PICK_DATA);
    }

    private void playPause() {
        if (vv.isPlaying()){
            stopPosition = vv.getCurrentPosition();
            vv.pause();
        }else {
            vv.seekTo(stopPosition);
            vv.start();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DATA && resultCode == RESULT_OK && data != null) {
            String path = data.getData().getPath();
            if (path.contains("/video")){
                data_code = 28;
                dataUri = data.getData();
                vv.setVisibility(View.VISIBLE);
                iv.setVisibility(View.GONE);
                crop.setVisibility(View.GONE);
                vv.setVideoURI(dataUri);
                vv.start();
                vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setLooping(true);
                    }
                });
            } else if (path.contains("/image") || path.contains("/file")){
                data_code = 1;
                dataUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dataUri);
                    Common.newBitmap = bitmap.copy(bitmap.getConfig(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                iv.setVisibility(View.VISIBLE);
                iv.setImageURI(dataUri);
                crop.setVisibility(View.VISIBLE);
                vv.setVisibility(View.GONE);

            }else {
                finish();
            }
        }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                dataUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dataUri);
                    Common.newBitmap = bitmap.copy(bitmap.getConfig(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                iv.setImageURI(dataUri);
            }

        }else {
            finish();
        }


    }
}
