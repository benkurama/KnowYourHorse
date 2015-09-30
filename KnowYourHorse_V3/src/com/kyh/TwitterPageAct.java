package com.kyh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import codes.libs.Utils;

import com.kyh.objects.TwitterClass;

public class TwitterPageAct extends Activity{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private Button LoginBtn, LogoutBtn, FollowBtn;
	private TwitterClass Twitter;
	private TextView Title;
	// ---------------------- //
	private Boolean Following;
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_twitter_page);
        
        SetupControls();
        
        runThis();
    }
    // =========================================================================
    @Override
	protected void onResume() {
		super.onResume();
		
		runThis();
	}
 // =========================================================================
 // TODO onClick View
 // =========================================================================
    public void onBack(View v){
    	
    	startActivity(new Intent(this,MainPageAct.class));
    	this.finish();
    	Vars.setPageNavi(false);
    }
    // =========================================================================
    public void onLogin(View v){
    	Twitter.login();
    }
    // =========================================================================
    public void onFollow(View v){
    	
		 if(Following){
			 
			 Twitter.unfollowUs();
		  }else{
			  
			  Twitter.follow();
		  }
		 
		  if(Twitter.checkFriendShipTask()){
			  
			  FollowBtn.setText("Unfollow");
			  Following = true;
			  Utils.MessageBox(this, "Successfully Follow");
		  }else{
			  
			  FollowBtn.setText("Follow us");
			  Following = false;
			  Utils.MessageBox(this, "Successfully Unfollow");
		  }
    	
    }
    // =========================================================================
    public void onLogout(View v){
    	
    	Twitter.logOutTweeter();
    	// --
    	LoginBtn.setVisibility(1);
    	FollowBtn.setVisibility(8);
    	LogoutBtn.setVisibility(8);
    }
 // =========================================================================
 // TODO Implementation
 // =========================================================================

 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	   	
    	LoginBtn = (Button)findViewById(R.id.btnTwitLogin);
    	LogoutBtn = (Button)findViewById(R.id.btnTwitLogout);
    	FollowBtn = (Button)findViewById(R.id.btnTwitFollow);
    	Title = (TextView)findViewById(R.id.tvTitle);
    	
    	// Initialize -------------------------------------------------------
    	Twitter = new TwitterClass(this, this);
    	Title.setTextSize(15);
    	Title.setText("Twitter Follow Page");
    }
    // =========================================================================
    public void runThis(){
    	
		final ProgressDialog dialog = ProgressDialog.show(this,"Loading...", "Please Wait");
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(Twitter.isTwitterLoggedInAlready()){
					logout();
					if(Twitter.checkFriendShipTask()){
						FollowBtn.setText("Unfollow");
						Following = true;
					}else{
						FollowBtn.setText("Follow us");
						Following = false;
					}
				}else{
					login();
				}
				dialog.dismiss();
			}
		};
 		Thread thread = new Thread(){
			@Override
			public void run() {
				Twitter.setAccount();
				handler.sendEmptyMessage(0);
			}
		};thread.start();
	}
    // =========================================================================
    public void logout(){
    	
    	LoginBtn.setVisibility(8);
    	FollowBtn.setVisibility(1);
    	LogoutBtn.setVisibility(1);
    }
    // =========================================================================
    public void login(){
    	
    	LoginBtn.setVisibility(1);
    	FollowBtn.setVisibility(8);
    	LogoutBtn.setVisibility(8);
    }
 // =========================================================================
 // TODO Final Destination
}
