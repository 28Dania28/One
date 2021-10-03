package com.dania.one;
import com.dania.one.Model.MsgModel;
import java.util.ArrayList;

public class ChatsFetchedGlobal {

    public static ArrayList<MsgModel> mediaChats = new ArrayList<>();
    public static ArrayList<String> mediaUri = new ArrayList<>();
    public static ArrayList<MsgModel> allChats = new ArrayList<>();
    public static String user_name = "";
    public static String user_uid = "";
    public static String user_dp = "";
    public static ArrayList<String> allChatsKey = new ArrayList<>();
    public static int replySelected = -1;
}
