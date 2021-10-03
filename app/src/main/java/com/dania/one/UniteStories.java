package com.dania.one;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.dania.one.Adapters.CategoriesAdapter2;
import com.dania.one.Adapters.stories_bond_adapter;
import com.dania.one.Adapters.stories_nearby_adapter;
import com.dania.one.Adapters.stories_trending_adapter;
import com.dania.one.Adapters.story_loading_holder_adapter;
import com.dania.one.DatabaseSqlite.DatabaseHelperChatRanker;
import com.dania.one.DatabaseSqlite.DatabaseMyData;
import com.dania.one.Model.CategoriesModel;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.leochuan.CenterSnapHelper;
import com.leochuan.GalleryLayoutManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class UniteStories extends Fragment implements CategoriesListener,FragmentLifecycle{


    private RecyclerView rv_top;
    private SimpleDraweeView pp;
    private ImageView add, searchCategory;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private List<String> b = new ArrayList<>();
    private String my_id, my_name, my_dp;
    private int dock_position = 0;
    private DatabaseHelperChatRanker mDatabaseHelperRank;
    private String uid_text;
    private Cursor c;
    private DatabaseReference locRef;
    private DatabaseReference user_ref;
    private RecyclerView rv;
    private FirebaseFirestore db;
    private CollectionReference collectionReference, catReference;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Query tsQuery, nsQuery, bsQuery;
    private CategoriesAdapter2 categoriesAdapter2;
    private ImageView mode;
    private DocumentSnapshot trendingLastStory = null;
    private stories_trending_adapter trending_adapter;
    private stories_bond_adapter buddies_adapter;
    private stories_nearby_adapter nearby_adapter;
    private int tsYesterday;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unite_stories, container, false);
        mode = view.findViewById(R.id.mode);
        rv_top = view.findViewById(R.id.rv_top);
        add = view.findViewById(R.id.add);
        pp = (SimpleDraweeView) view.findViewById(R.id.pp);
        searchCategory = view.findViewById(R.id.searchCategory);
        rv = view.findViewById(R.id.rv);
        checkMode();
        rv_top.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.HORIZONTAL);
        rv_top.setLayoutManager(lm);
        final GalleryLayoutManager gallaryLayoutManager = new GalleryLayoutManager(getContext(),5,LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(gallaryLayoutManager);
        new CenterSnapHelper().attachToRecyclerView(rv);
        rv.setHasFixedSize(true);
        rv.setAdapter(new story_loading_holder_adapter(getContext(), "Trending"));
        user_ref = FirebaseDatabase.getInstance().getReference("Users");
        if (Common.day){
            mode.setImageDrawable(getContext().getDrawable(R.drawable.sun));
        }else {
            mode.setImageDrawable(getContext().getDrawable(R.drawable.moon_img));
        }
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        locRef = FirebaseDatabase.getInstance().getReference("LiveLocations");
        geoFire = new GeoFire(locRef);
        mDatabaseHelperRank = new DatabaseHelperChatRanker(getContext());
        getCategories();
        getTrendingStories();
        searchCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), VideoActivity.class);
                Intent intent = new Intent(getContext(), LocationActivity.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(getContext(),UniteStoriesPickGallary.class);
                startActivity(i2);
            }
        });


        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UniteMyProfile.class);
                startActivity(intent);
            }
        });

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollHorizontally(1)) {
                    if (Common.selected_category==0){
                        getMoreTrending();
                    }
                }
            }

        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Stories");
        catReference = db.collection("Categories");

    }

    private void checkMode() {
        SharedPreferences sharedPreferences
                = getContext().getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor
                = sharedPreferences.edit();
        final boolean isDarkModeOn
                = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", false);
        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv.scrollToPosition(0);
                if (Common.day){
                    Common.day = false;
                    editor.putBoolean(
                            "isDarkModeOn", true);
                    editor.apply();
                    getActivity().recreate();
                    AppCompatDelegate
                            .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    mode.setImageDrawable(getContext().getDrawable(R.drawable.moon_img));
                }else {
                    Common.day = true;
                    editor.putBoolean(
                            "isDarkModeOn", false);
                    editor.apply();
                    AppCompatDelegate
                            .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    mode.setImageDrawable(getContext().getDrawable(R.drawable.sun));
                }
            }
        });
    }

    private void getLocation() {
        if (LocationGlobal.location==null){
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 28);
                }
                return;
            }
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    LocationGlobal.location = location;
                    if (LocationGlobal.location != null) {
                        geoFire.setLocation(my_id, new GeoLocation(LocationGlobal.location.getLatitude(), LocationGlobal.location.getLongitude()), new GeoFire.CompletionListener() {
                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                getNearbyUsers();

                            }
                        });
                    }
                }
            });
        }else {
            geoFire.setLocation(my_id, new GeoLocation(LocationGlobal.location.getLatitude(), LocationGlobal.location.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    getNearbyUsers();

                }
            });
        }
    }

    private void getNearbyUsers() {
        geoQuery = geoFire.queryAtLocation(new GeoLocation(LocationGlobal.location.getLatitude(),LocationGlobal.location.getLongitude()),Common.radius);
        geoQuery.removeAllListeners();
        int ts = Math.round(new Date().getTime() / 1000);
        tsYesterday = ts - (60*24*3600);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!key.equals(my_id)){
                    if (!StoriesFetchedGlobal.nearbyUsers.contains(key)){
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

    private void checkStoryExisted(final String key) {
        user_ref.child(key).child("basicInfo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("last_story")){
                    StoriesFetchedGlobal.nearbyUsers.add(key);
                    int last_story_ts = Integer.valueOf(dataSnapshot.child("last_story").getValue().toString());
                    //Remove 60* for last 24 hour stories
                    if (last_story_ts > tsYesterday){
                        String name = dataSnapshot.child("name").getValue().toString();
                        String dp_str = dataSnapshot.child("display_picture").getValue().toString();
                        UserModel um = new UserModel(key,name,dp_str,null,null,null);
                        getNearbyStory(key,um,tsYesterday);
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

    private void getNearbyStory(String key, final UserModel um, int tsYesterday) {
        nsQuery = collectionReference.whereGreaterThan("timestamp", tsYesterday).whereEqualTo("uid", key).orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING);
        nsQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
        bsQuery = collectionReference.whereGreaterThan("timestamp", tsYesterday).whereEqualTo("uid", key).orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING);
        bsQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

    private void getCategories() {
        final ArrayList<CategoriesModel> categoriesModels = new ArrayList<>();
        CategoriesModel cat1, cat2, cat3;
        cat1 = new CategoriesModel("Trending","https://firebasestorage.googleapis.com/v0/b/projectdns-120a2.appspot.com/o/Categories%2FCategoriesATrending1601368832-1127976149.jpg?alt=media&token=a147815f-f976-48cf-b79c-a4225fbf14cb",1,true);
        cat2 = new CategoriesModel("Nearby","https://firebasestorage.googleapis.com/v0/b/projectdns-120a2.appspot.com/o/Categories%2FCategoriesBNearby1601707264-524083585.jpg?alt=media&token=7dfa767c-9893-429a-9885-f55308945947",2,false);
        cat3 = new CategoriesModel("Buddies","https://firebasestorage.googleapis.com/v0/b/projectdns-120a2.appspot.com/o/Categories%2FCategoriesCBuddies1601707392859287040.jpg?alt=media&token=beb3c78b-36f7-4963-9975-cf30a95e559b",3,false);
        categoriesModels.add(cat1);
        categoriesModels.add(cat2);
        categoriesModels.add(cat3);
       /* catReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size()>0){
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                        CategoriesModel categoriesModel = queryDocumentSnapshot.toObject(CategoriesModel.class);
                        categoriesModels.add(categoriesModel);
                    }

                }
            }
        });
        */
        setCategoriesAdapter(categoriesModels);

    }

    private void setCategoriesAdapter(ArrayList<CategoriesModel> categoriesModels) {
        categoriesAdapter2 = new CategoriesAdapter2(getContext(),categoriesModels,this);
        rv_top.setAdapter(categoriesAdapter2);
    }


    public void getTrendingStories() {
        rv.setAdapter(new story_loading_holder_adapter(getContext(), "Trending"));
        int ts = Math.round(new Date().getTime() / 1000);
        int tsYesterday = ts - (60*24*3600);
        //remove 60* for last 24 hour stories
        tsQuery = collectionReference.whereGreaterThan("timestamp",tsYesterday).orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING).orderBy("views", com.google.firebase.firestore.Query.Direction.DESCENDING);
        tsQuery.limit(20).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size()>0){
                    trendingLastStory = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size()-1);
                    StoriesFetchedGlobal.TrendingLastStory = trendingLastStory;
                    ArrayList<Story> storyModels = new ArrayList<>();
                    for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots){
                        Story story = querySnapshot.toObject(Story.class);
                        storyModels.add(story);
                    }
                    StoriesFetchedGlobal.TrendingAllStories = storyModels;
                    addToTrendingRv();
                }
            }
        });
    }

    private void getMoreTrending() {
        if (trendingLastStory!=null){
            int ts = Math.round(new Date().getTime() / 1000);
            tsYesterday = ts - (60*24*3600);
            //remove 60* for last 24 hour stories
            tsQuery = collectionReference.whereGreaterThan("timestamp",tsYesterday).orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING).orderBy("views", com.google.firebase.firestore.Query.Direction.DESCENDING);
            tsQuery.startAfter(trendingLastStory).limit(20).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.size()>0){
                        trendingLastStory = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size()-1);
                        StoriesFetchedGlobal.TrendingLastStory = trendingLastStory;
                        ArrayList<Story> storyModels = new ArrayList<>();
                        for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots){
                            Story story = querySnapshot.toObject(Story.class);
                            storyModels.add(story);
                        }
                        StoriesFetchedGlobal.TrendingAllStories.addAll(storyModels);
                        trending_adapter.notifyDataSetChanged();
                    }
                }
            });
        }

    }


    private void getNearbyStories() {
        rv.setAdapter(new story_loading_holder_adapter(getContext(), "Nearby"));
        getLocation();
    }

    private void getBuddiesStories(){
        rv.setAdapter(new story_loading_holder_adapter(getContext(), "Buddies"));
        int ts = Math.round(new Date().getTime() / 1000);
        tsYesterday = ts - (60*24*3600);
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

    private void checkBondStoryExisted(final int position) {
        if (b.size()>position){
            final String key = b.get(position);
            user_ref.child(key).child("basicInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("last_story")){
                        int last_story_ts = Integer.valueOf(dataSnapshot.child("last_story").getValue().toString());
                        //Remove 60* for last 24 hour stories
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



    private void addToTrendingRv() {
        if (Common.selected_category==0){
            trending_adapter = new stories_trending_adapter(getContext(),StoriesFetchedGlobal.TrendingAllStories);
            rv.setAdapter(trending_adapter);
        }
    }

    private void addToNearbyRv(UserModel um, ArrayList<Story> storyModels) {
        StoryPacket storyPacket = new StoryPacket(um,storyModels);
        StoriesFetchedGlobal.nearbyStoryModels.addAll(storyModels);
        StoriesFetchedGlobal.nearbyStoryPackets.add(storyPacket);
        if (Common.selected_category==1){
            nearby_adapter = new stories_nearby_adapter(getContext());
            rv.setAdapter(nearby_adapter);
        }
    }


    private void addToBondRv(UserModel um, ArrayList<Story> storyModels) {
        StoryPacket storyPacket = new StoryPacket(um,storyModels);
        StoriesFetchedGlobal.buddiesStoryModels.addAll(storyModels);
        StoriesFetchedGlobal.buddies_packets.add(storyPacket);
        if (Common.selected_category==2){
            buddies_adapter = new stories_bond_adapter(getContext());
            rv.setAdapter(buddies_adapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==28 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
            getLocation();
        }else {
            Toast.makeText(getContext(), "Permission not granted by you.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Common.selected_category = 0;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void OnSelect(String name, int type) {
        ArrayList<CategoriesModel> cms = categoriesAdapter2.getAllModels();
        for (int i=0;i<cms.size();i++){
            if (cms.get(i).getName()==name){
                cms.get(i).setSelected(true);
            }else {
                cms.get(i).setSelected(false);
            }
        }
        categoriesAdapter2 = new CategoriesAdapter2(getContext(),cms,this);
        rv_top.setAdapter(categoriesAdapter2);

        if (type==1){
            getTrendingStories();
        }else if (type==2){
            StoriesFetchedGlobal.nearbyUsers = new ArrayList<>();
            StoriesFetchedGlobal.nearbyStoryModels = new ArrayList<>();
            StoriesFetchedGlobal.nearbyStoryPackets = new ArrayList<>();
            getNearbyStories();
        }else if (type==3){
            b = new ArrayList<>();
            StoriesFetchedGlobal.buddiesStoryModels = new ArrayList<>();
            StoriesFetchedGlobal.buddies_packets = new ArrayList<>();
            getBuddiesStories();
        }else {

        }

    }


    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }
}


