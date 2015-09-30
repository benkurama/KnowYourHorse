package com.kyh;

import com.kyh.utils.Global;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import codes.libs.controls.DialButttons;

public class HorseMenuAct extends Activity{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private Button HorseProfileBtn, PhotoAlbumBtn, AddOwnerBtn, ReportsBtn;
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_horse_menu);
        
        SetupControls();
        Vars.setPageNavi(true);
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
    public void onHorseProfile(View v){
    	
    	startActivity(new Intent(this,HorseProfileAct.class));
    }
    // =========================================================================
    public void onPhoto(View v){
    	
    	startActivity(new Intent(this,HorsePhotosAct.class));
    }
    // =========================================================================
    public void onOwnerAdd(View v){
    	
    }
    // =========================================================================
    public void onReports(View v){
    	
    	String[] captions = new String[]{"Make a report","View a report"};

    	Intent one = new Intent(this,ReportCreateDocsAct.class);
    	Intent two = new Intent(this,ReportCreateAudioAct.class);
    	Intent three = new Intent(this,ReportViewAudioAct.class);
    	Intent four = new Intent(this,ReportViewDocsAct.class);
    	// ---
    	Intent[] activities = new Intent[]{one, two, three, four};
    	// -- <
    	DialButttons PopupButtons = new DialButttons(this, captions, activities, 2, "Report Selection","KYHReports");
    	PopupButtons.show();
    }
 // =========================================================================
 // TODO Implementation
 // =========================================================================

    
 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	
    	HorseProfileBtn = (Button)findViewById(R.id.btnHorseProfile);
    	PhotoAlbumBtn = (Button)findViewById(R.id.btnPhotoAlbum);
    	AddOwnerBtn = (Button)findViewById(R.id.btnAddOwner);
    	ReportsBtn = (Button)findViewById(R.id.btnReports);
    	
    	// -- Initialization data ...
    	
    	HorseProfileBtn.setTypeface(Global.getFontBebas(this));
    	PhotoAlbumBtn.setTypeface(Global.getFontBebas(this));
    	AddOwnerBtn.setTypeface(Global.getFontBebas(this));
    	ReportsBtn.setTypeface(Global.getFontBebas(this));
    	
    	String type = Vars.getUserType(this);
    	if(type.equals("trainer")){
    		
    		AddOwnerBtn.setVisibility(View.VISIBLE);
    	} else if(type.equals("owner")){
    		
    		AddOwnerBtn.setVisibility(View.GONE);
    	}
    }
 // =========================================================================
 // TODO Final Destination
}
