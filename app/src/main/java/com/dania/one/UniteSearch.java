package com.dania.one;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.dania.one.Adapters.search_nearby_user_adapter;
import com.dania.one.Adapters.search_user_adapter;
import com.dania.one.DatabaseSqlite.DatabaseMyData;
import com.dania.one.Model.FriendModel;
import com.dania.one.Model.UserModel;
import com.dania.one.Spaces.SpacesItemDecoration;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;


public class UniteSearch extends Fragment implements FragmentLifecycle{

    private SearchView sv;
    private String my_id, my_name, my_dp;
    private RecyclerView rv,rv2;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private DatabaseReference locRef;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private DatabaseReference user_ref, bonds;
    private ImageView search_btn,addCategory;
    private RelativeLayout nearby_item;
    private CircleImageView catIcon;
    private String date;
    //private int last_radius = 25;q
    private List<String> a = new ArrayList<String>();
    private ArrayList<UserModel> user_model = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unite_search, container, false);
        Log.d("ShowLife","onCreateView");
        sv = view.findViewById(R.id.sv);
        rv2 = view.findViewById(R.id.rv2);
        rv = view.findViewById(R.id.rv);
        addCategory = view.findViewById(R.id.addCategory);
        nearby_item = view.findViewById(R.id.nearby_item);
        catIcon = view.findViewById(R.id.catIcon);
        search_btn = view.findViewById(R.id.search_btn);
        SpacesItemDecoration decoration = new SpacesItemDecoration(30);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv2.setHasFixedSize(true);
        rv2.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setLayoutManager(gridLayoutManager);
        rv.addItemDecoration(decoration);
        rv.setNestedScrollingEnabled(false);
        rv.setHasFixedSize(true);
        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (Common.my_uid.equals(null)){
            DatabaseMyData mydatadb = new DatabaseMyData(getContext());
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

        nearby_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.hyperspace_jump);
                catIcon.startAnimation(hyperspaceJumpAnimation);
            }
        });
        //last_radius = Common.radius; when changing radius
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sv.requestFocus();
                Intent intent = new Intent(getContext(), ViewChatMedia.class);
                startActivity(intent);
            }
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        locRef = FirebaseDatabase.getInstance().getReference("LiveLocations");
        geoFire = new GeoFire(locRef);
        bonds = FirebaseDatabase.getInstance().getReference(my_id + "Bonds");
        user_ref = FirebaseDatabase.getInstance().getReference().child("Users");
        getLocation();
        return view;
    }



    private void getLocation() {
        if (LocationGlobal.location == null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 28);
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
        } else {
            geoFire.setLocation(my_id, new GeoLocation(LocationGlobal.location.getLatitude(), LocationGlobal.location.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    getNearbyUsers();

                }
            });
    }

    }

    private void getNearbyUsers() {
       /* a = new ArrayList<>();
        user_model = new ArrayList<>();
        when changing radius
        */
        geoQuery = geoFire.queryAtLocation(new GeoLocation(LocationGlobal.location.getLatitude(),LocationGlobal.location.getLongitude()),Common.radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!key.equals(my_id)){
                    if (!a.contains(key)){
                        a.add(key);
                        checkBond(key);
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


    private void checkBond(final String key) {
        bonds.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.hasChild(key)){
                        addToRV(key,"bonded");
                    }else {
                        addToRV(key,"not_bonded");
                    }
                }else {
                    addToRV(key,"not_bonded");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void addToRV(final String key, final String status) {
        user_ref.child(key).child("basicInfo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String dp_str = dataSnapshot.child("display_picture").getValue().toString();
                UserModel um = new UserModel(key,name,dp_str,"",null,status);
                user_model.add(um);
                Common.FFuser_uid = a;
                Common.FFuser_model = user_model;
                rv.setAdapter(new search_nearby_user_adapter(getContext(),user_model));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        if (Common.FFuser_model!=null){
            a = Common.FFuser_uid;
            rv.setAdapter(new search_nearby_user_adapter(getContext(),Common.FFuser_model));
        }
       /* if (last_radius!=Common.radius){
            getNearbyUsers();
        }
        when changing radius
        */
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

    private void searchDatabase(String query){
        final ArrayList<FriendModel> userModels2 = new ArrayList<>();
        final ArrayList<String> uids = new ArrayList<>();
        if (!query.trim().isEmpty()){
            Query query1 = user_ref.orderByChild("name").startAt(query).endAt(query + "\uf8ff");
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //String uid = snapshot.child("user_id").getValue().toString();
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
                        //String uid = snapshot.child("user_id").getValue().toString();
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
                        //String uid = snapshot.child("user_id").getValue().toString();
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

    private void addToRv(ArrayList<FriendModel> userModels3) {
        rv2.setAdapter(new search_user_adapter(getContext(),userModels3));
    }


    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
//        getLocation();
        //run();
    }


    private void run() {
        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
    }
}
