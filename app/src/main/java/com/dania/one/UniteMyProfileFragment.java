package com.dania.one;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.DatabaseSqlite.DatabaseMyData;
import com.dania.one.Model.Story;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class UniteMyProfileFragment extends Fragment {

    private CircleImageView pp;
    private TextView name, stories_no, bond_no, views_no, more;
    private String my_id, my_name, my_dp;
    private DatabaseReference bonds;
    private CollectionReference collectionReference;
    private CardView cv1, cv2, cv3, cv4;
    private SimpleDraweeView iv1, iv2, iv3, iv4;
    private int total_stories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unite_my_profile, container, false);
        pp = view.findViewById(R.id.pp);
        name = view.findViewById(R.id.name);
        stories_no = view.findViewById(R.id.stories_no);
        bond_no = view.findViewById(R.id.bond_no);
        views_no = view.findViewById(R.id.views_no);
        cv1 = view.findViewById(R.id.cv1);
        cv2 = view.findViewById(R.id.cv2);
        cv3 = view.findViewById(R.id.cv3);
        cv4 = view.findViewById(R.id.cv4);
        iv1 = (SimpleDraweeView) view.findViewById(R.id.iv1);
        iv2 = (SimpleDraweeView) view.findViewById(R.id.iv2);
        iv3 = (SimpleDraweeView) view.findViewById(R.id.iv3);
        iv4 = (SimpleDraweeView) view.findViewById(R.id.iv4);
        more= view.findViewById(R.id.more);
        initializing();
        return view;
    }

    private void initializing() {
        StoriesFetchedGlobal.allMyStoryModels = new ArrayList<>();
        getBasicData();
        collectionReference = FirebaseFirestore.getInstance().collection("Stories");
        //Getting All My Stories
        StoriesFetchedGlobal.allMyStoryModels = new ArrayList<>();
        Query query = collectionReference.whereEqualTo("uid", my_id).orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING);
        query.limit(20).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0) {
                    for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {
                        Story story = querySnapshot.toObject(Story.class);
                        StoriesFetchedGlobal.MyLastStory = querySnapshot;
                        StoriesFetchedGlobal.allMyStoryModels.add(story);
                    }
                    addToViews();
                } else {

                }
            }
        });


    }

    private void addToViews() {
        int size = StoriesFetchedGlobal.allMyStoryModels.size();
        if (size>5){
            if (StoriesFetchedGlobal.allMyStoryModels.get(0).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allMyStoryModels.get(0).getUri());
                iv1.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allMyStoryModels.get(0).getUri()).thumbnail(0.2f).into(iv1);
            }
            if (StoriesFetchedGlobal.allMyStoryModels.get(1).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allMyStoryModels.get(1).getUri());
                iv2.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allMyStoryModels.get(1).getUri()).thumbnail(0.2f).into(iv2);
            }
            if (StoriesFetchedGlobal.allMyStoryModels.get(2).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allMyStoryModels.get(2).getUri());
                iv3.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allMyStoryModels.get(2).getUri()).thumbnail(0.2f).into(iv3);
            }
            if (StoriesFetchedGlobal.allMyStoryModels.get(3).getType().equals("image")){
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(StoriesFetchedGlobal.allMyStoryModels.get(3).getUri()))
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
                Glide.with(this).load(StoriesFetchedGlobal.allMyStoryModels.get(3).getUri()).thumbnail(0.2f).into(iv4);
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
                    Intent i = new Intent(getContext(), UniteMyProfileAllStories.class);
                    startActivity(i);
                }
            });
        }else if (size==4){
            if (StoriesFetchedGlobal.allMyStoryModels.get(0).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allMyStoryModels.get(0).getUri());
                iv1.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allMyStoryModels.get(0).getUri()).thumbnail(0.2f).into(iv1);
            }
            if (StoriesFetchedGlobal.allMyStoryModels.get(1).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allMyStoryModels.get(1).getUri());
                iv2.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allMyStoryModels.get(1).getUri()).thumbnail(0.2f).into(iv2);
            }
            if (StoriesFetchedGlobal.allMyStoryModels.get(2).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allMyStoryModels.get(2).getUri());
                iv3.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allMyStoryModels.get(2).getUri()).thumbnail(0.2f).into(iv3);
            }
            if (StoriesFetchedGlobal.allMyStoryModels.get(3).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allMyStoryModels.get(3).getUri());
                iv4.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allMyStoryModels.get(3).getUri()).thumbnail(0.2f).into(iv4);
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
            if (StoriesFetchedGlobal.allMyStoryModels.get(0).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allMyStoryModels.get(0).getUri());
                iv1.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allMyStoryModels.get(0).getUri()).thumbnail(0.2f).into(iv1);
            }
            if (StoriesFetchedGlobal.allMyStoryModels.get(1).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allMyStoryModels.get(1).getUri());
                iv2.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allMyStoryModels.get(1).getUri()).thumbnail(0.2f).into(iv2);
            }
            if (StoriesFetchedGlobal.allMyStoryModels.get(2).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allMyStoryModels.get(2).getUri());
                iv3.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allMyStoryModels.get(2).getUri()).thumbnail(0.2f).into(iv3);
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
            if (StoriesFetchedGlobal.allMyStoryModels.get(0).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allMyStoryModels.get(0).getUri());
                iv1.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allMyStoryModels.get(0).getUri()).thumbnail(0.2f).into(iv1);
            }
            if (StoriesFetchedGlobal.allMyStoryModels.get(1).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allMyStoryModels.get(1).getUri());
                iv2.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allMyStoryModels.get(1).getUri()).thumbnail(0.2f).into(iv2);
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
            if (StoriesFetchedGlobal.allMyStoryModels.get(0).getType().equals("image")){
                Uri uri = Uri.parse(StoriesFetchedGlobal.allMyStoryModels.get(0).getUri());
                iv1.setImageURI(uri);
            }else {
                Glide.with(this).load(StoriesFetchedGlobal.allMyStoryModels.get(0).getUri()).thumbnail(0.2f).into(iv1);
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
        Intent i = new Intent(getContext(), ViewAllMyStories.class);
        i.putExtra("position",position);
        startActivity(i);
    }

    private void getBasicData() {
        if (Common.my_uid.equals(null)) {
            DatabaseMyData mydatadb = new DatabaseMyData(getContext());
            Cursor c = mydatadb.getData();
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    my_id = c.getString(0);
                    my_name = c.getString(1);
                    my_dp = c.getString(2);
                }
            }
        } else {
            my_id = Common.my_uid;
            my_name = Common.my_name;
            my_dp = Common.my_dp;
        }
        Glide.with(getContext()).load(my_dp).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(pp);
        name.setText(my_name);

        //Get NoOfStories and NoOfViews
        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("Users").child(my_id).child("countInfo");
        user_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long views = (long) dataSnapshot.child("views").getValue();
                long stories = (long) dataSnapshot.child("stories_no").getValue();
                total_stories = (int) stories;
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
