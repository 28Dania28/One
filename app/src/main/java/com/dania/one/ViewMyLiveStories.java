package com.dania.one;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dania.one.DatabaseSqlite.DatabaseMyData;
import com.dania.one.Model.Story;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class ViewMyLiveStories extends AppCompatActivity {

    private SimpleDraweeView dp;
    private TextView time;
    private SimpleDraweeView story_bg,story_iv;
    private RelativeLayout left, right;
    private int position = 0;
    private int no_of_stories;
    Story sm;
    private String my_dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_live_stories);
        dp = (SimpleDraweeView) findViewById(R.id.dp);
        time = findViewById(R.id.time);
        story_iv = (SimpleDraweeView) findViewById(R.id.story_iv);
        story_bg = (SimpleDraweeView) findViewById(R.id.story_bg);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        no_of_stories = StoriesFetchedGlobal.MyLiveStories.size();
        if (Common.my_uid.equals(null)){
            DatabaseMyData mydatadb = new DatabaseMyData(getApplicationContext());
            Cursor c = mydatadb.getData();
            if (c.getCount() > 0) {
                while (c.moveToNext()){
                    my_dp = c.getString(2);
                }
            }
        }else {
            my_dp = Common.my_dp;
        }
        Uri uri = Uri.parse(my_dp);
        dp.setImageURI(uri);
        getStory();
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position!=0){
                    position = position-1;
                    getStory();
                }
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position+1<no_of_stories){
                    position = position+1;
                    getStory();
                }else {

                }
            }
        });

    }

    private void getStory() {
        sm = StoriesFetchedGlobal.MyLiveStories.get(position);
        String timeAgo = TimeAgo.getTimeAgo(sm.getTimestamp());
        time.setText(timeAgo);
        if (sm.getType().equals("image")) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(sm.getUri()))
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(20))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(story_bg.getController())
                    .build();
            story_bg.setController(controller);
            Uri uri = Uri.parse(sm.getUri());
            story_iv.setImageURI(uri);
        }else {

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