package com.dania.one;
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
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

public class ViewBuddiesStories extends AppCompatActivity {

    private CardStackView csv;
    int position;
    int current_position;
    boolean entered = false;
    int size;
    boolean all_finished = false;
    private TextView promoted,demoted, finish;
    private LottieAnimationView loading;
    private CardStackLayoutManager csv_manager;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_buddies_stories);
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
        size = StoriesFetchedGlobal.buddiesStoryModels.size();
        int cursorPosition = getCursorPosition(position);
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
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                if (position==size-1){
                    finish.setVisibility(View.VISIBLE);
                }
            }
        };
        csv_manager = new CardStackLayoutManager(getApplicationContext(),cardStackListener);
        csv_manager.setDirections(Direction.VERTICAL);
        csv_manager.setSwipeThreshold(0.07f);
        csv_manager.setCanScrollHorizontal(false);
        csv_manager.setCanScrollVertical(true);
        csv.setLayoutManager(csv_manager);
        csv.setAdapter(new ViewUserStoriesAdapter(getApplicationContext(),StoriesFetchedGlobal.buddiesStoryModels));
        csv.scrollToPosition(cursorPosition);

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


    public int getCursorPosition(int position){
        int number = 0;
        for (int i=0;i<position;i++){
            number = number+StoriesFetchedGlobal.buddies_packets.get(i).getStoryModels().size();
        }
        return number;
    }


}