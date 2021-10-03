package com.dania.one;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class PublishStoryProt extends AppCompatActivity {
    SimpleDraweeView iv;
    Uri dataUri;
    ImageView check, cancel;
    GifImageView dialog_earth_loading;
    Dialog dialog_loading;
    TextView dialog_title;
    private FirebaseFirestore db;
    private Button dialog_retake;
    private Button tagBuddiesBtn;
    private Button addCatBtn;
    private Button addLocationBtn;
    private StorageReference storageReference;
    RadioGroup visibleToRg;
    String my_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_story_prot);
        iv = (SimpleDraweeView) findViewById(R.id.iv);
        tagBuddiesBtn = findViewById(R.id.tagBuddiesBtn);
        addCatBtn = findViewById(R.id.addCatBtn);
        addLocationBtn = findViewById(R.id.addLocationBtn);
        visibleToRg = findViewById(R.id.visibeToRg);
        check = findViewById(R.id.check);
        cancel = findViewById(R.id.cancel);
        Intent intent = getIntent();
        String image_path= intent.getStringExtra("dataUri");
        dataUri = Uri.parse(image_path);
        iv.setImageURI(dataUri);
        dialog_loading = new Dialog(this);
        dialog_loading.setContentView(R.layout.ff_camera_loading_dialog);
        dialog_loading.setCanceledOnTouchOutside(false);
        dialog_earth_loading = dialog_loading.findViewById(R.id.earth_loading);
        dialog_title = dialog_loading.findViewById(R.id.dialog_title);
        dialog_retake = dialog_loading.findViewById(R.id.retake);
        my_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        tagBuddiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),TagBuddies.class);
                Pair<View, String> p1 = Pair.create((View)tagBuddiesBtn, "tag");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(PublishStoryProt.this, p1);
                startActivity(i, options.toBundle());
            }
        });
        addCatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddCategories.class);
                Pair<View, String> p1 = Pair.create((View)addCatBtn, "addCat");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(PublishStoryProt.this, p1);
                startActivity(i, options.toBundle());
            }
        });
        addLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddLocation.class);
                Pair<View, String> p1 = Pair.create((View)addLocationBtn, "addLoc");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(PublishStoryProt.this, p1);
                startActivity(i, options.toBundle());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void uploadImage() {
        dialog_loading.show();
        dialog_title.setText("Setting up your story.");
        dialog_earth_loading.setVisibility(View.VISIBLE);
        if (dataUri!=null){
            storageReference = FirebaseStorage.getInstance().getReference("Stories");
            int random = new Random().nextInt();
            final int timestamp = (int) new Timestamp(new Date()).getSeconds();
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
                        String date = new SimpleDateFormat("d MMM, yyyy").format(new Date());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("uri", mUri);
                        map.put("uid", my_id);
                        map.put("date", date);
                        map.put("type", "image");
                        map.put("views", 0);
                        map.put("reach",0);
                        map.put("story_id",file_name);
                        map.put("timestamp",timestamp);

                        HashMap<String, Object> mapCat = new HashMap<>();
                        mapCat.put("uri", mUri);
                        mapCat.put("uid", my_id);
                        mapCat.put("date", date);
                        mapCat.put("type", "image");
                        mapCat.put("story_id",file_name);
                        mapCat.put("timestamp",timestamp);
                        map.put("reach",0);
                        //reference.updateChildren(map);
                        db.collection("Stories").document(file_name).set(map);

                        //For Categories
                        if (AddStoryGlobal.selectedCategoriesGlobal.size()>0){
                            for (int i=0;i<AddStoryGlobal.selectedCategoriesGlobal.size();i++){
                                db.collection("Stories"+AddStoryGlobal.selectedCategoriesGlobal.get(i).getName().trim()).document(file_name).set(mapCat);
                            }
                            AddStoryGlobal.selectedCategoriesGlobal = new ArrayList<>();
                        }

                        final DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("Users").child(my_id);
                        user_ref.child("countInfo").child("stories_no").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                long stories = (long) snapshot.getValue();
                                long updated_stories = stories+1;
                                user_ref.child("countInfo").child("stories_no").setValue(updated_stories);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        user_ref.child("basicInfo").child("last_story").setValue(timestamp).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog_loading.dismiss();
                                Toast.makeText(PublishStoryProt.this, "Story added!!", Toast.LENGTH_SHORT).show();
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


}