package com.dania.one;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dania.one.DatabaseSqlite.DatabaseMyData;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jgabrielfreitas.core.BlurImageView;
import com.vlstr.blurdialog.BlurDialog;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

public class ViewStoryProt extends AppCompatActivity {

    private SimpleDraweeView dp;
    private TextView categories;
    private ImageView tags,location, story_iv;
    private ImageView story_bg;
    private Dialog dialog_prot;
    private RelativeLayout left, right;
    private int position = 0;
    private String my_dp, my_id, my_name;
    private LinearLayout rv;
    Drawable drawable;



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
        setContentView(R.layout.activity_view_story_prot);

        dp = (SimpleDraweeView) findViewById(R.id.dp);
        categories = findViewById(R.id.categories);
        tags = findViewById(R.id.tags);
        location = findViewById(R.id.location);
        story_iv = findViewById(R.id.story_iv);
        story_bg = findViewById(R.id.story_bg);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        Bitmap img =((BitmapDrawable) getResources().getDrawable(R.drawable.example_story)).getBitmap();
        Bitmap story = roundCorner(img);
        story_iv.setImageBitmap(story);
        //story_iv.setImageResource(R.drawable.example_story);
        drawable = getDrawable(R.drawable.example_story);
        if (Common.my_uid.equals(null)){
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

        Uri uri = Uri.parse(my_dp);
        dp.setImageURI(uri);
        Bitmap blurred = blurRenderScript(((BitmapDrawable) drawable).getBitmap(), 20);
        story_bg.setImageBitmap(blurred);
        dialog_prot = new Dialog(this);
        dialog_prot.setContentView(R.layout.dialog_tagged_users);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        dialog_prot.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_prot.setCanceledOnTouchOutside(true);
        rv = dialog_prot.findViewById(R.id.rv);
        View decor2 = dialog_prot.getWindow().getDecorView();
        decor2.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position==0){
                    position = 3;
                    Bitmap img =((BitmapDrawable) getResources().getDrawable(R.drawable.example_story11)).getBitmap();
                    Bitmap story = roundCorner(img);
                    story_iv.setImageBitmap(story);
                    //story_iv.setImageResource(R.drawable.example_story11);
                    drawable = getDrawable(R.drawable.example_story11);
                    Bitmap blurred = blurRenderScript(((BitmapDrawable) drawable).getBitmap(), 20);
                    story_bg.setImageBitmap(blurred);
                } else if (position==1) {
                    position = 0;
                    Bitmap img =((BitmapDrawable) getResources().getDrawable(R.drawable.example_story)).getBitmap();
                    Bitmap story = roundCorner(img);
                    story_iv.setImageBitmap(story);
                   // story_iv.setImageResource(R.drawable.example_story);
                    drawable = getDrawable(R.drawable.example_story);
                    Bitmap blurred = blurRenderScript(((BitmapDrawable) drawable).getBitmap(), 20);
                    story_bg.setImageBitmap(blurred);
                } else if (position==2) {
                    position = 1;
                    Bitmap img =((BitmapDrawable) getResources().getDrawable(R.drawable.example_story)).getBitmap();
                    Bitmap story = roundCorner(img);
                    story_iv.setImageBitmap(story);
                    //story_iv.setImageResource(R.drawable.example_story43);
                    drawable = getDrawable(R.drawable.example_story43);
                    Bitmap blurred = blurRenderScript(((BitmapDrawable) drawable).getBitmap(), 20);
                    story_bg.setImageBitmap(blurred);

                } else if (position==3) {
                    position = 2;
                    Bitmap img =((BitmapDrawable) getResources().getDrawable(R.drawable.example_story199)).getBitmap();
                    Bitmap story = roundCorner(img);
                    story_iv.setImageBitmap(story);
                    //story_iv.setImageResource(R.drawable.example_story199);
                    drawable = getDrawable(R.drawable.example_story199);
                    Bitmap blurred = blurRenderScript(((BitmapDrawable) drawable).getBitmap(), 20);
                    story_bg.setImageBitmap(blurred);
                } else {
                    position = 2;
                    Bitmap img =((BitmapDrawable) getResources().getDrawable(R.drawable.example_story199)).getBitmap();
                    Bitmap story = roundCorner(img);
                    story_iv.setImageBitmap(story);
                    //story_iv.setImageResource(R.drawable.example_story199);
                    drawable = getDrawable(R.drawable.example_story199);
                    Bitmap blurred = blurRenderScript(((BitmapDrawable) drawable).getBitmap(), 20);
                    story_bg.setImageBitmap(blurred);

                }

            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position==0){
                    position = 1;
                    Bitmap img =((BitmapDrawable) getResources().getDrawable(R.drawable.example_story43)).getBitmap();
                    Bitmap story = roundCorner(img);
                    story_iv.setImageBitmap(story);
                    //story_iv.setImageResource(R.drawable.example_story43);
                    drawable = getDrawable(R.drawable.example_story43);
                    Bitmap blurred = blurRenderScript(((BitmapDrawable) drawable).getBitmap(), 20);
                    story_bg.setImageBitmap(blurred);
                } else if (position==1) {
                    position = 2;
                    Bitmap img =((BitmapDrawable) getResources().getDrawable(R.drawable.example_story199)).getBitmap();
                    Bitmap story = roundCorner(img);
                    story_iv.setImageBitmap(story);
                    //story_iv.setImageResource(R.drawable.example_story199);
                    drawable = getDrawable(R.drawable.example_story199);
                    Bitmap blurred = blurRenderScript(((BitmapDrawable) drawable).getBitmap(), 20);
                    story_bg.setImageBitmap(blurred);
                } else if (position==2) {
                    position = 3;
                    Bitmap img =((BitmapDrawable) getResources().getDrawable(R.drawable.example_story11)).getBitmap();
                    Bitmap story = roundCorner(img);
                    story_iv.setImageBitmap(story);
                    //story_iv.setImageResource(R.drawable.example_story11);
                    drawable = getDrawable(R.drawable.example_story11);
                    Bitmap blurred = blurRenderScript(((BitmapDrawable) drawable).getBitmap(), 20);
                    story_bg.setImageBitmap(blurred);
                } else if (position==3) {
                    position = 0;
                    Bitmap img =((BitmapDrawable) getResources().getDrawable(R.drawable.example_story)).getBitmap();
                    Bitmap story = roundCorner(img);
                    story_iv.setImageBitmap(story);
                    //story_iv.setImageResource(R.drawable.example_story);
                    drawable = getDrawable(R.drawable.example_story);
                    Bitmap blurred = blurRenderScript(((BitmapDrawable) drawable).getBitmap(), 20);
                    story_bg.setImageBitmap(blurred);
                } else {
                    position = 0;
                    Bitmap img =((BitmapDrawable) getResources().getDrawable(R.drawable.example_story)).getBitmap();
                    Bitmap story = roundCorner(img);
                    story_iv.setImageBitmap(story);
                    //story_iv.setImageResource(R.drawable.example_story);
                    drawable = getDrawable(R.drawable.example_story);
                    Bitmap blurred = blurRenderScript(((BitmapDrawable) drawable).getBitmap(), 20);
                    story_bg.setImageBitmap(blurred);
                }

            }
        });

    }

    public Bitmap roundCorner(Bitmap mbitmap){
        Bitmap imageRounded=Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
        Canvas canvas=new Canvas(imageRounded);
        Paint mpaint=new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 20, 20, mpaint); // Round Image Corner 100 100 100 100
        return imageRounded;
    }

    public Bitmap blurRenderScript(Bitmap smallBitmap, int radius) {

        int width  = Math.round(smallBitmap.getWidth() * 0.1f);
        int height = Math.round(smallBitmap.getHeight() * 0.1f);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(smallBitmap, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(getApplicationContext());
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(radius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    private void showDialogBox() {
        // dialog_prot.show();
        final BlurDialog blurDialog = (BlurDialog) findViewById(R.id.blurView);
        blurDialog.create(getWindow().getDecorView(), 10);
        blurDialog.show();

    }
}
