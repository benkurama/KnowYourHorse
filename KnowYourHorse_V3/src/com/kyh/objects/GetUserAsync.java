package com.kyh.objects;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import codes.libs.Utils;

import com.kyh.Vars;
import com.kyh.utils.JsonParser;

public class GetUserAsync extends AsyncTask<Void, Void, String>{

	private String Email;
	private Context Core;
	
	public GetUserAsync(Context core, String email){
		Core = core;
		Email = email;
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		
	}
	
	@Override
	protected String doInBackground(Void... params) {
		
		//String post = "Hello Universe"; 
		
		ArrayList<UserObj> UserList = new ArrayList<UserObj>();
		
		UserList = JsonParser.getLoginData(Email, Core);
		
		Vars.setUserListObj(UserList);
		
		return UserList.get(0).DISPLAYNAME;
	}
	@Override
	protected void onPostExecute(String value){
		
		Utils.MessageToast(Core, "Welcome " +value);
	}

}