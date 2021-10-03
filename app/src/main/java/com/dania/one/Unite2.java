package com.dania.one;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.dania.one.DatabaseSqlite.DatabaseMyData;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import java.util.ArrayList;

public class Unite2 extends AppCompatActivity {

    private NonSwipeableViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ImageView search_btn,stories_btn,chats_btn;
    private LinearLayout btm_lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unite2);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        viewPager = findViewById(R.id.vp);
        search_btn = findViewById(R.id.search_btn);
        stories_btn = findViewById(R.id.stories_btn);
        chats_btn = findViewById(R.id.chats_btn);
        btm_lay = findViewById(R.id.btm_lay);
        setUpViewPager();
        initializeCommonData();
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hyperspace_jump);
                search_btn.startAnimation(hyperspaceJumpAnimation);

            }
        });
        chats_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hyperspace_jump);
                chats_btn.startAnimation(hyperspaceJumpAnimation);

            }
        });
        stories_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hyperspace_jump);
                stories_btn.startAnimation(hyperspaceJumpAnimation);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setUpViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UniteSearch());
        adapter.addFragment(new UniteStories2());
        adapter.addFragment(new UniteChats2());
        viewPager.setAdapter(adapter);
        viewPager.setNestedScrollingEnabled(false);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FragmentLifecycle fragmentToShow = (FragmentLifecycle) adapter.getItem(position);
                fragmentToShow.onResumeFragment();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initializeCommonData() {
        DatabaseMyData mydatadb = new DatabaseMyData(Unite2.this);
        try {
            Cursor cur = mydatadb.getData();
            if (cur.getCount() > 0) {
                while (cur.moveToNext()){
                    Common.my_uid = cur.getString(0);
                    Common.my_name = cur.getString(1);
                    Common.my_dp = cur.getString(2);
                }
            }
        }catch (Exception e){
            Log.d("ErrorInUnite2",e.getMessage());
        }
    }
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem()!=1){
            viewPager.setCurrentItem(1);
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                    btm_lay.getLayoutParams();
            HideBottomViewOnScrollBehavior behavior = (HideBottomViewOnScrollBehavior) params.getBehavior();
            behavior.slideUp(btm_lay);
        }else {
            super.onBackPressed();
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
        }


        @Override
        public Fragment getItem(int position) {

            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void addFragment(Fragment fragment) {

            fragments.add(fragment);

        }

    }

}