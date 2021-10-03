package com.dania.one;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.ChangeBounds;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.dania.one.Adapters.chat_friend_adapter;
import com.dania.one.DatabaseSqlite.DatabaseHelperChatRanker;
import com.dania.one.DatabaseSqlite.DatabaseMyData;
import com.dania.one.Model.FriendModel;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UniteChats2 extends Fragment implements FragmentLifecycle{

    private RecyclerView rv;
    private String my_id, my_name, my_dp;
    private ArrayList<FriendModel> friendModels = new ArrayList<>();
    private ArrayList<String> buddiesIds = new ArrayList<>();
    private DatabaseHelperChatRanker mDatabaseHelperRank;
    private String uid_text, name_text, dp_text, token_text;
    private SimpleDraweeView pp;
    private DatabaseReference reference, my_ref;
    private ImageView sv_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unite_chats2, container, false);
        getActivity().getWindow().setSharedElementEnterTransition(enterTransition());
        getActivity().getWindow().setSharedElementReturnTransition(returnTransition());
        pp = (SimpleDraweeView) view.findViewById(R.id.pp);
        sv_btn = view.findViewById(R.id.sv_btn);
        rv = view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        if (Common.my_uid.equals(null)){
            DatabaseMyData mydatadb = new DatabaseMyData(getContext());
            Cursor cur = mydatadb.getData();
            if (cur.getCount() > 0) {
                while (cur.moveToNext()){
                    my_id = cur.getString(0);
                    my_name = cur.getString(1);
                    my_dp = cur.getString(2);
                }
            }
        }else {
            my_id = Common.my_uid;
            my_name = Common.my_name;
            my_dp = Common.my_dp;
        }
        Uri uri = Uri.parse(my_dp);
        pp.setImageURI(uri);
        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UniteMyProfile.class);
                startActivity(intent);
            }
        });

        sv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),UniteChatsSearch.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(sv_btn, "sv_iv_transition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity() , pairs);
                startActivity(intent, options.toBundle());
            }
        });

        reference = FirebaseDatabase.getInstance().getReference(my_id+"Bonds");
        my_ref = FirebaseDatabase.getInstance().getReference("Users").child(my_id).child("countInfo").child("buddies_modify");
        mDatabaseHelperRank = new DatabaseHelperChatRanker(getContext());
        return view;
    }

    public void fillup() {
        friendModels = new ArrayList<>();
        buddiesIds = new ArrayList<>();
        Cursor c = mDatabaseHelperRank.getFriendsData("Buddies"+my_id);
        if (c.getCount()>0){
            while (c.moveToNext()){
                uid_text = c.getString(1);
                name_text = c.getString(2);
                dp_text = c.getString(3);
                token_text = c.getString(4);
                FriendModel m = new FriendModel(uid_text,name_text,dp_text,token_text);
                friendModels.add(m);
                buddiesIds.add(uid_text);
            }
            chat_friend_adapter userAdapter = new chat_friend_adapter(getContext(), friendModels);
            rv.setAdapter(userAdapter);
            my_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue().toString().equals("1")){
                        readOnce();
                        my_ref.setValue("0");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            /*reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        int item_count = (int)dataSnapshot.getChildrenCount();
                        if (item_count!=c.getCount()){
                            mDatabaseHelperRank.deleteAllFriends("Buddies"+my_id);
                            readOnce();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

             */
        }
        else {
            readOnce();
        }

    }

    private void readOnce() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        final String uid = snapshot.getKey();
                        if (!buddiesIds.contains(uid)){
                            DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("basicInfo");
                            user_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String name = dataSnapshot.child("name").getValue().toString();
                                    String dp_str = dataSnapshot.child("display_picture").getValue().toString();
                                    if (dataSnapshot.hasChild("token")){
                                        String token = dataSnapshot.child("token").getValue().toString();
                                        boolean insertData = mDatabaseHelperRank.addFriendsData("Buddies"+my_id,uid,name,dp_str,token);
                                    }else {
                                        boolean insertData = mDatabaseHelperRank.addFriendsData("Buddies"+my_id,uid,name,dp_str,"nothing");
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                    addToArray();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private android.transition.Transition enterTransition() {
        android.transition.ChangeBounds bounds = new android.transition.ChangeBounds();
        bounds.setDuration(100);

        return bounds;
    }

    private android.transition.Transition returnTransition() {
        android.transition.ChangeBounds bounds = new ChangeBounds();
        bounds.setInterpolator(new DecelerateInterpolator());
        bounds.setDuration(100);

        return bounds;
    }

    private void addToArray() {
        friendModels = new ArrayList<>();
        buddiesIds = new ArrayList<>();
        Cursor c = mDatabaseHelperRank.getFriendsData("Buddies"+my_id);
        if (c.getCount()>0) {
            while (c.moveToNext()) {
                uid_text = c.getString(1);
                name_text = c.getString(2);
                dp_text = c.getString(3);
                token_text = c.getString(4);
                FriendModel m = new FriendModel(uid_text, name_text, dp_text, token_text);
                friendModels.add(m);
                buddiesIds.add(uid_text);
            }
            chat_friend_adapter userAdapter = new chat_friend_adapter(getContext(), friendModels);
            rv.setAdapter(userAdapter);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            fillup();
        }catch (Exception e){
            Log.d("ResumeException",e.getMessage());
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            checkBuddies();
        }else {

        }
    }

    private void checkBuddies() {
        my_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue().toString().equals("1")){
                    readOnce();
                    my_ref.setValue("0");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }
}