package com.dania.one;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Rational;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.dania.one.Adapters.ViewUserStoriesAdapter;
import com.dania.one.Model.Story;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.Date;

public class ViewTrendingStories extends AppCompatActivity {

    private CardStackView csv;
    int position;
    int current_position;
    boolean entered = false;
    int size;
    boolean all_finished = false;
    private TextView promoted,demoted, finish;
    private LottieAnimationView loading;
    private CollectionReference collectionReference;
    private DatabaseReference users_ref;
    private CardStackLayoutManager csv_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trending_stories);
        Intent i = getIntent();
        if (i!=null){
            position = i.getIntExtra("position",0);
            current_position = position;
        }else {

        }
        csv = (CardStackView) findViewById(R.id.csv);
        promoted = findViewById(R.id.promoted);
        demoted = findViewById(R.id.demoted);
        finish = findViewById(R.id.finish);
        loading = (LottieAnimationView) findViewById(R.id.loading);
        size = StoriesFetchedGlobal.TrendingAllStories.size();
        collectionReference = FirebaseFirestore.getInstance().collection("Stories");
        CardStackListener cardStackListener = new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                if (direction == Direction.Top){
                    promoted.setVisibility(View.VISIBLE);
                    demoted.setVisibility(View.GONE);
                }else {
                    demoted.setVisibility(View.VISIBLE);
                    promoted.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCardSwiped(Direction direction) {
                if (direction == Direction.Top){
                    promoted.setVisibility(View.GONE);
                    demoted.setVisibility(View.GONE);
                }else {
                    demoted.setVisibility(View.GONE);
                    promoted.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCardRewound() {

            }

            @Override
            public void onCardCanceled() {
                promoted.setVisibility(View.GONE);
                demoted.setVisibility(View.GONE);

            }

            @Override
            public void onCardAppeared(View view, int position) {
                current_position = position;
                users_ref = FirebaseDatabase.getInstance().getReference("Users").child(StoriesFetchedGlobal.TrendingAllStories.get(position).getUid()).child("countInfo").child("views");
                collectionReference.document(StoriesFetchedGlobal.TrendingAllStories.get(position).getStory_id()).update("views", FieldValue.increment(1));
                users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long views = (long) dataSnapshot.getValue();
                        long updated_views = views+1;
                        users_ref.setValue(updated_views);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                if (position==size-1){
                    loading.setVisibility(View.VISIBLE);
                    getMoreTrending();
                }
            }
        };
        csv_manager = new CardStackLayoutManager(getApplicationContext(),cardStackListener);
        csv_manager.setDirections(Direction.VERTICAL);
        csv_manager.setSwipeThreshold(0.07f);
        csv_manager.setCanScrollHorizontal(false);
        csv_manager.setCanScrollVertical(true);
        csv.setLayoutManager(csv_manager);
        csv.setAdapter(new ViewUserStoriesAdapter(getApplicationContext(),StoriesFetchedGlobal.TrendingAllStories));
        csv.scrollToPosition(position);
    }

    private void getMoreTrending() {
        if (StoriesFetchedGlobal.TrendingLastStory!=null){
            int ts = Math.round(new Date().getTime() / 1000);
            int tsYesterday = ts - (60*24*3600);
            //remove 60* for last 24 hour stories
            Query tsQuery = collectionReference.whereGreaterThan("timestamp",tsYesterday).orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING).orderBy("views", com.google.firebase.firestore.Query.Direction.DESCENDING);
            tsQuery.startAfter(StoriesFetchedGlobal.TrendingLastStory).limit(20).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.size()>0){
                        StoriesFetchedGlobal.TrendingLastStory = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size()-1);
                        ArrayList<Story> storyModels = new ArrayList<>();
                        for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots){
                            Story story = querySnapshot.toObject(Story.class);
                            storyModels.add(story);
                        }
                        StoriesFetchedGlobal.TrendingAllStories.addAll(storyModels);
                        loading.setVisibility(View.GONE);
                        size = StoriesFetchedGlobal.TrendingAllStories.size();
                        csv.setAdapter(new ViewUserStoriesAdapter(getApplicationContext(),StoriesFetchedGlobal.TrendingAllStories));
                    } else {
                        loading.setVisibility(View.GONE);
                        finish.setVisibility(View.VISIBLE);
                        all_finished = true;
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (entered||all_finished){
                finish();
            }else {
                Display d = csv.getDisplay();
                Point p = new Point();
                d.getSize(p);
                int w = p.x;
                int h = p.y;

                Rational ratio = new Rational(w,h);
                PictureInPictureParams.Builder pip_builder = new PictureInPictureParams.Builder();
                pip_builder.setAspectRatio(ratio).build();
                enterPictureInPictureMode(pip_builder.build());
                entered = true;
            }
        }else {
            finish();
        }

    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode,newConfig);
        if (isInPictureInPictureMode){
            csv_manager.findViewByPosition(current_position).findViewById(R.id.controls).setVisibility(View.GONE);
            csv_manager.findViewByPosition(current_position).findViewById(R.id.btm_rl).setVisibility(View.GONE);
        }else {
            csv_manager.findViewByPosition(current_position).findViewById(R.id.controls).setVisibility(View.VISIBLE);
            csv_manager.findViewByPosition(current_position).findViewById(R.id.btm_rl).setVisibility(View.VISIBLE);
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