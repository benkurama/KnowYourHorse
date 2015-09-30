package com.kyh;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.kyh.objects.UserObj;
import com.kyh.utils.JsonParser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import codes.libs.NetUtils;
import codes.libs.Utils;
import codes.libs.network.NetBroadcast;

public class UserLoginAct extends Activity implements Observer{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private TextView TitleTV;
	private EditText EmailET,PasswordET;
	
	private ArrayList<UserObj> UserList = new ArrayList<UserObj>();
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_user_login);
        
        new NetBroadcast(getApplicationContext(), this);
        SetupControls();
        
        if (!TextUtils.isEmpty(Vars.getEmail(this))){
        	
        	EmailET.setText(Vars.getEmail(this));
        }
    }
    // =========================================================================
    @Override
	public void update(Observable observable, Object data) {
		 
    	Vars.setOnlineState(data.toString());
	}
 // =========================================================================
 // TODO onClick View
 // =========================================================================
    public void onBack(View v){
    	this.finish();
    }
    // =========================================================================
    @SuppressLint("HandlerLeak")
	public void onLog(View v){
    	
    	final String email = EmailET.getText().toString();
    	//final String password = PasswordET.getText().toString();
    	
    	if(NetUtils.isNetworkOn(this)){
    		
    		if(email.length() != 0){
    			
		    	final ProgressDialog dialog = ProgressDialog.show(this, "Please Wait..","Processing...", true);
				final Handler handler = new Handler() {
				   public void handleMessage(Message msg) {
				      dialog.dismiss();
				      ///// 2nd if the load finish -----
				      setVars();
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
 // TODO Implementation
 // =========================================================================

 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	   	
    	TitleTV = (TextView)findViewById(R.id.tvTitle);
    	EmailET = (EditText)findViewById(R.id.etUsername);
    	PasswordET = (EditText)findViewById(R.id.etPassword);
    	
    	// Initialize -------------------------------------------------------
    	
    	TitleTV.setText("User Login");
    }
 // =========================================================================
    public void setVars(){
    	
    	if(UserList.get(0).VALID.equals("1")){
    		
    		Vars.setUserListObj(UserList);
        	
        	Vars.setUsername(this, UserList.get(0).USERNAME);
        	Vars.setEmail(this, UserList.get(0).EMAIL);
        	Vars.setName(this, UserList.get(0).DISPLAYNAME);
        	Vars.setUserID(this, UserList.get(0).USERID);
        	Vars.setUserType(this, UserList.get(0).MEMBERSHIPTYPE);
        	
        	Vars.setRefresh(true);
        	
        	this.finish();
        	Vars.setPageNavi(false);
        	
    	} else if (UserList.get(0).VALID.equals("0")){
    		
    		Utils.MessageBox(this, "Your email or password is invalid, try again");
    	} else if(UserList.get(0).VALID.equals("false")){
    		
    		Utils.MessageBox(this, "Slow or No Internet Connection");
    	}
    }
// =========================================================================
// TODO Test Functions
// =========================================================================

// =========================================================================
// TODO Final Destination
}
