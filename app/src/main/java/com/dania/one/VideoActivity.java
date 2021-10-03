package com.dania.one;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    private VideoView vv;
    private ImageView play, back , forward;

    //SimpleExoPlayer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        vv = (VideoView) findViewById(R.id.story_vv);
        play = findViewById(R.id.play);
        back = findViewById(R.id.back);
        forward = findViewById(R.id.forward);
        final MediaController controller = new MediaController(this);
        controller.setMediaPlayer(vv);
        vv.setMediaController(controller);
        Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/projectdns-120a2.appspot.com/o/Cloud%2FF6GBUJ6R9efaNV6N5EtSk10NXYq2Video1608384384-1734442520.mp4?alt=media&token=704dcadc-0f62-4015-8e3c-4393a8d9966f");
        vv.setVideoURI(uri);
        vv.start();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vv.isPlaying()){
                    play.setImageDrawable(getDrawable(R.drawable.vector_play));
                    vv.stopPlayback();
                }else {
                    play.setImageDrawable(getDrawable(R.drawable.vector_pause));
                    vv.start();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vv.seekTo(vv.getCurrentPosition()-1);
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vv.seekTo(vv.getCurrentPosition()+1);
            }
        });
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