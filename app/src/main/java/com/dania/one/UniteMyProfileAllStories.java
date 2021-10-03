package com.dania.one;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.Adapters.AllMyStoriesAdapter;
import com.dania.one.Adapters.ViewUserStoriesAdapter;
import com.dania.one.Model.Story;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class UniteMyProfileAllStories extends AppCompatActivity {

    private ImageView back;
    private CircleImageView pp;
    private RecyclerView rv;
    private AllMyStoriesAdapter allMyStoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unite_my_profile_all_stories);
        pp = findViewById(R.id.pp);
        back = findViewById(R.id.back);
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new GridLayoutManager(this,3));
        Glide.with(getApplicationContext()).load(Common.my_dp).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(pp);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        allMyStoriesAdapter = new AllMyStoriesAdapter(UniteMyProfileAllStories.this);
        rv.setAdapter(allMyStoriesAdapter);
        getMoreStories();
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    getMoreStories();
                }
            }

        });

    }

    private void getMoreStories() {
        if (StoriesFetchedGlobal.MyLastStory!=null){
            CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Stories");
            Query query = collectionReference.whereEqualTo("uid", Common.my_uid).orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING);
            query.startAfter(StoriesFetchedGlobal.MyLastStory).limit(20).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.size() > 0) {
                        for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {
                            Story story = querySnapshot.toObject(Story.class);
                            StoriesFetchedGlobal.MyLastStory = querySnapshot;
                            StoriesFetchedGlobal.allMyStoryModels.add(story);
                        }
                        allMyStoriesAdapter.notifyDataSetChanged();
                    } else {

                    }
                }
            });
        }
    }
}