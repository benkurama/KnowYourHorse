package com.kyh;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.HttpResponse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import codes.libs.NetUtils;
import codes.libs.Utils;
import codes.libs.dialogs.DatePickers;
import codes.libs.network.NetBroadcast;

import com.kyh.objects.UserObj;
import com.kyh.utils.Configs;
import com.kyh.utils.Global;
import com.kyh.utils.JsonParser;

public class UserProfileAct extends Activity implements Observer{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private TextView Title,UserNameTV, DisplayNameTV;
	private EditText DisplayName,BOD,FirstName,LastName,Description;
	private Spinner Gender;
	private Button Update, Edit, CancelBtn;
	private ImageView EditIcon, UserImageIV, BackIV;
	private HttpResponse resp;
	private Bitmap bm;
	// ---------------------- //
	private ArrayList<UserObj> UserList = new ArrayList<UserObj>();
	private ArrayAdapter itemAdap;
	// ---------------------- //
	private String Succeed,id,firstName,lastName,birthDate,gen,desc;
	private boolean UpdateMode = false;
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_user_profile);
        
        new NetBroadcast(getApplicationContext(), this);
        SetupControls();
        Vars.setPageNavi(true);
        
        initialPost();
        
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
    public void onDate(View v){
    	// --- Bangumilibs
    	DatePickers DateTest = new DatePickers(this,BOD);
    	DateTest.show();
    }
    // =========================================================================
    public void onCancel(View v){
    	
    	disableEdit();
    }
    // =========================================================================
    @SuppressLint("HandlerLeak")
	public void onEdit(View v){
    	//Utils.MessageBox(this, "Working");
    	
    	if(!UpdateMode){
    		
    		enableEdit();
    		
    	} else if (UpdateMode){
    		
    		if(NetUtils.isNetworkOn(this)){
    			
    			id = UserList.get(0).USERID;
    			firstName = FirstName.getText().toString();
    			final String fName = Utils.URLEncode(firstName);
    			
    			lastName = LastName.getText().toString();
    			final String lName = Utils.URLEncode(lastName);
    			
    			birthDate = BOD.getText().toString();
    			
    			String[] arrBdo = birthDate.split("-");
    			final String BODStr = arrBdo[0] + arrBdo[1] + arrBdo[2];
    			
    			String gender = Gender.getSelectedItem().toString();
            	if(gender.equals("Male")){
            		gen = "9";
            	}else{
            		gen = "5";
            	}
            	
            	desc = Description.getText().toString();
            	final String descript = Utils.URLEncode(desc);
    			
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
        			  /// 1st main activity here -----
        			  Succeed = JsonParser.updateProfile(id,fName,lName,BODStr,gen,descript);
        			  ////// -----
        		      handler.sendEmptyMessage(0);				      
        		      }};
        		      checkUpdate.start();
    			
    		}	
    	}
    }
 // =========================================================================
 // TODO Implementation
 // =========================================================================

 // =========================================================================
 // TODO Functions
 // =========================================================================
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void SetupControls(){
    	
    	UserList = Vars.getUserlistObj();
    	
    	Title = (TextView) findViewById(R.id.tvTitle);
    	Title.setText("Profile Page");
    	
    	UserNameTV = (TextView)findViewById(R.id.tvUsername);
    	DisplayName = (EditText) findViewById(R.id.etName);
    	Edit = (Button)findViewById(R.id.btnEdit);
    	UserImageIV = (ImageView)findViewById(R.id.ivUserImage);
    	
    	Gender = (Spinner) findViewById(R.id.spnGender);
    	String[] itemGender = new String[]{"Male","Female"};
    	itemAdap = new ArrayAdapter(this,android.R.layout.simple_spinner_item, itemGender);
    	itemAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	Gender.setAdapter(itemAdap);
    	//-->>
    	Gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        	((TextView) parentView.getChildAt(0)).setTextColor(getResources().getColor(R.color.green));
            ((TextView) parentView.getChildAt(0)).setTextSize(13);  
        }
		public void onNothingSelected(AdapterView<?> arg0) {
		}});
    	
    	//--<<
    	BOD = (EditText) findViewById(R.id.etBirth);
    	FirstName = (EditText) findViewById(R.id.etFirstName);
    	LastName = (EditText) findViewById(R.id.etLastName);
    	Description = (EditText) findViewById(R.id.etDesc);
    	EditIcon = (ImageView)findViewById(R.id.ivEditIcon);
    	//--
//    	DisplayNameTV = (TextView)findViewById(R.id.tvDisplayNamePro);
//    	DisplayNameTV.setTypeface(Global.getFontBebas(this));
    	BackIV = (ImageView)findViewById(R.id.ivBackUP);
    	CancelBtn = (Button)findViewById(R.id.btnCloseUP);
    	
    	// Initialization --------------------------------
    	
    	disableEdit();
    }
    // =========================================================================
    public void disableEdit(){
    	
    	DisplayName.setSelected(false);
    	DisplayName.setEnabled(false);
    	Gender.setEnabled(false);
    	BOD.setEnabled(false);
    	BOD.setCompoundDrawablesWithIntrinsicBounds(null, null, null,null);
    	FirstName.setEnabled(false);
    	FirstName.setCompoundDrawablesWithIntrinsicBounds(null, null, null,null);
    	LastName.setEnabled(false);
    	LastName.setCompoundDrawablesWithIntrinsicBounds(null, null, null,null);
    	Description.setEnabled(false);
    	Description.setCompoundDrawablesWithIntrinsicBounds(null, null, null,null);
    	
    	EditIcon.setVisibility(View.INVISIBLE);
    	CancelBtn.setVisibility(View.GONE);
    	BackIV.setVisibility(View.VISIBLE);
    	
    	UpdateMode = false;
    	Edit.setText("Edit");
    	Title.setText("Profile Page");
    }
    // =========================================================================
    public void enableEdit(){
    	
    	Edit.setText("Save");
    	Gender.setEnabled(true);
    	BOD.setEnabled(true);
    	BOD.setCompoundDrawablesWithIntrinsicBounds(null, null, Global.setCalendarIcon(this),null);
    	FirstName.setEnabled(true);
    	FirstName.setCompoundDrawablesWithIntrinsicBounds(null, null, Global.setEditIcon(this),null);
    	LastName.setEnabled(true);
    	LastName.setCompoundDrawablesWithIntrinsicBounds(null, null, Global.setEditIcon(this),null);
    	Description.setEnabled(true);
    	Description.setCompoundDrawablesWithIntrinsicBounds(null, null, Global.setEditIcon(this),null);
    	EditIcon.setVisibility(View.VISIBLE);
    	
    	CancelBtn.setVisibility(View.VISIBLE);
    	BackIV.setVisibility(View.GONE);
    	
    	Title.setText("Edit Mode");
		UpdateMode = true;
    }
    // =========================================================================
    public void initialPost(){
    	
    	String ImagePath = Configs.KYH_WEBSITE_LINK + UserList.get(0).PICTURE;
    	new userPics().execute(ImagePath);
    	
    	UserNameTV.setText(UserList.get(0).USERNAME);
    	DisplayName.setText(UserList.get(0).DISPLAYNAME);
    	
    	String fullname = UserList.get(0).DISPLAYNAME;
    	String[] arrName = fullname.split(" ");
    	
    	FirstName.setText(arrName[0]);
    	
    	int count = arrName.length;
    		
    	if(count >= 2){
    		LastName.setText(arrName[1]);
    	} else {
    		LastName.setText("");
    	}
    	
    	String gender = UserList.get(0).GENDER;
    	
    	if(gender.equals("9")){
    		Gender.setSelection(0);
    	} else if(gender.equals("5")){
    		Gender.setSelection(1);
    	}
    	
    	BOD.setText(UserList.get(0).BIRTHDATE);
    	Description.setText(UserList.get(0).DESCRIPTION);
    	
    }
    // =========================================================================
    public void nextStep(){
    	
    	if(Succeed.equals("Success")){
    		
    		UserList.get(0).DISPLAYNAME = firstName +" "+lastName;
    		UserList.get(0).BIRTHDATE = birthDate;
    		UserList.get(0).GENDER = gen;
    		UserList.get(0).DESCRIPTION = desc;
    		
    		Vars.setName(this, firstName +" "+lastName);
    		
    		Vars.setUserListObj(UserList);
    		
    		Utils.MessageToast(this, "Update Successful");
    		this.finish();
    		Vars.setPageNavi(false);
    	} else if (Succeed.equals("Failed")){
    		
    		Utils.MessageToast(this, "Update Failed");
    	} else if(Succeed.equals("False")){
    		Utils.MessageBox(this, "Slow or No Internet Connection");
    	}
    }
 // =========================================================================
 // TODO AsynkTask
 // =========================================================================   
    	private class userPics extends AsyncTask<String, Void, Void>{

    	Bitmap bmp;
    	// --------------------------------------------------------------
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			
		}
		// --------------------------------------------------------------
		@Override
		protected Void doInBackground(String... params) {
			
			URL url;
			
			try {
				
				url = new URL(params[0]);
				bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
		    	
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		// --------------------------------------------------------------
		@Override
		protected void onPostExecute(Void value){
			
			UserImageIV.setImageBitmap(bmp);
		}
	}
 // =========================================================================
 // TODO Final Destination
}