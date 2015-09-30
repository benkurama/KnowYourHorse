package com.kyh;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import codes.libs.Utils;
import codes.libs.network.NetBroadcast;

import com.kyh.objects.UserObj;
import com.kyh.utils.Global;
import com.kyh.utils.JsonParser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class UserSettingsGeneralAct extends Activity implements Observer{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private TextView Title;
	private EditText EmailAddET,ProfileAddET;
	private Spinner RegionLocaleSpn,TimeZoneSpn;
	private Button EditBtn;
	private ImageView EditIcon1,EditIcon2;
	
	private boolean UpdateMode = false;
	
	private String[] TimeZoneVal,TimeZoneSel,RegionLocaleVal,RegionLocaleSel;
	private String Succeed,Id,EmailAdd,ProfileAdd,TimeZone,RegionLocale;
	
	ArrayList<UserObj> UserList = new ArrayList<UserObj>();
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_user_settings_general);
        
        new NetBroadcast(getApplicationContext(), this);
        SetupControls();
        Vars.setPageNavi(true);
        
        EmailAddET.setText(UserList.get(0).EMAIL);
		ProfileAddET.setText(UserList.get(0).USERNAME);
		
		//-->>
		int posTime = 0;
		for (int x = 0;x < TimeZoneVal.length;x++){
			
			if(TimeZoneVal[x].equals(UserList.get(0).TIMEZONE)){
				posTime = x;
				break;
			}
		}
		TimeZoneSpn.setSelection(posTime);
		//--<<
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
	}
 // =========================================================================
 // TODO onClick View
 // =========================================================================
    public void onBack(View v){
    	this.finish();
    	Vars.setPageNavi(false);
    }
    // =========================================================================
    @SuppressLint("HandlerLeak")
	public void onEdit(View v){
    	
    	if(!UpdateMode){
			
    		EmailAddET.setEnabled(true);
    		EmailAddET.setCompoundDrawablesWithIntrinsicBounds(null, null, Global.setEditIcon(this),null);
    		ProfileAddET.setEnabled(true);
    		ProfileAddET.setCompoundDrawablesWithIntrinsicBounds(null, null, Global.setEditIcon(this),null);
    		TimeZoneSpn.setEnabled(true);
    		RegionLocaleSpn.setEnabled(true);
    		EditIcon1.setVisibility(View.VISIBLE);
    		EditIcon2.setVisibility(View.VISIBLE);
    		
    		EditBtn.setText("Save");
    		UpdateMode = true;
    		
    		} else if (UpdateMode){
    			
    			Id = UserList.get(0).USERID;
    			EmailAdd = EmailAddET.getText().toString();
    			ProfileAdd = ProfileAddET.getText().toString();
    			
    			int select = TimeZoneSpn.getSelectedItemPosition();
    			TimeZone = TimeZoneVal[select];
    			
    			//RegionLocale = "auto";
    			int selectReg = RegionLocaleSpn.getSelectedItemPosition();
    			RegionLocale = RegionLocaleVal[selectReg];
    			// ---
    			
            	final ProgressDialog dialog = ProgressDialog.show(this, "Please Wait..","Processing...", true);
        		final Handler handler = new Handler() {
        		   public void handleMessage(Message msg) {
        		      dialog.dismiss();
        		      ///// 2nd if the load finish -----
        		      nextStep();
        			  ///// -----
        		      }};
        		      Thread checkUpdate = new Thread() {  
        		   public void run() {	
        			  /// 1st main activity here... -----
        			  Succeed = JsonParser.updateGeneralSet(Id,EmailAdd,ProfileAdd,TimeZone,RegionLocale);
        			  ////// -----
        		      handler.sendEmptyMessage(0);				      
        		      }};
        		      checkUpdate.start();
    		}
    }
 // =========================================================================
 // TODO Implementation
 // =========================================================================

 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	
    	UserList = Vars.getUserlistObj();
    	// -- 
		TimeZoneSel = getResources().getStringArray(R.array.TimeZone_arrays);
		TimeZoneVal = getResources().getStringArray(R.array.TimeZoneValue_arrays);
		// -- 
		RegionLocaleVal = getResources().getStringArray(R.array.RegionLocaleValue);
		RegionLocaleSel = getResources().getStringArray(R.array.RegionLocaleSelection);
		// -- 
		Title = (TextView) findViewById(R.id.tvTitle);
		Title.setText("General Settings");
		
		//-->>
		RegionLocaleSpn = (Spinner)findViewById(R.id.spnRegionLocale);
		ArrayAdapter RegionItems = new ArrayAdapter(this,android.R.layout.simple_spinner_item,RegionLocaleSel);
		RegionItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		RegionLocaleSpn.setAdapter(RegionItems);
		
			RegionLocaleSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	        	((TextView) parentView.getChildAt(0)).setTextColor(getResources().getColor(R.color.green));
	            ((TextView) parentView.getChildAt(0)).setTextSize(12);
	            
	        }
			public void onNothingSelected(AdapterView<?> arg0) {
			}});
		
			
		EmailAddET = (EditText)findViewById(R.id.etEmailAdd);
		ProfileAddET = (EditText)findViewById(R.id.etProfileAdd);
		
		TimeZoneSpn = (Spinner)findViewById(R.id.spnTimeZone);
		ArrayAdapter TimeItem = new ArrayAdapter(this,android.R.layout.simple_spinner_item,TimeZoneSel);
		//TimeItem.setDropDownViewResource(R.layout.spinner_dropdown);
		TimeItem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		TimeZoneSpn.setAdapter(TimeItem);
		
			TimeZoneSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	        	((TextView) parentView.getChildAt(0)).setTextColor(getResources().getColor(R.color.green));
	            ((TextView) parentView.getChildAt(0)).setTextSize(12);
	            
	        }
			public void onNothingSelected(AdapterView<?> arg0) {
			}});
		
		EditBtn = (Button)findViewById(R.id.btnEdit);
		
		EditIcon1 = (ImageView)findViewById(R.id.ivEdit1);
		EditIcon2 = (ImageView)findViewById(R.id.ivEdit2);
		
		// Initialization --------------------------------
		RegionLocaleSpn.setEnabled(false);
		EmailAddET.setEnabled(false);
		ProfileAddET.setEnabled(false);
		TimeZoneSpn.setEnabled(false);
		EditIcon1.setVisibility(View.INVISIBLE);
		EditIcon2.setVisibility(View.INVISIBLE);   	
    }
    // =========================================================================
    public void nextStep(){
    	if(Succeed.equals("Success")){
    		
    		UserList.get(0).EMAIL = EmailAdd;
    		UserList.get(0).USERNAME = ProfileAdd;
    		UserList.get(0).TIMEZONE = TimeZone;
    		UserList.get(0).LOCALE = RegionLocale;
    		
    		Vars.setUserListObj(UserList);
    		
    		Utils.MessageToast(this, "Success");
    		this.finish();
    		Vars.setPageNavi(false);
    		
    	} else if (Succeed.equals("Failed")){
    		
    		Utils.MessageBox(this, "Failed");
    	} else if (Succeed.equals("False")){
    		
    		Utils.MessageBox(this, "Slow or No Internet Connection");
    	}
    }
 // =========================================================================
 // TODO Final Destination
}
