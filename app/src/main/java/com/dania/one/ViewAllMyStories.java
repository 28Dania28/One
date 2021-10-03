package com.dania.one;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.dania.one.Adapters.ViewUserStoriesAdapter;
import com.dania.one.Model.DateWiseStoriesModel;
import com.dania.one.Model.Story;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;

public class ViewAllMyStories extends AppCompatActivity {

    private CardStackView csv;
    int position;
    int size;
    private TextView finish;
    private LottieAnimationView loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_my_stories);
        Intent i = getIntent();
        if (i!=null){
            position = i.getIntExtra("position",0);
        }else {

        }
        csv = (CardStackView) findViewById(R.id.csv);
        finish = findViewById(R.id.finish);
        loading = (LottieAnimationView) findViewById(R.id.loading);
        size = StoriesFetchedGlobal.allMyStoryModels.size();
        CardStackListener cardStackListener = new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {

            }

            @Override
            public void onCardSwiped(Direction direction) {

            }

            @Override
            public void onCardRewound() {

            }

            @Override
            public void onCardCanceled() {

            }

            @Override
            public void onCardAppeared(View view, int position) {

            }

            @Override
            public void onCardDisappeared(View view, int position) {
                if (position==size-1){
                    loading.setVisibility(View.VISIBLE);
                    getMoreStories();
                }
            }
        };
        CardStackLayoutManager csv_manager = new CardStackLayoutManager(getApplicationContext(),cardStackListener);
        csv_manager.setDirections(Direction.VERTICAL);
        csv_manager.setSwipeThreshold(0.07f);
        csv_manager.setCanScrollHorizontal(false);
        csv_manager.setCanScrollVertical(true);
        csv.setLayoutManager(csv_manager);
        csv.setAdapter(new ViewUserStoriesAdapter(getApplicationContext(),StoriesFetchedGlobal.allMyStoryModels));
        csv.scrollToPosition(position);

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
                        loading.setVisibility(View.GONE);
                        size = StoriesFetchedGlobal.allMyStoryModels.size();
                        csv.setAdapter(new ViewUserStoriesAdapter(getApplicationContext(),StoriesFetchedGlobal.allMyStoryModels));
                    } else {
                        loading.setVisibility(View.GONE);
                        finish.setVisibility(View.VISIBLE);
                    }
                }
            });
        }


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}