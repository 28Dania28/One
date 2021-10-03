package com.dania.one;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.sax.EndElementListener;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.Adapters.unite_user_stories_datewise_adapter;
import com.dania.one.DatabaseSqlite.DatabaseMyData;
import com.dania.one.Model.DateWiseStoriesModel;
import com.dania.one.Model.FCMResponse;
import com.dania.one.Model.FCMSendData;
import com.dania.one.Model.Story;
import com.dania.one.Remote.IFCMService;
import com.dania.one.Remote.RetrofitFCMClient;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UniteUserProfileActivity extends AppCompatActivity {

    private CircleImageView pp, dialog_dp;
    private TextView name, stories_no, views_no, more, dialog_name;
    private String my_id, my_name, my_dp;
    private String user_uid, user_name, user_dp;
    private CardView cv1, cv2, cv3, cv4;
    private SimpleDraweeView iv1, iv2, iv3, iv4;
    private Button bond;
    private Button remove_buddy_btn;
    private ImageView bonded;
    private CircleImageView msg_btn;
    private Dialog dialog;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private int total_stories;
    private IFCMService ifcmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unite_user_profile);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b!=null){
            user_uid = b.getString("user_uid");
            StoriesFetchedGlobal.user_uid = user_uid;
        }
        initializing();
    }

    private void initializing() {
        pp = findViewById(R.id.pp);
        name = findViewById(R.id.name);
        stories_no = findViewById(R.id.stories_no);
        views_no = findViewById(R.id.views_no);
        cv1 = findViewById(R.id.cv1);
        cv2 = findViewById(R.id.cv2);
        cv3 = findViewById(R.id.cv3);
        cv4 = findViewById(R.id.cv4);
        iv1 = (SimpleDraweeView) findViewById(R.id.iv1);
        iv2 = (SimpleDraweeView) findViewById(R.id.iv2);
        iv3 = (SimpleDraweeView) findViewById(R.id.iv3);
        iv4 = (SimpleDraweeView) findViewById(R.id.iv4);
        more= findViewById(R.id.more);
        bond = findViewById(R.id.bond);
        bonded = findViewById(R.id.bonded);
        msg_btn = findViewById(R.id.msg_btn);
        dialog = new Dialog(UniteUserProfileActivity.this);
        dialog.setContentView(R.layout.remove_buddy_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_dp = dialog.findViewById(R.id.dialog_dp);
        dialog_name = dialog.findViewById(R.id.dialog_name);
        remove_buddy_btn = dialog.findViewById(R.id.remove_buddy_btn);
        ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Stories");
        StoriesFetchedGlobal.allUserStoryModels = new ArrayList<>();
        getBasicData();
        getStoryData();
        msg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getApplicationContext(),UniteChattingActivity.class);
                i1.putExtra("user_uid",user_uid);
                i1.putExtra("user_name",user_name);
                i1.putExtra("user_dp",user_dp);
                startActivity(i1);
//                Pair[] pairs = new Pair[2];
//                pairs[0] = new Pair<View, String>(pp, "user_pp");
//                pairs[1] = new Pair<View, String>(name,"user_name");
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UniteUserProfileActivity.this, pairs);
//                startActivity(i1, options.toBundle());

            }
        });

        bonded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getApplicationContext()).load(user_dp).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(dialog_dp);
                dialog_name.setText(user_name);
                dialog.show();
            }
        });

        remove_buddy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference d1 = FirebaseDatabase.getInstance().getReference(user_uid+"Bonds").child(my_id);
                d1.removeValue();
                DatabaseReference d2 = FirebaseDatabase.getInstance().getReference(my_id+"Bonds").child(user_uid);
                d2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        Toast.makeText(UniteUserProfileActivity.this, "Buddy Removed", Toast.LENGTH_SHORT).show();
                        checkBonds();
                    }
                });
                if (Common.FFuser_uid.contains(user_uid)){
                    addStatusToCommon("not_bonded");
                }

            }
        });

        bond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference sendRequest = FirebaseDatabase.getInstance().getReference(user_uid+"Bonds").child(my_id);
                sendRequest.setValue(my_id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
                DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("Users").child(user_uid).child("countInfo").child("buddies_modify");
                user_ref.setValue("1");
                DatabaseReference requested = FirebaseDatabase.getInstance().getReference(my_id+"Bonds").child(user_uid);
                requested.setValue(user_uid).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UniteUserProfileActivity.this, "Buddy added", Toast.LENGTH_SHORT).show();
                        checkBonds();
                        Map<String,String> notiData = new HashMap<>();
                        notiData.put("title","DnS");
                        notiData.put("content","You got a new bond with "+my_name);
                        notiData.put("intent","bond");
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


                    }
                });
                FirebaseMessaging.getInstance().subscribeToTopic("Stories"+user_uid).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
                if (Common.FFuser_uid.contains(user_uid)){
                    addStatusToCommon("bonded");
                }

            }
        });

    }

    private void addStatusToCommon(String status) {
        for (int i=0;i<Common.FFuser_model.size();i++){
            if (Common.FFuser_model.get(i).getId().equals(user_uid)){
                Common.FFuser_model.get(i).setStatus(status);
            }
        }
    }
    private void getBasicData() {
        if (Common.my_uid == null){
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user_uid).child("basicInfo");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name_user = dataSnapshot.child("name").getValue().toString();
                user_name = name_user;
                String dp  = dataSnapshot.child("display_picture").getValue().toString();
                user_dp = dp;
                StoriesFetchedGlobal.user_dp = user_dp;
                Glide.with(getApplicationContext()).load(dp).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(pp);
                name.setText(name_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (!user_uid.equals(my_id)){
            checkBonds();
        }

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(user_uid).child("countInfo").child("views");
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long views = (long) snapshot.getValue();
                views_no.setText(String.valueOf(views));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query query = collectionReference.whereEqualTo("uid",user_uid).orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size()>0){
                    total_stories = queryDocumentSnapshots.size();
                    stories_no.setText(String.valueOf(total_stories));
                    for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots){

                    }
                }
            }
        });
    }

    private void checkBonds() {
        DatabaseReference check_bonds = FirebaseDatabase.getInstance().getReference(my_id+"Bonds");
        check_bonds.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.hasChild(user_uid)){
                        bond.setVisibility(View.GONE);
                        bonded.setVisibility(View.VISIBLE);
                        msg_btn.setVisibility(View.VISIBLE);

                    }else {
                        bond.setVisibility(View.VISIBLE);
                        bonded.setVisibility(View.GONE);
                        msg_btn.setVisibility(View.GONE);
                    }
                }else {
                    bond.setVisibility(View.VISIBLE);
                    bonded.setVisibility(View.GONE);
                    msg_btn.setVisibility(View.GONE);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getStoryData() {
        Query query = collectionReference.whereEqualTo("uid", user_uid).orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING);
        query.limit(20).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0) {
                    for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {
                        Story story = querySnapshot.toObject(Story.class);
                        StoriesFetchedGlobal.UserLastStory= querySnapshot;
                        StoriesFetchedGlobal.allUserStoryModels.add(story);
                    }
                    addToViews();
                } else {

                }
            }
        });
    }

    private void addToViews() {
        int size = StoriesFetchedGlobal.allUserStoryModels.size();
        if (size>5){
            if (StoriesFetchedGlobal.allUserStoryModels.get(0).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allUserStoryModels.get(0).getUri());
                iv1.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allUserStoryModels.get(0).getUri()).thumbnail(0.2f).into(iv1);
            }
            if (StoriesFetchedGlobal.allUserStoryModels.get(1).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allUserStoryModels.get(1).getUri());
                iv2.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allUserStoryModels.get(1).getUri()).thumbnail(0.2f).into(iv2);
            }
            if (StoriesFetchedGlobal.allUserStoryModels.get(2).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allUserStoryModels.get(2).getUri());
                iv3.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allUserStoryModels.get(2).getUri()).thumbnail(0.2f).into(iv3);
            }
            if (StoriesFetchedGlobal.allUserStoryModels.get(3).getType().equals("image")){
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(StoriesFetchedGlobal.allUserStoryModels.get(3).getUri()))
                        .setPostprocessor(new IterativeBoxBlurPostProcessor(20))
                        .build();
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(iv4.getController())
                        .build();
                iv4.setController(controller);
//                Uri uri = Uri.parse(StoriesFetchedGlobal.allMyStoryModels.get(3).getUri());
//                iv4.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allUserStoryModels.get(3).getUri()).thumbnail(0.2f).into(iv4);
            }
            int extra = total_stories - 4;
            more.setText("+ "+extra);
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStory(0);
                }
            });
            iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStory(1);
                }
            });
            iv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStory(2);
                }
            });
            iv4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), UniteUserProfileAllStories.class);
                    startActivity(i);
                }
            });
        }else if (size==4){
            if (StoriesFetchedGlobal.allUserStoryModels.get(0).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allUserStoryModels.get(0).getUri());
                iv1.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allUserStoryModels.get(0).getUri()).thumbnail(0.2f).into(iv1);
            }
            if (StoriesFetchedGlobal.allUserStoryModels.get(1).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allUserStoryModels.get(1).getUri());
                iv2.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allUserStoryModels.get(1).getUri()).thumbnail(0.2f).into(iv2);
            }
            if (StoriesFetchedGlobal.allUserStoryModels.get(2).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allUserStoryModels.get(2).getUri());
                iv3.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allUserStoryModels.get(2).getUri()).thumbnail(0.2f).into(iv3);
            }
            if (StoriesFetchedGlobal.allUserStoryModels.get(3).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allUserStoryModels.get(3).getUri());
                iv4.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allUserStoryModels.get(3).getUri()).thumbnail(0.2f).into(iv4);
            }
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStory(0);
                }
            });
            iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStory(1);
                }
            });
            iv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStory(2);
                }
            });
            iv4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStory(3);
                }
            });
        }else if (size==3){
            if (StoriesFetchedGlobal.allUserStoryModels.get(0).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allUserStoryModels.get(0).getUri());
                iv1.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allUserStoryModels.get(0).getUri()).thumbnail(0.2f).into(iv1);
            }
            if (StoriesFetchedGlobal.allUserStoryModels.get(1).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allUserStoryModels.get(1).getUri());
                iv2.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allUserStoryModels.get(1).getUri()).thumbnail(0.2f).into(iv2);
            }
            if (StoriesFetchedGlobal.allUserStoryModels.get(2).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allUserStoryModels.get(2).getUri());
                iv3.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allUserStoryModels.get(2).getUri()).thumbnail(0.2f).into(iv3);
            }
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStory(0);
                }
            });
            iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStory(1);
                }
            });
            iv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStory(2);
                }
            });
        }else if (size==2){
            if (StoriesFetchedGlobal.allUserStoryModels.get(0).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allUserStoryModels.get(0).getUri());
                iv1.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allUserStoryModels.get(0).getUri()).thumbnail(0.2f).into(iv1);
            }
            if (StoriesFetchedGlobal.allUserStoryModels.get(1).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allUserStoryModels.get(1).getUri());
                iv2.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allUserStoryModels.get(1).getUri()).thumbnail(0.2f).into(iv2);
            }
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStory(0);
                }
            });
            iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStory(1);
                }
            });
        }else if (size==1){
            if (StoriesFetchedGlobal.allUserStoryModels.get(0).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allUserStoryModels.get(0).getUri());
                iv1.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allUserStoryModels.get(0).getUri()).thumbnail(0.2f).into(iv1);
            }
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStory(0);
                }
            });
        }else {

        }
    }

    private void showStory(int position) {
        Intent i = new Intent(getApplicationContext(), ViewUserProfileStory.class);
        i.putExtra("position",position);
        startActivity(i);
    }
}
