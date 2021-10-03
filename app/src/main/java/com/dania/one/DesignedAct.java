package com.dania.one;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.DatabaseSqlite.DatabaseMyData;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DesignedAct extends AppCompatActivity {


    private TextView name, stories_no, bond_no, views_no, views;
    private SimpleDraweeView dp;
    private String my_id, my_name, my_dp;
    private DatabaseReference bonds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designed2);
        name = findViewById(R.id.name);
        stories_no = findViewById(R.id.stories_no);
        bond_no = findViewById(R.id.bond_no);
        views_no = findViewById(R.id.views_no);
        dp = (SimpleDraweeView) findViewById(R.id.dp);
        getBasicData();

    }
    private void getBasicData() {
        if (Common.my_uid.equals(null)){
            DatabaseMyData mydatadb = new DatabaseMyData(this);
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
        Glide.with(this).load(my_dp).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(dp);
        name.setText(my_name);

        //Get NoOfStories and NoOfViews
        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("Users").child(my_id).child("countInfo");
        user_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long views = (long) dataSnapshot.child("views").getValue();
                long stories= (long) dataSnapshot.child("stories_no").getValue();
                ValueAnimator animator = ValueAnimator.ofInt(0, (int) stories);
                animator.setDuration(1500);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        stories_no.setText(animation.getAnimatedValue().toString());
                    }
                });
                ValueAnimator animator2 = ValueAnimator.ofInt(0, (int) views);
                animator2.setDuration(1500);
                animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        views_no.setText(animation.getAnimatedValue().toString());
                    }
                });
                animator.start();
                animator2.start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //GetBondsNo
        bonds = FirebaseDatabase.getInstance().getReference(my_id + "Bonds");
        updateBondsNo();
        bonds.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateBondsNo();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                updateBondsNo();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void updateBondsNo() {
        bonds.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int total_bonds = (int) dataSnapshot.getChildrenCount();
                    //bond_no.setText(String.valueOf(total_bonds));
                    ValueAnimator animator = ValueAnimator.ofInt(0, total_bonds);
                    animator.setDuration(500);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            bond_no.setText(animation.getAnimatedValue().toString());
                        }
                    });
                    animator.start();
                } else {
                    bond_no.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}