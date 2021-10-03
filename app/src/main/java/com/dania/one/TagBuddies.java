package com.dania.one;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dania.one.Adapters.SearchTagBuddiesAdapter;
import com.dania.one.Adapters.SelectedAddCategoriesAdapter;
import com.dania.one.Adapters.SelectedTagBuddiesAdapter;
import com.dania.one.Adapters.TagBuddiesAdapter;
import com.dania.one.Adapters.chat_friend_adapter;
import com.dania.one.DatabaseSqlite.DatabaseHelperChatRanker;
import com.dania.one.DatabaseSqlite.DatabaseMyData;
import com.dania.one.Model.FriendModel;
import com.dania.one.Model.TagBuddyModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TagBuddies extends AppCompatActivity implements TagBuddiesListener,SearchTagBuddiesListener{


    private RecyclerView rv, rv_top, rv_search;
    private ArrayList<TagBuddyModel> tagBuddyModels = new ArrayList<>();
    private String my_id, my_name, my_dp;
    private DatabaseHelperChatRanker mDatabaseHelperRank;
    private String uid_text, name_text, dp_text;
    private Button tagBuddiesBtn, doneBtn;
    private Dialog confirDialog;
    private Button dc_discard;
    private Button dc_keep;
    private Cursor c;
    private DatabaseReference reference;
    private ArrayList<String> taggedIds = new ArrayList<>();
    TagBuddiesAdapter userAdapter;
    private SearchView sv;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_buddies);
        tagBuddiesBtn = findViewById(R.id.tagBuddiesBtn);
        doneBtn = findViewById(R.id.doneBtn);
        rv = findViewById(R.id.rv);
        rv_top = findViewById(R.id.rv_top);
        rv_search = findViewById(R.id.rv_search);
        sv = findViewById(R.id.sv);
        back = findViewById(R.id.back);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_top.setHasFixedSize(true);
        confirDialog = new Dialog(TagBuddies.this);
        confirDialog.setContentView(R.layout.dialog_confirm);
        confirDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        confirDialog.setCanceledOnTouchOutside(false);
        dc_discard = confirDialog.findViewById(R.id.dc_discard);
        dc_keep = confirDialog.findViewById(R.id.dc_keep);
        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
        lm.setOrientation(RecyclerView.HORIZONTAL);
        rv_top.setLayoutManager(lm);
        rv_search.setHasFixedSize(true);
        rv_search.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
        if (AddStoryGlobal.selectedTaggedBuddies.size()>0){
            doneBtn.setEnabled(true);
            doneBtn.setTextColor(getResources().getColor(R.color.colorAnahata));
            tagBuddyModels.addAll(AddStoryGlobal.selectedTaggedBuddies);
            SelectedTagBuddiesAdapter selectedTagBuddiesAdapter = new SelectedTagBuddiesAdapter(getApplicationContext(),AddStoryGlobal.selectedTaggedBuddies);
            rv_top.setAdapter(selectedTagBuddiesAdapter);
            userAdapter = new TagBuddiesAdapter(getApplicationContext(), AddStoryGlobal.selectedTaggedBuddies,this);
            rv.setAdapter(userAdapter);
            for (int i=0;i<AddStoryGlobal.selectedTaggedBuddies.size();i++){
                taggedIds.add(AddStoryGlobal.selectedTaggedBuddies.get(i).getId());
            }
        }
        reference = FirebaseDatabase.getInstance().getReference(my_id+"Bonds");
        mDatabaseHelperRank = new DatabaseHelperChatRanker(getApplicationContext());
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

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStoryGlobal.selectedTaggedBuddies = userAdapter.getSelectedIds();
                finish();
            }
        });

        dc_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStoryGlobal.selectedTaggedBuddies = new ArrayList<>();
                finish();
            }
        });

        dc_keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirDialog.dismiss();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fillup();
    }

    private void searchDatabase(String query) {
        ArrayList<TagBuddyModel> allTBM = userAdapter.tagBuddyModels;
        ArrayList<TagBuddyModel> resultTBM = new ArrayList<>();
        if (!query.trim().isEmpty()) {
            for (int i = 0; i < allTBM.size(); i++) {
                if (allTBM.get(i).getName().toLowerCase().contains(query.toLowerCase())) {
                    resultTBM.add(allTBM.get(i));
                }
            }
            SearchTagBuddiesAdapter searchTagBuddiesAdapter = new SearchTagBuddiesAdapter(getApplicationContext(), resultTBM, this);
            rv_search.setAdapter(searchTagBuddiesAdapter);
        }else {
            SearchTagBuddiesAdapter searchTagBuddiesAdapter = new SearchTagBuddiesAdapter(getApplicationContext(), resultTBM, this);
            rv_search.setAdapter(searchTagBuddiesAdapter);
        }
    }

    public void fillup() {
        c = mDatabaseHelperRank.getFriendsData("Buddies"+my_id);
        if (c.getCount()>0){
            while (c.moveToNext()){
                uid_text = c.getString(1);
                if (!taggedIds.contains(uid_text)) {
                    name_text = c.getString(2);
                    dp_text = c.getString(3);
                    TagBuddyModel tbm = new TagBuddyModel(uid_text,name_text,dp_text,false);
                    tagBuddyModels.add(tbm);
                }

            }
            userAdapter = new TagBuddiesAdapter(getApplicationContext(), tagBuddyModels,this);
            rv.setAdapter(userAdapter);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    addToArray();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void addToArray() {
        tagBuddyModels = new ArrayList<>();
        c = mDatabaseHelperRank.getFriendsData("Buddies"+my_id);
        if (c.getCount() > 0) {
            while (c.moveToNext()){
                uid_text = c.getString(1);
                name_text = c.getString(2);
                dp_text = c.getString(3);
                TagBuddyModel tbm = new TagBuddyModel(uid_text,name_text,dp_text,false);
                tagBuddyModels.add(tbm);
            }
            userAdapter = new TagBuddiesAdapter(getApplicationContext(), tagBuddyModels,this);
            rv.setAdapter(userAdapter);
        }
    }


    @Override
    public void OnAction(Boolean isSelected) {
        if (isSelected){
            doneBtn.setEnabled(true);
            doneBtn.setTextColor(getResources().getColor(R.color.colorAnahata));
            ArrayList<TagBuddyModel> tagBuddyModels = userAdapter.getSelectedIds();
            SelectedTagBuddiesAdapter selectedTagBuddiesAdapter = new SelectedTagBuddiesAdapter(getApplicationContext(),tagBuddyModels);
            rv_top.setAdapter(selectedTagBuddiesAdapter);
            searchDatabase(sv.getQuery().toString());
        }else {
            doneBtn.setEnabled(false);
            doneBtn.setTextColor(getResources().getColor(R.color.txt2));
            ArrayList<TagBuddyModel> tagBuddyModels = new ArrayList<>();
            SelectedTagBuddiesAdapter selectedTagBuddiesAdapter = new SelectedTagBuddiesAdapter(getApplicationContext(),tagBuddyModels);
            rv_top.setAdapter(selectedTagBuddiesAdapter);
            searchDatabase(sv.getQuery().toString());
        }
    }

    @Override
    public void OnSearchAction(Boolean isSelected) {
        if (isSelected){
            doneBtn.setEnabled(true);
            doneBtn.setTextColor(getResources().getColor(R.color.colorAnahata));
            ArrayList<TagBuddyModel> tagBuddyModels = userAdapter.getSelectedIds();
            SelectedTagBuddiesAdapter selectedTagBuddiesAdapter = new SelectedTagBuddiesAdapter(getApplicationContext(),tagBuddyModels);
            rv_top.setAdapter(selectedTagBuddiesAdapter);
        }else {
            doneBtn.setEnabled(false);
            doneBtn.setTextColor(getResources().getColor(R.color.txt2));
            ArrayList<TagBuddyModel> tagBuddyModels = new ArrayList<>();
            SelectedTagBuddiesAdapter selectedTagBuddiesAdapter = new SelectedTagBuddiesAdapter(getApplicationContext(),tagBuddyModels);
            rv_top.setAdapter(selectedTagBuddiesAdapter);
        }
    }

    @Override
    public void OnSelect(TagBuddyModel tagBuddyModel) {
        for (int i=0;i<userAdapter.tagBuddyModels.size();i++){
            if (userAdapter.tagBuddyModels.get(i).getId().equals(tagBuddyModel.getId())){
                userAdapter.tagBuddyModels.get(i).setSelected(tagBuddyModel.getSelected());
            }
        }
        userAdapter = new TagBuddiesAdapter(getApplicationContext(), userAdapter.tagBuddyModels,this);
        rv.setAdapter(userAdapter);
    }

    @Override
    public void OnDeSelect(TagBuddyModel tagBuddyModel) {
        for (int i=0;i<userAdapter.tagBuddyModels.size();i++){
            if (userAdapter.tagBuddyModels.get(i).getId().equals(tagBuddyModel.getId())){
                userAdapter.tagBuddyModels.get(i).setSelected(tagBuddyModel.getSelected());
            }
        }
        userAdapter = new TagBuddiesAdapter(getApplicationContext(), userAdapter.tagBuddyModels,this);
        rv.setAdapter(userAdapter);
    }

    @Override
    public void onBackPressed() {
        if (userAdapter.getSelectedIds().size()>0){
            confirDialog.show();
        }else {
            AddStoryGlobal.selectedTaggedBuddies = new ArrayList<>();
            super.onBackPressed();
        }
    }
}