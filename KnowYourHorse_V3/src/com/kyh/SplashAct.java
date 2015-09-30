package com.kyh;

import java.util.ArrayList;

import codes.libs.NetUtils;
import codes.libs.Utils;

import com.kyh.objects.GetUserAsync;
import com.kyh.objects.UserObj;
import com.kyh.utils.Global;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;

public class SplashAct extends Activity{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private Handler handler;
	
	ArrayList<UserObj> UserList = new ArrayList<UserObj>();
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_splash);
        
        SetupControls();
        
    	if(Global.checkUserdata(this)){
        	
        	UserList = Vars.getUserlistObj();
        	
        	if(UserList == null){
        		
        		if(NetUtils.isNetworkOn(this)){
        			
        			new GetUserAsync(this, Vars.getEmail(this)).execute();
        		} else {
        			
        			Utils.MessageToast(this, "User data not retrive, cannot access to internet");
        		}
        	}
        } 
        
        handler = new Handler();
        handler.postDelayed(Running, 3000);
    }
 // =========================================================================
 // TODO onClick View
 // =========================================================================
    public void onBack(View v){
    	this.finish();
    }
 // =========================================================================
 // TODO Implementation
 // =========================================================================
    private Runnable Running = new Runnable(){
		public void run() {
			MainPage();
			
			
		}
    };
 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	   	
    }
    // =========================================================================
    public void MainPage(){
    	
    	startActivity(new Intent(this,MainPageAct.class));
    	this.finish();
    }
 // =========================================================================
 // TODO Final Destination
}
