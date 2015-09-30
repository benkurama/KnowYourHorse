package com.kyh;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import codes.libs.Utils;
import codes.libs.dialogs.DatePickers;
import codes.libs.network.NetBroadcast;

import com.kyh.R.drawable;
import com.kyh.objects.HorseObj;
import com.kyh.utils.Configs;
import com.kyh.utils.Global;
import com.kyh.utils.JsonParser;

public class HorseProfileAct extends Activity implements Observer{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private TextView Title, HorseNameTitle;
	private EditText HorseTitle,DOB,Gender,Age,Breeding,Conformation,Height,Weight,Trainer,Owner,Description;
	private Button btnUpdate,Edit;
	private Spinner TypeSpn;
	private ImageView EditIconIV,HorsePicIV;
	
	private ArrayList<HorseObj> HorseList = new ArrayList<HorseObj>();
	private String Succeed,id,title,dob,desc,breed,confor,height,weight,type,descrip;
	private boolean UpdateMode = false;
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_horse_profile);
        
        new NetBroadcast(getApplicationContext(), this);
        SetupControls();
        Vars.setPageNavi(true);
        
        HorseTitle.setText(HorseList.get(Vars.getHorsePos()).TITLE);
        HorseNameTitle.setText(HorseList.get(Vars.getHorsePos()).TITLE);
        DOB.setText(HorseList.get(Vars.getHorsePos()).DOB);
        //-->>
        //Gender.setText(HorseList.get(GloVars.getPos()).GENDER);
        String gender = HorseList.get(Vars.getHorsePos()).GENDER;
        
        if(gender.equals("0")){
        	
        	Gender.setText("Guilding");
        } else if (gender.equals("1")){
        	
        	Gender.setText("Mare");
        }
        //--<<
        Age.setText(HorseList.get(Vars.getHorsePos()).AGE);
        Breeding.setText(HorseList.get(Vars.getHorsePos()).BREEDING);
        Conformation.setText(HorseList.get(Vars.getHorsePos()).CONFORMATON);
        Height.setText(HorseList.get(Vars.getHorsePos()).HEIGHT);
        Weight.setText(HorseList.get(Vars.getHorsePos()).WEIGHT);
        Trainer.setText(HorseList.get(Vars.getHorsePos()).TRAINER);
        Owner.setText(HorseList.get(Vars.getHorsePos()).OWNER);
        Description.setText(HorseList.get(Vars.getHorsePos()).DESCRIPTION);
        //-->>
        String type = HorseList.get(Vars.getHorsePos()).TYPE;
        
        if(type.equals("2")){
        	
        	TypeSpn.setSelection(0);
        } else if (type.equals("3")){
        	
        	TypeSpn.setSelection(1);
        } else if (type.equals("4")){
        	
        	TypeSpn.setSelection(2);
        }
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
    public void onEdit(View v){
    
    	if(!UpdateMode){ // if Edit Mode is ON
    		
    		HorseTitle.setEnabled(true);
        	HorseTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, Global.setEditIcon(this),null);
        	DOB.setEnabled(true);
        	DOB.setCompoundDrawablesWithIntrinsicBounds(null, null, Global.setCalendarIcon(this),null);
        	//Gender.setEnabled(false);
        	//Age.setEnabled(false);
        	Breeding.setEnabled(true);
        	Breeding.setCompoundDrawablesWithIntrinsicBounds(null, null, Global.setEditIcon(this),null);
        	Conformation.setEnabled(true);
        	Conformation.setCompoundDrawablesWithIntrinsicBounds(null, null, Global.setEditIcon(this),null);
        	Height.setEnabled(true);
        	Height.setCompoundDrawablesWithIntrinsicBounds(null, null, Global.setEditIcon(this),null);
        	Weight.setEnabled(true);
        	Weight.setCompoundDrawablesWithIntrinsicBounds(null, null, Global.setEditIcon(this),null);
        	//Trainer.setEnabled(false);
        	//Owner.setEnabled(false);
        	Description.setEnabled(true);
        	Description.setCompoundDrawablesWithIntrinsicBounds(null, null, Global.setEditIcon(this),null);
        	TypeSpn.setEnabled(true);
        	EditIconIV.setVisibility(View.VISIBLE);
        	
        	Title.setText("Edit Mode");
        	Edit.setText("Save");
        	
	        UpdateMode = true;
	        
	    	} else if (UpdateMode){
	    		
	    		id = HorseList.get(Vars.getHorsePos()).ID;
	    		title = HorseTitle.getText().toString();
	    		final String titleS = Utils.URLEncode(title);
	    		dob = DOB.getText().toString();
	    		
	    		desc = Description.getText().toString();
	    		final String descS = Utils.URLEncode(desc);
	    		breed = Breeding.getText().toString();
	    		final String breedS = Utils.URLEncode(breed);
	    		confor = Conformation.getText().toString();
	    		
	    		height = Height.getText().toString();
	    		weight = Weight.getText().toString();
	    		
	    		String typeConv = TypeSpn.getSelectedItem().toString();
        		if(typeConv.equalsIgnoreCase("Sprinter")){
        			type = "2";
        		} else if (typeConv.equals("Middle Distance")){
        			type = "3";
        		} else if (typeConv.equals("Stayer")){
        			type = "4";
        		}
	    		
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
        			   Succeed = JsonParser.updateHorseProfile(id,titleS,dob,descS,breedS,confor,height,weight,type);
        			  ////// -----
        		      handler.sendEmptyMessage(0);				      
        		      }};
        		      checkUpdate.start();
	    }
    }
    // =========================================================================
    public void onDatePicker(View v){
    	
    	// --- Bangumilibs
    	DatePickers DateTest = new DatePickers(this,DOB);
    	DateTest.show();
    }
 // =========================================================================
 // TODO Implementation
 // =========================================================================

 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	
    	Title = (TextView) findViewById(R.id.tvTitle);
    	Title.setText("Horse Profile");
    	
    	HorseNameTitle  =(TextView)findViewById(R.id.tvHorseName);
    	HorseTitle = (EditText)findViewById(R.id.etTitle);
    	DOB = (EditText) findViewById(R.id.etDOB);
    	Gender = (EditText) findViewById(R.id.etGender);
    	Age = (EditText) findViewById(R.id.etAge);
    	Breeding = (EditText) findViewById(R.id.etBreeding);
    	Conformation = (EditText) findViewById(R.id.etConformation);
    	Height = (EditText) findViewById(R.id.etHeight);
    	Weight = (EditText) findViewById(R.id.etWeight);
    	Trainer = (EditText) findViewById(R.id.etTrainer);
    	Owner = (EditText) findViewById(R.id.etOwner);
    	Description = (EditText) findViewById(R.id.etDescription);
    	HorsePicIV = (ImageView)findViewById(R.id.ivPicHorse);
    	//Type = (EditText)findViewById(R.id.etType);
    	// -- >>
    	TypeSpn = (Spinner)findViewById(R.id.spnType);
    	String[] itemType = new String[]{"Sprinter","Middle Distance","Stayer"};
    	ArrayAdapter TypeAdap = new ArrayAdapter(this,android.R.layout.simple_spinner_item,itemType);
    	TypeAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	
    	TypeSpn.setAdapter(TypeAdap);
    	TypeSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parentView, View arg1,int arg2, long arg3) {
				((TextView) parentView.getChildAt(0)).setTextColor(getResources().getColor(R.color.green));
	            ((TextView) parentView.getChildAt(0)).setTextSize(13);
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
    	// -- <<
    	Edit = (Button)findViewById(R.id.btnEdit);
    	EditIconIV = (ImageView)findViewById(R.id.ivEditIcon);
    	
    	// Initialization --------------------------------
    	
    	HorseList = Vars.getHorseListObj();
    	
    	String pathPic = Configs.KYH_WEBSITE_LINK + HorseList.get(Vars.getHorsePos()).PICPATH;
    	
    	new horsePic().execute(pathPic);
    	
    	HorseTitle.setEnabled(false);
    	DOB.setEnabled(false);
    	Gender.setEnabled(false);
    	Age.setEnabled(false);
    	Breeding.setEnabled(false);
    	Conformation.setEnabled(false);
    	Height.setEnabled(false);
    	Weight.setEnabled(false);
    	Trainer.setEnabled(false);
    	Owner.setEnabled(false);
    	Description.setEnabled(false);
    	TypeSpn.setEnabled(false);
    	EditIconIV.setVisibility(View.INVISIBLE);
    	// ---
    	String type = Vars.getUserType(this);
    	if(type.equals("trainer")){
    		Edit.setVisibility(View.VISIBLE);
    	} else if(type.equals("owner")){
    		Edit.setVisibility(View.INVISIBLE);
    	}
    	// ---
    }
    // =========================================================================
    public void nextStep(){
    	
    	if(Succeed.equals("Success")){
    		
    		HorseList.get(Vars.getHorsePos()).TITLE = title;
    		HorseList.get(Vars.getHorsePos()).DOB = dob;
    		HorseList.get(Vars.getHorsePos()).DESCRIPTION = desc;
    		HorseList.get(Vars.getHorsePos()).BREEDING = breed;
    		HorseList.get(Vars.getHorsePos()).CONFORMATON = confor;
    		HorseList.get(Vars.getHorsePos()).HEIGHT = height;
    		HorseList.get(Vars.getHorsePos()).WEIGHT = weight;
    		HorseList.get(Vars.getHorsePos()).TYPE = type;
    		
    		Vars.setHorseListObj(HorseList);
    		
    		Utils.MessageToast(this, "Update Success");
    		this.finish();
    		Vars.setPageNavi(false);
    		
    	} else if (Succeed.equals("Failed")){
    		
    		Utils.MessageBox(this, "Update Failed");
    	} else if (Succeed.equals("False")){
    		
    		Utils.MessageBox(this, "Slow or No Internet Connection");
    	}
    }
    // =========================================================================
	private class horsePic extends AsyncTask<String, Void, Void>{

		Bitmap bipmap;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			
		}
		// --------------------------------------------------------------
		@Override
		protected Void doInBackground(String... params) {
			
			URL url;
			String pathName = "";
			
			try {
				
				pathName = params[0];
				
				if(!(params[0].length() == 36)){
					
					url = new URL(params[0]);
					bipmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
				} else {
					bipmap = BitmapFactory.decodeResource(getResources(),R.drawable.horsenopic);
				}
				
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
			HorsePicIV.setImageBitmap(bipmap);
		}
	}
 // =========================================================================
 // TODO Final Destination
}
