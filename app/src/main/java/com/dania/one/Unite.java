package com.dania.one;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;

public class Unite extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter adapter;
    ImageView profile_btn,search_btn,stories_btn,chats_btn,add_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unite);
        viewPager = findViewById(R.id.vp);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UniteSearch(),"Search");
        adapter.addFragment(new UniteStories2(),"Story");
        adapter.addFragment(new UniteChats2(),"Chat");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        profile_btn = findViewById(R.id.profile_btn);
        search_btn = findViewById(R.id.search_btn);
        stories_btn = findViewById(R.id.stories_btn);
        chats_btn = findViewById(R.id.chats_btn);
        add_btn = findViewById(R.id.add_btn);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });
        chats_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });
        stories_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);

            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);

            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }


        @Override public Fragment getItem(int position) {

            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void addFragment(Fragment fragment, String title){

            fragments.add(fragment);
            titles.add(title);

        }


        //ctrl+o


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }


}