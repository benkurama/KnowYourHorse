package com.kyh;

import com.kyh.utils.Global;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import codes.libs.controls.DialButttons;

public class ReportCreateDocsAct extends Activity{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private TextView PageTitleTV;
	private Button SpellingBtn, TrainingBtn, RacingBtn;
	private String Type = "";
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_report_create_docs);
        
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
    public void onSpelling(View v){
    	
    	Type = "Spelling / Injury";
    	
    	String[] captions = new String[]{"Horse Spelling","Horse Injury"};
    	// -- >
    	Bundle passValue1 = new Bundle();
    	passValue1.putString("type", Type);
    	passValue1.putString("subtype", "7");
    	
    	Bundle passValue2 = new Bundle();
    	passValue2.putString("type", Type);
    	passValue2.putString("subtype", "92");
    	// ---
    	Intent one = new Intent(this,ReportFormFormatAct.class);
    	one.putExtras(passValue1);
    	
    	Intent two = new Intent(this,ReportFormFormatAct.class);
    	two.putExtras(passValue2);
    	// ---
    	Intent[] activities = new Intent[]{one,two};
    	// -- <
    	DialButttons PopupButtons = new DialButttons(this, captions, activities, 2, "Spelling / Injury Menu","");
    	PopupButtons.show();
    }
    // =========================================================================
    public void onTraining(View v){
    	
    	Type = "Training";
    	
    	String[] captions = new String[]{"Horse in Training","Pre Training Report","Track Work"};
    	
    	// -- >
    	Bundle passValue1 = new Bundle();
    	passValue1.putString("type", Type);
    	passValue1.putString("subtype", "15");
    	
    	Bundle passValue2 = new Bundle();
    	passValue2.putString("type", Type);
    	passValue2.putString("subtype", "16");
    	
    	Bundle passValue3 = new Bundle();
    	passValue3.putString("type", Type);
    	passValue3.putString("subtype", "17");
    	// ---
    	Intent one = new Intent(this,ReportFormFormatAct.class);
    	one.putExtras(passValue1);
    	
    	Intent two = new Intent(this,ReportFormFormatAct.class);
    	two.putExtras(passValue2);
    	
    	Intent three = new Intent(this,ReportFormFormatAct.class);
    	three.putExtras(passValue3);
    	// ---
    	Intent[] activities = new Intent[]{one,two,three};
    	// -- <
    	DialButttons PopupButtons = new DialButttons(this, captions, activities, 3, "Training Menu","");
    	PopupButtons.show();
    }
    // =========================================================================
    public void onRacing(View v){
    	
    	Type = "Racing";
    	
    	String[] captions = new String[]{"Post Race Report","Pre Racing","Race Day Morning"};
    	
    	// -- >
    	Bundle passValue1 = new Bundle();
    	passValue1.putString("type", Type);
    	passValue1.putString("subtype", "20");
    	
    	Bundle passValue2 = new Bundle();
    	passValue2.putString("type", Type);
    	passValue2.putString("subtype", "18");
    	
    	Bundle passValue3 = new Bundle();
    	passValue3.putString("type", Type);
    	passValue3.putString("subtype", "19");
    	// ---
    	Intent one = new Intent(this,ReportFormFormatAct.class);
    	one.putExtras(passValue1);
    	
    	Intent two = new Intent(this,ReportFormFormatAct.class);
    	two.putExtras(passValue2);
    	
    	Intent three = new Intent(this,ReportFormFormatAct.class);
    	three.putExtras(passValue3);
    	// ---
    	Intent[] activities = new Intent[]{one,two,three};
    	// -- <
    	DialButttons PopupButtons = new DialButttons(this, captions, activities, 3, "Racing Menu","");
    	PopupButtons.show();
    }
 // =========================================================================
 // TODO Implementation
 // =========================================================================

    
 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	   	
    	PageTitleTV = (TextView)findViewById(R.id.tvTitle);
    	SpellingBtn = (Button)findViewById(R.id.btnSpelling);
    	TrainingBtn = (Button)findViewById(R.id.btnTraining);
    	RacingBtn = (Button)findViewById(R.id.btnRacing);
    	
    	// Initialize -------------------------------------------------------
    	
    	PageTitleTV.setText("Report");
    	
    	SpellingBtn.setTypeface(Global.getFontBebas(this));
    	TrainingBtn.setTypeface(Global.getFontBebas(this));
    	RacingBtn.setTypeface(Global.getFontBebas(this));
    }
 // =========================================================================
 // TODO Final Destination
}
