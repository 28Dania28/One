package com.dania.one;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dania.one.Adapters.stories_bond_adapter;
import com.dania.one.Adapters.stories_nearby_adapter;
import com.dania.one.Adapters.stories_trending_adapter;
import com.dania.one.Adapters.story_loading_holder_adapter;
import com.dania.one.DatabaseSqlite.DatabaseHelperChatRanker;
import com.dania.one.DatabaseSqlite.DatabaseMyData;
import com.dania.one.Model.Story;
import com.dania.one.Model.StoryPacket;
import com.dania.one.Model.UserModel;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.leochuan.ScaleLayoutManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class prot2 extends Fragment {

    private RecyclerView rv_top;
    private SimpleDraweeView pp;
    private ImageView add,addCategory;
    private Location my_location;
    private RelativeLayout trending_rl, nearby_rl, bond_rl;
    private CircleImageView trending_icon, nearby_icon, bond_icon;
    private TextView bond_label, nearby_label, trending_label;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private List<String> a = new ArrayList<String>();
    private List<String> b = new ArrayList<String>();
    private ArrayList<UserModel> nearby_user_model = new ArrayList<>();
    private ArrayList<UserModel> bond_user_model = new ArrayList<>();
    private ArrayList<StoryPacket> nearbyStoryPackets = new ArrayList<>();
    private ArrayList<StoryPacket> bondStoryPackets = new ArrayList<>();
    private String my_id, my_name, my_dp;
    private String name_text, dp_text, email_text, token_text;
    private int dock_position = 0;
    private DatabaseHelperChatRanker mDatabaseHelperRank;
    private String uid_text;
    private Cursor c;
    private DatabaseReference locRef;
    private DatabaseReference user_ref;
    private RecyclerView rv;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unite_stories2, container, false);
        rv_top = view.findViewById(R.id.rv_top);
        add = view.findViewById(R.id.add);
        pp = (SimpleDraweeView) view.findViewById(R.id.pp);
        trending_rl = view.findViewById(R.id.trending_rl);
        nearby_rl = view.findViewById(R.id.nearby_rl);
        bond_rl = view.findViewById(R.id.bond_rl);
        trending_icon = view.findViewById(R.id.trending_icon);
        nearby_icon = view.findViewById(R.id.nearby_icon);
        bond_icon = view.findViewById(R.id.bond_icon);
        trending_label = view.findViewById(R.id.trending_label);
        nearby_label = view.findViewById(R.id.nearby_label);
        bond_label = view.findViewById(R.id.bond_label);
        addCategory = view.findViewById(R.id.addCategory);
        trending_label.setTextColor(getResources().getColor(R.color.iBlue));
        rv = view.findViewById(R.id.rv);
        rv_top.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.HORIZONTAL);
        rv_top.setLayoutManager(lm);

        ScaleLayoutManager scaleLayoutManager = new ScaleLayoutManager(getContext(),1,ScaleLayoutManager.HORIZONTAL);
        rv.setLayoutManager(scaleLayoutManager);
        //new CenterSnapHelper().attachToRecyclerView(rv);

        rv.setHasFixedSize(true);
        rv.setAdapter(new story_loading_holder_adapter(getContext(),"Trending"));


        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.hyperspace_jump);
                addCategory.startAnimation(hyperspaceJumpAnimation);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UniteAdd.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(add, "add_transition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity() , pairs);
                startActivity(intent, options.toBundle());
            }
        });
        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UniteMyProfile.class);
                startActivity(intent);
            }
        });

        trending_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.hyperspace_jump);
                trending_icon.startAnimation(hyperspaceJumpAnimation);
                trending_label.setTextColor(getResources().getColor(R.color.iBlue));
                int a  = dock_position;
                dock_position = 0;
                switch (a){
                    case 0:
                        break;
                    case 1:
                        nearby_label.setTextColor(getResources().getColor(R.color.iTextGrey));
                        break;
                    case 2:
                        bond_label.setTextColor(getResources().getColor(R.color.iTextGrey));
                        break;

                }
                rv.setAdapter(new story_loading_holder_adapter(getContext(),"Trending"));
                getTrendingStories();
            }
        });

        nearby_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.hyperspace_jump);
                nearby_icon.startAnimation(hyperspaceJumpAnimation);
                nearby_label.setTextColor(getResources().getColor(R.color.iBlue));
                int b  = dock_position;
                dock_position = 1;
                switch (b){
                    case 0:
                        trending_label.setTextColor(getResources().getColor(R.color.iTextGrey));
                        break;
                    case 1:
                        break;
                    case 2:
                        bond_label.setTextColor(getResources().getColor(R.color.iTextGrey));
                        break;

                }
                rv.setAdapter(new story_loading_holder_adapter(getContext(),"Nearby"));
                a = new ArrayList<>();
                nearby_user_model = new ArrayList<>();
                nearbyStoryPackets = new ArrayList<>();
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    getLocation();//+updateLocation+getNearbyUsers
                }else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });

        bond_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.hyperspace_jump);
                bond_icon.startAnimation(hyperspaceJumpAnimation);
                bond_label.setTextColor(getResources().getColor(R.color.iBlue));
                int a  = dock_position;
                dock_position = 2;
                switch (a){
                    case 0:
                        trending_label.setTextColor(getResources().getColor(R.color.iTextGrey));
                        break;
                    case 1:
                        nearby_label.setTextColor(getResources().getColor(R.color.iTextGrey));
                        break;
                    case 2:
                        break;

                }
                rv.setAdapter(new story_loading_holder_adapter(getContext(),"Bond"));
                b = new ArrayList<>();
                bond_user_model = new ArrayList<>();
                bondStoryPackets = new ArrayList<>();
                c = mDatabaseHelperRank.getFriendsData("Buddies"+my_id);
                if (c.getCount()>0) {
                    while (c.moveToNext()) {
                        uid_text = c.getString(1);
                        b.add(uid_text);
                    }
                }
                if (b.size()>0){
                    checkBondStoryExisted(0);
                }

            }
        });
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
        user_ref = FirebaseDatabase.getInstance().getReference("Users");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        locRef = FirebaseDatabase.getInstance().getReference("LiveLocations");
        geoFire = new GeoFire(locRef);
        mDatabaseHelperRank = new DatabaseHelperChatRanker(getContext());

        getTrendingStories();

        return view;
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                my_location = task.getResult();
                if (my_location != null) {
                    updateLocation();
                }
            }
        });
    }


    private void updateLocation() {
        if (my_location != null) {
            geoFire.setLocation(my_id, new GeoLocation(my_location.getLatitude(), my_location.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    getNearbyUsers();
                }
            });
        } else {
            Toast.makeText(getContext(), "Error in updating location.", Toast.LENGTH_SHORT).show();
        }
    }


    private void getNearbyUsers() {
        geoQuery = geoFire.queryAtLocation(new GeoLocation(my_location.getLatitude(),my_location.getLongitude()),Common.radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!key.equals(my_id)){
                    if (!a.contains(key)){
                        checkStoryExisted(key);
                    }else {

                    }
                }else {

                }

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }


    private void checkBondStoryExisted(final int position) {
        if (b.size()>position){
            final String key = b.get(position);
            user_ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("last_story")){
                        int last_story_ts = Integer.valueOf(dataSnapshot.child("last_story").getValue().toString());
                        int ts = Math.round(new Date().getTime() / 1000);
                        int tsYesterday = ts - (24*3600);
                        if (last_story_ts > tsYesterday){
                            String name = dataSnapshot.child("name").getValue().toString();
                            String dp_str = dataSnapshot.child("display_picture").getValue().toString();
                            UserModel um = new UserModel(key,name,dp_str,null,null,null);
                            getBondStory(key,um,tsYesterday);
                        }else {

                        }
                        checkBondStoryExisted(position+1);
                    }else {
                        checkBondStoryExisted(position+1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void checkStoryExisted(final String key) {
        user_ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("last_story")){
                    a.add(key);
                    int last_story_ts = Integer.valueOf(dataSnapshot.child("last_story").getValue().toString());
                    int ts = Math.round(new Date().getTime() / 1000);
                    int tsYesterday = ts - (24*3600);
                    if (last_story_ts > tsYesterday){
                        String name = dataSnapshot.child("name").getValue().toString();
                        String dp_str = dataSnapshot.child("display_picture").getValue().toString();
                        UserModel um = new UserModel(key,name,dp_str,null,null,null);
                        getStory(key,um,tsYesterday);
                    }else {

                    }
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getStory(String key, final UserModel um, int tsYesterday) {
        com.google.firebase.firestore.Query query = collectionReference.whereGreaterThan("timestamp", tsYesterday).whereEqualTo("uid", key).orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0) {
                    ArrayList<Story> storyModels = new ArrayList<>();
                    for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {
                        Story story = querySnapshot.toObject(Story.class);
                        storyModels.add(story);
                    }
                    addToNearbyRv(um, storyModels);
                }
            }
        });

    }


    private void getBondStory(String key, final UserModel um, int tsYesterday) {
        com.google.firebase.firestore.Query query = collectionReference.whereGreaterThan("timestamp", tsYesterday).whereEqualTo("uid", key).orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0) {
                    ArrayList<Story> storyModels = new ArrayList<>();
                    for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {
                        Story story = querySnapshot.toObject(Story.class);
                        storyModels.add(story);
                    }
                    addToBondRv(um, storyModels);
                }
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Stories");

    }


    private void getTrendingStories() {
        int ts = Math.round(new Date().getTime() / 1000);
        int tsYesterday = ts - (24*3600);
        com.google.firebase.firestore.Query query = collectionReference.whereGreaterThan("timestamp",tsYesterday).orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING).orderBy("views", com.google.firebase.firestore.Query.Direction.DESCENDING);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size()>0){
                    ArrayList<Story> storyModels = new ArrayList<>();
                    for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots){
                        Story story = querySnapshot.toObject(Story.class);
                        storyModels.add(story);
                    }
                    addToRv(storyModels);
                }
            }
        });
    }

    private void addToRv(ArrayList<Story> storyModels) {
        StoriesFetchedGlobal.TrendingAllStories = storyModels;
        rv.setAdapter(new stories_trending_adapter(getContext(),storyModels));
    }

    private void addToNearbyRv(UserModel um, ArrayList<Story> storyModels) {
        StoryPacket storyPacket = new StoryPacket(um,storyModels);
        StoriesFetchedGlobal.nearbyStoryModels.addAll(storyModels);
        StoriesFetchedGlobal.nearbyStoryPackets.add(storyPacket);
        if (Common.selected_category==1){
            stories_nearby_adapter nearby_adapter = new stories_nearby_adapter(getContext());
            rv.setAdapter(nearby_adapter);
        }
    }


    private void addToBondRv(UserModel um, ArrayList<Story> storyModels) {
        bond_user_model.add(um);
        StoryPacket storyPacket = new StoryPacket(um,storyModels);
        StoriesFetchedGlobal.buddies_packets.add(storyPacket);
        stories_bond_adapter buddies_adapter = new stories_bond_adapter(getContext());
        rv.setAdapter(buddies_adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            getLocation();
        }else {
            Toast.makeText(getContext(), "Permissions declined.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }

}

