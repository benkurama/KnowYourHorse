package com.kyh;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import codes.libs.NetUtils;
import codes.libs.Utils;
import codes.libs.network.NetBroadcast;

import com.kyh.objects.UserObj;
import com.kyh.utils.Global;
import com.kyh.utils.JsonParser;

public class UserMenuAct extends Activity implements Observer{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private Button Profile,Login,Inbox,Settings;
	// ---------------------- //
	private ArrayList<UserObj> UserList = new ArrayList<UserObj>();
	// ---------------------- //
	private Typeface fontType;
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_user_menu_act);
        
        SetupControls();
        // --- Page Transition
        Vars.setPageNavi(true);
         
        //new BroadcastWifi(getApplicationContext(), this);
        new NetBroadcast(getApplicationContext(), this);
    }
    // =========================================================================
    @Override
	protected void onResume() {
		super.onResume();
		// -- Detect if User has Currently Login
		if(Global.checkUserdata(this)){
			
			Profile.setEnabled(true);
			Profile.setBackgroundResource(R.drawable.user_button_xml);
			Inbox.setEnabled(true);
			Inbox.setBackgroundResource(R.drawable.user_button_xml);
			Settings.setEnabled(true);
			Settings.setBackgroundResource(R.drawable.user_button_xml);
			
			Login.setText("    Logout");
			
			
		} else {
			
			Profile.setEnabled(false);
			Profile.setBackgroundResource(R.drawable.user_buttons_press);
			Inbox.setEnabled(false);
			Inbox.setBackgroundResource(R.drawable.user_buttons_press);
			Settings.setEnabled(false);
			Settings.setBackgroundResource(R.drawable.user_buttons_press);
			
			Login.setText("    Login");
		}
		// --------------------------------------------------------------
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
 // TODO onClick View
 // =========================================================================
    public void onBack(View v){
    	this.finish();
    	Vars.setPageNavi(false);
    }
    // =========================================================================
    public void onProf(View v){
    	
    	if(Global.checkUserdata(this)){
        	
	    	UserList = Vars.getUserlistObj();
	    	if (UserList != null){
	    		
	    		startActivity(new Intent(this,UserProfileAct.class));
	    	} else {
	    		
	    		if(NetUtils.isNetworkOn(this)){
	    			callUserData(1);
	    		} else {
	    			Utils.MessageBox(this, "Internet Connection Needed");
	    		}
	    	}
    	}
    }
    // =========================================================================
    public void onHorse(View v){
    	startActivity(new Intent(this,HorseListAct.class));
    }
    // =========================================================================
    public void onSettings(View v){
    	
    	
    	if(Global.checkUserdata(this)){
        	
	    	UserList = Vars.getUserlistObj();
	    	if (UserList != null){
	    		
	    		startActivity(new Intent(this,UserSettingsMenuAct.class));
	    	} else {
	    		
	    		if(NetUtils.isNetworkOn(this)){
	    			callUserData(2);
	    		} else {
	    			Utils.MessageBox(this, "Internet Connection Needed");
	    		}
	    	}
    	}
    	
    }
    // =========================================================================
    public void onLogin(View v){
    	
    	if(Global.checkUserdata(this)){
    		
    		UserList = Vars.getUserlistObj();
    		if(UserList != null){
    			Vars.setUserListObjToNull();
    		}
    		Vars.setUserObjToNull(this);
    		Utils.MessageToast(this, "Account has logged out");
    		
    		this.finish();
    		
    	} else{
    		
    		startActivity(new Intent(this,UserLoginAct.class));
    	}
    }
 // =========================================================================
 // TODO Implementation
 // =========================================================================

 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	
    	Profile = (Button) findViewById(R.id.btnProfile);
    	Login = (Button) findViewById(R.id.btnLogin);
    	Inbox = (Button)findViewById(R.id.btnInbox);
    	Settings = (Button)findViewById(R.id.btnSettings);
    	
    	// Initialize -------------------------------------------------------
    	
    	fontType = Typeface.createFromAsset(getAssets(), "fonts/BEBAS.TTF");
    	
    	Profile.setTypeface(Global.getFontBebas(this),Typeface.BOLD);
    	Login.setTypeface(Global.getFontBebas(this),Typeface.BOLD);
    	Inbox.setTypeface(Global.getFontBebas(this),Typeface.BOLD);
    	Settings.setTypeface(Global.getFontBebas(this),Typeface.BOLD);
    }
    // =========================================================================
    public void callUserData(final int page){
    	
    	final String email = Vars.getEmail(this);
    	//final String password = PasswordET.getText().toString();
    	
    	if(NetUtils.isNetworkOn(this)){
    		
    		if(email.length() != 0){
    			
		    	final ProgressDialog dialog = ProgressDialog.show(this, "Please Wait..","Processing...", true);
				final Handler handler = new Handler() {
				   public void handleMessage(Message msg) {
				      dialog.dismiss();
				      ///// 2nd if the load finish -----
				       callUserProfile(page);
					  ///// -----
				      }};
				      Thread checkUpdate = new Thread() {  
				   public void run() {	
					  /// 1st main activity here... -----
					   UserList = JsonParser.getLoginData(email, getApplicationContext());
					  ////// -----
				      handler.sendEmptyMessage(0);				      
				      }};
				      checkUpdate.start();
    			
    		} else {
    			
    			Utils.MessageBox(this, "Fill all Textbox");
    		}
    		
    	} else {
    		
    		Utils.MessageBox(this, "Slow or No Internet Connection");
    	}
    }
    // =========================================================================
    public void callUserProfile(int page){
    	
    	Vars.setUserListObj(UserList);
    	
    	if (page == 1){
    		startActivity(new Intent(this,UserProfileAct.class));
    	} else if (page == 2){
    		startActivity(new Intent(this,UserSettingsMenuAct.class));
    	}
    }
 // =========================================================================
 // TODO Final Destination
}
