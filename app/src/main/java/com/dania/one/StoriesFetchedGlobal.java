package com.dania.one;

import com.dania.one.Model.DateWiseStoriesModel;
import com.dania.one.Model.MonthWiseStoriesModel;
import com.dania.one.Model.Story;
import com.dania.one.Model.StoryPacket;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

public class StoriesFetchedGlobal {

    public static ArrayList<Story> MyLiveStories = new ArrayList<>();
    public static DocumentSnapshot MyLastStory = null;
    public static ArrayList<Story> allMyStoryModels = new ArrayList<>();


    public static DocumentSnapshot UserLastStory = null;
    public static ArrayList<Story> allUserStoryModels = new ArrayList<>();
    public static String user_uid = null;
    public static String user_dp = null;


    public static ArrayList<Story> TrendingAllStories;
    public static DocumentSnapshot TrendingLastStory;

    public static ArrayList<StoryPacket> buddies_packets = new ArrayList<>();
    public static ArrayList<Story> buddiesStoryModels = new ArrayList<>();

    public static ArrayList<Object> nearbyUsers = new ArrayList<>();
    public static ArrayList<StoryPacket> nearbyStoryPackets = new ArrayList<>();
    public static ArrayList<Story> nearbyStoryModels = new ArrayList<>();

}
