package com.dania.one;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.transition.ChangeBounds;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.dania.one.Adapters.chat_friend_adapter;
import com.dania.one.Adapters.search_chats_adapter;
import com.dania.one.Adapters.search_user_adapter;
import com.dania.one.Model.FriendModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class UniteChatsSearch extends AppCompatActivity {

    private RecyclerView rv;
    private SearchView sv;
    private ImageView sv_btn;
    private DatabaseReference user_ref;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unite_chats_search);
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);
        getWindow().setSharedElementEnterTransition(enterTransition());
        getWindow().setSharedElementReturnTransition(returnTransition());
        rv = findViewById(R.id.rv);
        sv = findViewById(R.id.sv);
        sv_btn = findViewById(R.id.sv_btn);
        sv.requestFocus();

        user_ref = FirebaseDatabase.getInstance().getReference().child("Users");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDatabase(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchDatabase(newText);
                return true;
            }
        });
        
        sv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sv.requestFocus();
            }
        });
        
    }

    private void searchDatabase(String query) {
        final ArrayList<FriendModel> userModels2 = new ArrayList<>();
        final ArrayList<String> uids = new ArrayList<>();
        if (!query.trim().isEmpty()){
            Query query1 = user_ref.orderByChild("name").startAt(query).endAt(query + "\uf8ff");
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String uid = snapshot.getKey();
                        if (!uids.contains(uid)){
                            uids.add(uid);
                            String name = snapshot.child("basicInfo").child("name").getValue().toString();
                            String dp = snapshot.child("basicInfo").child("display_picture").getValue().toString();
                            if (dataSnapshot.hasChild("token")){
                                String token = snapshot.child("token").getValue().toString();
                                FriendModel fm = new FriendModel(uid,name,dp,token);
                                userModels2.add(fm);
                            }else {

                                FriendModel fm = new FriendModel(uid,name,dp,null);
                                userModels2.add(fm);
                            }
                        }

                    }
                    addToRv(userModels2);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            String capQuery = capitalize(query);
            Query query2 = user_ref.orderByChild("name").startAt(capQuery).endAt(capQuery + "\uf8ff");
            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String uid = snapshot.getKey();
                        if (!uids.contains(uid)){
                            uids.add(uid);
                            String name = snapshot.child("basicInfo").child("name").getValue().toString();
                            String dp = snapshot.child("basicInfo").child("display_picture").getValue().toString();
                            if (dataSnapshot.hasChild("token")){
                                String token = snapshot.child("token").getValue().toString();
                                FriendModel fm = new FriendModel(uid,name,dp,token);
                                userModels2.add(fm);
                            }else {

                                FriendModel fm = new FriendModel(uid,name,dp,null);
                                userModels2.add(fm);
                            }
                        }

                    }
                    addToRv(userModels2);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Query query3 = user_ref.orderByChild("name").startAt(query.toLowerCase()).endAt(query.toLowerCase() + "\uf8ff");
            query3.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String uid = snapshot.getKey();
                        if (!uids.contains(uid)){
                            uids.add(uid);
                            String name = snapshot.child("basicInfo").child("name").getValue().toString();
                            String dp = snapshot.child("basicInfo").child("display_picture").getValue().toString();
                            if (dataSnapshot.hasChild("token")){
                                String token = snapshot.child("token").getValue().toString();
                                FriendModel fm = new FriendModel(uid,name,dp,token);
                                userModels2.add(fm);
                            }else {

                                FriendModel fm = new FriendModel(uid,name,dp,null);
                                userModels2.add(fm);
                            }
                        }

                    }
                    addToRv(userModels2);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }else {
            addToRv(userModels2);
        }


    }

    private void addToRv(ArrayList<FriendModel> userModels2) {
        rv.setAdapter(new search_chats_adapter(UniteChatsSearch.this,userModels2));
    }

    public static String capitalize(String input){
        String[] words = input.toLowerCase().split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<words.length;i++){
            String word = words[i];
            if (i>0&&words.length>0){
                builder.append(" ");
            }
            String cap = word.substring(0,1).toUpperCase()+word.substring(1);
            builder.append(cap);
        }
        return builder.toString();
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
}