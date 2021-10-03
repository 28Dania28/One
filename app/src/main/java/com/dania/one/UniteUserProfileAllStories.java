package com.dania.one;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.Adapters.AllMyStoriesAdapter;
import com.dania.one.Adapters.AllUserStoriesAdapter;
import com.dania.one.Model.Story;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class UniteUserProfileAllStories extends AppCompatActivity {

    private ImageView back;
    private CircleImageView pp;
    private RecyclerView rv;
    private AllUserStoriesAdapter allUserStoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unite_user_profile_all_stories);
        pp = findViewById(R.id.pp);
        back = findViewById(R.id.back);
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new GridLayoutManager(this,3));
        Glide.with(getApplicationContext()).load(StoriesFetchedGlobal.user_dp).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(pp);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        allUserStoriesAdapter = new AllUserStoriesAdapter(UniteUserProfileAllStories.this);
        rv.setAdapter(allUserStoriesAdapter);
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
        if (StoriesFetchedGlobal.UserLastStory!=null){
            CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Stories");
            Query query = collectionReference.whereEqualTo("uid", StoriesFetchedGlobal.user_uid).orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING);
            query.startAfter(StoriesFetchedGlobal.UserLastStory).limit(20).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.size() > 0) {
                        for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {
                            Story story = querySnapshot.toObject(Story.class);
                            StoriesFetchedGlobal.UserLastStory = querySnapshot;
                            StoriesFetchedGlobal.allUserStoryModels.add(story);
                        }
                        allUserStoriesAdapter.notifyDataSetChanged();
                    } else {

                    }
                }
            });
        }
    }
}