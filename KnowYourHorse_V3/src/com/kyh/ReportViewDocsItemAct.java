package com.kyh;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class ReportViewDocsItemAct extends Activity{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private TextView TypeTV, SubtypeTV, StatusTV, DateTV, ReportsTV;
	// ---------------------- //
	private Bundle getValues;
	// ---------------------- //
	private String TypeStr;
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_report_view_docs_item);
        
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
 // TODO Implementation
 // =========================================================================

 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	   	
    	TypeTV = (TextView)findViewById(R.id.tvReportItemType);
    	SubtypeTV = (TextView)findViewById(R.id.tvReportItemSubtype);
    	DateTV = (TextView)findViewById(R.id.tvReportItemDate);
    	StatusTV = (TextView)findViewById(R.id.tvReportItemStatus);
    	ReportsTV = (TextView)findViewById(R.id.tvReportItemReports);
    	
    	// Initialize -------------------------------------------------------
    	getValues = getIntent().getExtras();
    	
    	//TypeStr = getValues.getString("Type");
    	
    	TypeTV.setText(getValues.getString("Type"));
    	SubtypeTV.setText(getValues.getString("Subtype"));
    	DateTV.setText(getValues.getString("Date"));
    	StatusTV.setText(getValues.getString("Status"));
    	ReportsTV.setText(getValues.getString("Reports"));
    }
 // =========================================================================
 // TODO Final Destination
}
