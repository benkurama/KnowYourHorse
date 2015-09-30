package com.kyh;

import java.util.ArrayList;
import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import com.kyh.objects.HorseObj;
import com.kyh.objects.HorseSearchObj;
import com.kyh.objects.UserObj;

public class Vars extends Application{
	
	public static ArrayList<HorseObj> HorseList;
	public static ArrayList<HorseSearchObj> HorseSearchList;
	public static ArrayList<UserObj> UserList;
	public static boolean PageNavi;
	public static int HorsePos;
	public static int HorseSearchPos;
	public static boolean Online;
	public static boolean Refresh;
	// =========================================================================
	public void onCreate() {
		super.onCreate();
		
		HorseList = null;
		HorseSearchList = null;
		UserList = null;
		PageNavi = false;
		
		HorsePos = 0;
		HorseSearchPos = 0;
		
		Online = true;
		Refresh = false;
	}
	// =========================================================================
	public static boolean getRefresh(){
		return  Refresh;
	}
	public static void setRefresh(boolean refresh){
		Vars.Refresh = refresh;
	}
	// =========================================================================
	public static boolean getOnlineState(){
		return Online;
	}
	public static void setOnlineState(String state){
		
		if(state.equals("1")){
			Vars.Online = true;
		} else if (state.equals("0")) {
			Vars.Online = false;
		}
	}
	// =========================================================================
	public static boolean getPageNavi(){
		return PageNavi;
	}
	public static void setPageNavi(boolean navi){
		Vars.PageNavi = navi;
	}
	// =========================================================================
	public static int getHorsePos(){
		return HorsePos;
	}
	public static void setHorsePos(int pos){
		Vars.HorsePos = pos;
	}
	// =========================================================================
	public static int getHorseSearchPos(){
		return HorseSearchPos;
	}
	public static void setHorseSearchPos(int pos){
		Vars.HorseSearchPos = pos;
	}
	// =========================================================================
	// TODO Global Objects
	// =========================================================================
	// -- for User Objects
	public static ArrayList<UserObj> getUserlistObj() {
	    return UserList;
	}
	public static void setUserListObj(ArrayList<UserObj> userlist) {
		Vars.UserList = userlist;
	}
	public static void setUserListObjToNull() {
		Vars.UserList.clear();
	}
	// --- for Horse Object
	public static ArrayList<HorseObj> getHorseListObj(){
		return HorseList;
	}
	public static void setHorseListObj(ArrayList<HorseObj> horselist){
		Vars.HorseList = horselist;
	}
	// --- for Horse Search Object
	public static ArrayList<HorseSearchObj> getHorseSearchListObj(){
		return HorseSearchList;
	}
	public static void setHorseSearchList (ArrayList<HorseSearchObj> horselistsearch){
		Vars.HorseSearchList = horselistsearch;
	}
	// =========================================================================
	// TODO Preferences
	// =========================================================================
	
	// For User Object Preference
	public static String getUsername(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("username", null);
	}
	public static void setUsername(Context context,String name){
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString("username", name).commit();
	}
	// --------------------------------------------------------------
	public static String getEmail(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("email", null);
	}
	public static void setEmail(Context context,String email){
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString("email", email).commit();
	}
	// --------------------------------------------------------------
	public static String getName(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("name", null);
	}
	public static void setName(Context context,String namedisplay){
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString("name", namedisplay).commit();
	}
	// --------------------------------------------------------------
	public static String getUserID(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("userid", null);
	}
	public static void setUserID(Context context, String id){
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString("userid", id).commit();
	}
	// --------------------------------------------------------------
	public static String getUserType(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("usertype", null);
	}
	public static void setUserType(Context context, String type){
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString("usertype", type).commit();
	}
	// --------------------------------------------------------------
	public static void setUserObjToNull(Context context){
		
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString("username", "").commit();
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString("email", "").commit();
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString("name", "").commit();
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString("userid", "").commit();
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString("usertype", "").commit();
		
	}
	// -- FOR MAIN PAGE ONLY
	public static String getGreet(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("greet", null);
	}
	public static void setGreet(Context context){
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString("greet", "no").commit();
	}
	//--<
	// =========================================================================
	// TODO Final
}
