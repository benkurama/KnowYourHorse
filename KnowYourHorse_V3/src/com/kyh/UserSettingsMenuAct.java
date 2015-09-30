package com.kyh;

import com.kyh.utils.Global;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class UserSettingsMenuAct extends Activity{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private Button GeneralBtn, ChangePasswordBtn, DeleteAccountBtn;
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_user_settings_menu);
        
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
    public void onGeneral(View v){
    	
    	startActivity(new Intent(this,UserSettingsGeneralAct.class));
    }
 // =========================================================================
 // TODO Implementation
 // =========================================================================

 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	   	
    	GeneralBtn = (Button)findViewById(R.id.btnGeneral);
    	ChangePasswordBtn = (Button)findViewById(R.id.btnChangePassword);
    	DeleteAccountBtn = (Button)findViewById(R.id.btnDeleteAccount);
    	
    	// Initialize -------------------------------------------------------
    	
    	GeneralBtn.setTypeface(Global.getFontBebas(this),Typeface.BOLD);
    	ChangePasswordBtn.setTypeface(Global.getFontBebas(this),Typeface.BOLD);
    	DeleteAccountBtn.setTypeface(Global.getFontBebas(this),Typeface.BOLD);
    }
 // =========================================================================
 // TODO Final Destination
}
