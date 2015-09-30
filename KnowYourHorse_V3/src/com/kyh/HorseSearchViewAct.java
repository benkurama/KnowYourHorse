package com.kyh;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.kyh.objects.HorseSearchObj;
import com.kyh.utils.Configs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class HorseSearchViewAct extends Activity{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private TextView Title,HorseNameTV;
	private EditText HorseTitle,DOB,Gender,Age,Breeding,Conformation,Height,Weight,Trainer,Owner,Description;
	private Spinner TypeSpn;
	private ImageView EditIconIV,HorsePicIV;
	
	private ArrayList<HorseSearchObj> HorseSearchList = new ArrayList<HorseSearchObj>();
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_horse_search_view);
        
        SetupControls();
        Vars.setPageNavi(true);
        
        HorseTitle.setText(HorseSearchList.get(Vars.getHorseSearchPos()).TITLE);
        HorseNameTV.setText(HorseSearchList.get(Vars.getHorseSearchPos()).TITLE);
        
        DOB.setText(HorseSearchList.get(Vars.getHorseSearchPos()).DOB);
        //-->>
        //Gender.setText(HorseList.get(GloVars.getPos()).GENDER);
        String gender = HorseSearchList.get(Vars.getHorseSearchPos()).GENDER;
        
        if(gender.equals("0")){
        	
        	Gender.setText("Guilding");
        } else if (gender.equals("1")){
        	
        	Gender.setText("Mare");
        }
        
        
        //--<<
        Age.setText(HorseSearchList.get(Vars.getHorseSearchPos()).AGE);
        Breeding.setText(HorseSearchList.get(Vars.getHorseSearchPos()).BREEDING);
        Conformation.setText(HorseSearchList.get(Vars.getHorseSearchPos()).CONFORMATON);
        Height.setText(HorseSearchList.get(Vars.getHorseSearchPos()).HEIGHT);
        Weight.setText(HorseSearchList.get(Vars.getHorseSearchPos()).WEIGHT);
        Trainer.setText(HorseSearchList.get(Vars.getHorseSearchPos()).TRAINER);
        Owner.setText(HorseSearchList.get(Vars.getHorseSearchPos()).OWNER);
        Description.setText(HorseSearchList.get(Vars.getHorseSearchPos()).DESCRIPTION);
        //-->>
        String type = HorseSearchList.get(Vars.getHorseSearchPos()).TYPE;
        
        if(type.equals("2")){
        	
        	TypeSpn.setSelection(0);
        } else if (type.equals("3")){
        	
        	TypeSpn.setSelection(1);
        } else if (type.equals("4")){
        	
        	TypeSpn.setSelection(2);
        }
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
 // TODO onClick View
 // =========================================================================
    public void onBack(View v){
    	this.finish();
    	Vars.setPageNavi(false);
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
    	
    	HorseTitle = (EditText)findViewById(R.id.etTitle);
    	HorseNameTV = (TextView)findViewById(R.id.tvHorseName2);
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
    	HorsePicIV = (ImageView)findViewById(R.id.ivPicHorse2);
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
    	
    	// Initialization --------------------------------
    	HorseSearchList = Vars.getHorseSearchListObj();
    	
    	String path = Configs.KYH_WEBSITE_LINK + HorseSearchList.get(Vars.getHorseSearchPos()).PICPATH;
    	new horsePic().execute(path);
    	
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
