package com.kyh;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import codes.libs.Utils;
import codes.libs.network.NetBroadcast;

import com.kyh.objects.PageCurlView;
import com.kyh.objects.UserObj;
import com.kyh.utils.Configs;
import com.kyh.utils.Global;
import com.kyh.utils.JsonParser;

public class MainPageAct extends Activity implements Observer{
 // =========================================================================
 // TODO Variables
 // =========================================================================
	private ImageView FlipIV;
	
	ArrayList<UserObj> UserList = new ArrayList<UserObj>();
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_main_page);
        
        
        SetupControls();
        Vars.setPageNavi(true);
        new NetBroadcast(getApplicationContext(), this);
        
        //Global.lockOrientationPortrait(this);
        //lockOrientationLandscape();
        
        if(TextUtils.isEmpty(Vars.getGreet(this))){
        	
        	Utils.MessageBox2(this,"Greetings!..To start, Kindly go to user accounts and login using your credentials");
        	Vars.setGreet(this);
        }
        // -----=-----=-----=----- ><
        // --- Get all data from UserDataObj
//        if(Global.checkUserdata(this)){
//        	
//        	UserList = Vars.getUserlistObj();
//        	
//        	if(UserList == null){
//        		// --- Reserved Codes
//        		//new GetUserAsync(this, Vars.getEmail(this)).execute();
//        		// 2nd Option call Userlist with Progress dialog
//        		callProgressDial();
//        		
//        	}
//        } 
    }
    // =========================================================================
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
	}
    // =========================================================================
    @Override
	protected void onResume() {
		super.onResume();
		
		if(Vars.getPageNavi()){
			this.overridePendingTransition(R.anim.slide_right_in,R.anim.slide_right_out);
		} else {
			this.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
		}
	}
    
    // =========================================================================
    @Override
	public void update(Observable observable, Object data) {
		 
    	Vars.setOnlineState(data.toString());
    	
    	if(Vars.getOnlineState()){
    		//Utils.MessageToast(this, "May Internet");
    	} else {
    		//Utils.MessageToast(this, "Walang Internet");
    	}
    	
	}
    // =========================================================================
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	System.gc();
    	finish();
    }
 // =========================================================================
 // TODO onClick View
 // =========================================================================
    public void onUser(View v){
    	
    	startActivityForResult(new Intent(this,UserMenuAct.class),Configs.UserPage);
    }
    // =========================================================================
    public void onHorse(View v){
    	
    	startActivity(new Intent(this,HorseSearchAct.class));
    }
    // =========================================================================
    public void onSign(View v){
    	
    	Uri url = Uri.parse(Configs.KYH_REGISTRATION_LINK);
		startActivity(new Intent(Intent.ACTION_VIEW,url));
    }
    // =========================================================================
    public void onFlip(View v){
    	
    	//Utils.MessageBox(this, "Facebook and Twitter");
    	
//    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
//
//    	alert.setTitle("Like us on");
// 		//alert.setMessage(""); 
//
// 		alert.setPositiveButton("Facebook", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				
//				callFacebook();
//			}
//		});
//
// 		alert.setNegativeButton("Twitter", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				
//				callTwitter();
//			}
//		});
// 		alert.show(); 
    	setContentView(R.layout.act_page_curl);
    }
    // =========================================================================
    public void onMain(View v){
    	
    	//startActivity(new Intent(this,MainPageAct.class));
    	setContentView(R.layout.act_main_page);
    }
    // =========================================================================
    public void onFacebook(View v){
    	
    	callFacebook();
    }
    // =========================================================================
    public void onTwitter(View v){
    	
    	callTwitter();
    }
 // =========================================================================
 // TODO Implementation
 // =========================================================================

 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	 
    	FlipIV = (ImageView)findViewById(R.id.ivFlip);
    	
    	// --- Initialization
    }
    // =========================================================================
    public void callProgressDial(){
    	
    	final ProgressDialog dialog = ProgressDialog.show(this, "Please Wait..","Get user data", true);
        		final Handler handler = new Handler() {
        		   public void handleMessage(Message msg) {
        		      dialog.dismiss();
        		      ///// 2nd if the load finish -----
        		      setupUserList();
        			  ///// -----
        		      }};
        		      Thread checkUpdate = new Thread() {  
        		   public void run() {	
        			  /// 1st main activity here -----
        			  UserList = JsonParser.getLoginData(Vars.getEmail(getApplicationContext()), getApplicationContext()); 
        			  ////// -----
        		      handler.sendEmptyMessage(0);				      
        		      }};
        		      checkUpdate.start();
    	
    }
    // =========================================================================
    public void setupUserList(){
    	
    	if(!UserList.get(0).VALID.equals("false")){
    		
    		Vars.setUserListObj(UserList);
    	} else {
    		Utils.MessageBox(this, "Slow or No Internet");
    	}
    }
    // =========================================================================
    public void callFacebook(){
    	
//    	Bundle FacebookData = new Bundle();
//    	FacebookData.putString("value", "facebook");
//    	
//    	Intent facebookIntent = new Intent(this,WebsiteAct.class);
//    	facebookIntent.putExtras(FacebookData);
//    	
//    	startActivity(facebookIntent);
    	
    	Uri url = Uri.parse(Configs.KYH_FACEBOOK_LIKE);
		startActivity(new Intent(Intent.ACTION_VIEW,url));
    }
    // =========================================================================
    public void callTwitter(){
    	
//    	Bundle FacebookData = new Bundle();
//    	FacebookData.putString("value", "twitter");
//    	
//    	Intent facebookIntent = new Intent(this,WebsiteAct.class);
//    	facebookIntent.putExtras(FacebookData);
//    	
//    	startActivity(facebookIntent);
    	// --
//    	Uri url = Uri.parse(Configs.KYH_TWITTER_LIKE);
//		startActivity(new Intent(Intent.ACTION_VIEW,url));
    	// --
    	startActivity(new Intent(this,TwitterPageAct.class));
    }
 // =========================================================================
 // TODO Final Destination
}
