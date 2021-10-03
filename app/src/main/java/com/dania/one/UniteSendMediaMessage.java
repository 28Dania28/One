package com.dania.one;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.DatabaseSqlite.DatabaseHelperChats;
import com.dania.one.DatabaseSqlite.DatabaseMyData;
import com.dania.one.Model.FCMResponse;
import com.dania.one.Model.FCMSendData;
import com.dania.one.Remote.IFCMService;
import com.dania.one.Remote.RetrofitFCMClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UniteSendMediaMessage extends AppCompatActivity {

    ImageView iv, back, crop, emoji_iv, send_iv;
    String user_uid, user_name, user_dp, my_id, my_name, my_dp;
    final int PICK_DATA = 1;
    Uri dataUri;
    private StorageReference storageReference;
    FirebaseFirestore db;
    CircleImageView pp;
    EmojiEditText msg;
    ViewGroup rootView;
    EmojiPopup emojiPopup;
    Boolean emojiIcon = true;
    Dialog dialog_loading;
    VideoView vv;
    int data_code = 0;
    private int stopPosition;
    IFCMService ifcmService;
    DatabaseHelperChats mDatabaseHelperChats;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unite_send_media_message);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b!=null){
            user_uid = b.getString("user_uid");
            user_name = b.getString("user_name");
            user_dp = b.getString("user_dp");
        }
        vv = findViewById(R.id.vv);
        iv = findViewById(R.id.iv);
        emoji_iv = findViewById(R.id.emoji_iv);
        rootView = findViewById(R.id.rootView);
        send_iv = findViewById(R.id.send_iv);
        pp = findViewById(R.id.pp);
        dialog_loading = new Dialog(this);
        dialog_loading.setContentView(R.layout.dialog_loading);
        dialog_loading.setCanceledOnTouchOutside(false);
        dialog_loading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_loading.getWindow().getAttributes().windowAnimations = R.style.DialogSlide;
        msg = findViewById(R.id.msg);
        back = findViewById(R.id.back);
        crop = findViewById(R.id.crop);
        db = FirebaseFirestore.getInstance();
        ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);
        mDatabaseHelperChats = new DatabaseHelperChats(this);
        setUpEmojiPopUp();
        getGallaryIntent();
        if (user_dp!=null){
            Glide.with(getApplicationContext()).load(user_dp).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(pp);
        }else {
            DatabaseReference dp_ref = FirebaseDatabase.getInstance().getReference("Users").child(user_uid).child("basicInfo").child("display_picture");
            dp_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String dp = dataSnapshot.getValue().toString();
                    user_dp = dp;
                    Glide.with(getApplicationContext()).load(user_dp).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(pp);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        if (Common.my_uid.equals(null)){
            DatabaseMyData mydatadb = new DatabaseMyData(getApplicationContext());
            Cursor c = mydatadb.getData();
            if (c.getCount() > 0) {
                while (c.moveToNext()){
                    my_id = c.getString(0);
                    my_name = c.getString(1);
                    my_dp = c.getString(2);
                }
            }
        }else {
            my_id = Common.my_uid;
            my_name = Common.my_name;
            my_dp = Common.my_dp;
        }

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCrop();
            }
        });

        vv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPause();
            }
        });

        send_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data_code==1){
                    sendImageMsg();
                }else if (data_code==28){
                    sendVideoMsg();
                }else {

                }

            }
        });

    }

    private void setUpEmojiPopUp() {
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                .build(msg);
        emoji_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emojiIcon){
                    emojiPopup.toggle();
                    emoji_iv.setImageResource(R.drawable.keyboard_logo);
                    emojiIcon = false;
                }else {
                    emojiPopup.toggle();
                    emoji_iv.setImageResource(R.drawable.emoji_ios_category_smileysandpeople);
                    emojiIcon = true;
                }
            }
        });
    }


    private void getCrop() {
        CropImage.activity(dataUri)
                .start(UniteSendMediaMessage.this);
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



    private void getGallaryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("*/*");
        String[] mimetypes = {"image/*","video/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimetypes);
        startActivityForResult(intent,PICK_DATA);
    }


    private void sendImageMsg() {
        dialog_loading.show();
        //dialog_title.setText("Setting up your story.");
        //dialog_earth_loading.setVisibility(View.VISIBLE);
        if (dataUri!=null){
            send_iv.setEnabled(false);
            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("h:mm a");
            final String time = currentTime.format(calForTime.getTime());
            final String date = new SimpleDateFormat("d MMM, yyyy").format(new Date());
            storageReference = FirebaseStorage.getInstance().getReference("Cloud");
            int random = new Random().nextInt();
            int timestamp = Math.round(new Date().getTime() / 1000);
            String file_name = my_id+"Image"+timestamp+random;
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
                        Uri downloadUrl = (Uri) task.getResult();
                        assert downloadUrl!= null;
                        String mUri = downloadUrl.toString();
                        String key = db.collection("Chats").document().getId();
                        int timestamp = Math.round(new Date().getTime()/1000);
                        String msg_txt = msg.getText().toString().trim();
                        Map<String, Object> map = new HashMap<>();
                        map.put("index", FieldValue.serverTimestamp());
                        map.put("timestamp", timestamp);
                        map.put("time", time);
                        map.put("date", date);
                        map.put("msg", msg_txt);
                        map.put("type", "image_msg");
                        map.put("sender", my_id);
                        map.put("msg_id",key);
                        map.put("uri",mUri);
                        boolean insertData = mDatabaseHelperChats.addData("Chats"+my_id+user_uid,msg_txt,time,date,my_id,key,timestamp,"image_msg","right",mUri,"");
                        if (insertData) {
                            db.collection(user_uid+my_id+"Chats").document(key).set(map);
                            Map<String,String> notiData = new HashMap<>();
                            notiData.put("title","DnS");
                            notiData.put("intent","image_msg");
                            notiData.put("name",my_name);
                            notiData.put("i1","Chats"+my_id+user_uid);
                            notiData.put("i2",msg_txt);
                            notiData.put("i3",time);
                            notiData.put("i4",date);
                            notiData.put("i5",my_id);
                            notiData.put("i6",key);
                            notiData.put("i7",String.valueOf(timestamp));
                            notiData.put("i8","image_msg");
                            notiData.put("i9","left");
                            notiData.put("i10",mUri);
                            notiData.put("image",my_dp);
                            FCMSendData sendData = new FCMSendData("/topics/"+user_uid,notiData);
                            ifcmService.sendNotification(sendData)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<FCMResponse>() {
                                        @Override
                                        public void accept(FCMResponse fcmResponse) throws Exception {

                                        }
                                    });


                            dialog_loading.dismiss();
                            finish();
                        }else {

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog_loading.dismiss();
                    //dialog_earth_loading.setVisibility(View.GONE);
                    //dialog_title.setText("Failed to SetUp! Retry Again!");
                    //dialog_retake.setVisibility(View.VISIBLE);

                }
            });


        }else {
            dialog_loading.dismiss();
            Toast.makeText(getApplicationContext(),"Failed to send image.", Toast.LENGTH_SHORT).show();
            //dialog_earth_loading.setVisibility(View.GONE);
            //dialog_title.setText("Failed to SetUp! Retry Again!");
            //dialog_retake.setVisibility(View.VISIBLE);
        }

    }


    private void sendVideoMsg() {
        dialog_loading.show();
        //dialog_title.setText("Setting up your story.");
        //dialog_earth_loading.setVisibility(View.VISIBLE);
        if (dataUri!=null){
            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("h:mm a");
            final String time = currentTime.format(calForTime.getTime());
            final String date = new SimpleDateFormat("d MMM, yyyy").format(new Date());
            storageReference = FirebaseStorage.getInstance().getReference("Cloud");
            int random = new Random().nextInt();
            int timestamp = Math.round(new Date().getTime() / 1000);
            String file_name = my_id+"Video"+timestamp+random;
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
                        Uri downloadUrl = (Uri) task.getResult();
                        assert downloadUrl!= null;
                        String mUri = downloadUrl.toString();
                        String key = db.collection("Chats").document().getId();
                        int timestamp = Math.round(new Date().getTime()/1000);
                        String msg_txt = msg.getText().toString().trim();
                        Map<String, Object> map = new HashMap<>();
                        map.put("index", FieldValue.serverTimestamp());
                        map.put("timestamp", timestamp);
                        map.put("time", time);
                        map.put("date", date);
                        map.put("msg", msg_txt);
                        map.put("type", "video_msg");
                        map.put("sender", my_id);
                        map.put("msg_id",key);
                        map.put("uri",mUri);
                        boolean insertData = mDatabaseHelperChats.addData("Chats"+my_id+user_uid,msg_txt,time,date,my_id,key,timestamp,"video_msg","right",mUri,"");
                        if (insertData) {
                            db.collection(user_uid+my_id+"Chats").document(key).set(map);
                            Map<String,String> notiData = new HashMap<>();
                            notiData.put("title","DnS");
                            notiData.put("intent","video_msg");
                            notiData.put("name",my_name);
                            notiData.put("i1","Chats"+my_id+user_uid);
                            notiData.put("i2",msg_txt);
                            notiData.put("i3",time);
                            notiData.put("i4",date);
                            notiData.put("i5",my_id);
                            notiData.put("i6",key);
                            notiData.put("i7",String.valueOf(timestamp));
                            notiData.put("i8","video_msg");
                            notiData.put("i9","left");
                            notiData.put("i10",mUri);
                            notiData.put("image",my_dp);
                            FCMSendData sendData = new FCMSendData("/topics/"+user_uid,notiData);
                            ifcmService.sendNotification(sendData)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<FCMResponse>() {
                                        @Override
                                        public void accept(FCMResponse fcmResponse) throws Exception {

                                        }
                                    });


                            dialog_loading.dismiss();
                            finish();
                        }else {

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog_loading.dismiss();
                    //dialog_earth_loading.setVisibility(View.GONE);
                    //dialog_title.setText("Failed to SetUp! Retry Again!");
                    //dialog_retake.setVisibility(View.VISIBLE);

                }
            });


        }else {
            dialog_loading.dismiss();
            Toast.makeText(getApplicationContext(),"Failed to send image.", Toast.LENGTH_SHORT).show();
            //dialog_earth_loading.setVisibility(View.GONE);
            //dialog_title.setText("Failed to SetUp! Retry Again!");
            //dialog_retake.setVisibility(View.VISIBLE);
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
                iv.setImageURI(dataUri);
            }

        }else {
            finish();
        }


    }


}
