package com.kyh;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.kyh.objects.HorseObj;
import com.kyh.objects.KYHDatabase;
import com.kyh.objects.ReportListObj;

public class ReportViewDocsAct extends Activity implements OnItemClickListener{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private ListView ReportLV;
	// ---------------------- //
	private KYHDatabase Database;
	private ArrayList<ReportListObj> ReportList = new ArrayList<ReportListObj>();
	private ArrayList<HashMap<String, String>> HashReport = new ArrayList<HashMap<String, String>>();
	private HashMap<String, String> row;
	private ArrayList<HorseObj> HorseList = new ArrayList<HorseObj>();
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_report_view_docs);
        
        SetupControls();
        Vars.setPageNavi(true);
        // --
        getReportList();
        // -- 
        setReportData();
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
    @Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
    	
    	Bundle passValues = new Bundle();
    	passValues.putString("Type", ReportList.get(pos).TYPE);
    	passValues.putString("Subtype", ReportList.get(pos).SUBTYPE);
    	passValues.putString("Status", ReportList.get(pos).STATUS);
    	passValues.putString("Date", ReportList.get(pos).DATE);
    	passValues.putString("Reports", ReportList.get(pos).REPORT);
    	
    	Intent viewValues = new Intent(this,ReportViewDocsItemAct.class);
    	viewValues.putExtras(passValues);
    	
		startActivity(viewValues);
    	//startActivity(new Intent(this,ReportViewDocsItemAct.class));
	}
 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	   	
    	ReportLV = (ListView)findViewById(R.id.lvReportList);
    	
    	
    	// Initialize -------------------------------------------------------
    	HorseList = Vars.getHorseListObj();
    	ReportLV.setOnItemClickListener(this);
    }
    // =========================================================================
    public void getReportList(){
    	
    	int userID = Integer.parseInt(Vars.getUserID(this));
    	int horseID = Integer.parseInt(HorseList.get(Vars.getHorsePos()).ID);
    	
    	Database = new KYHDatabase(this);
    	Database.openToRead();
    	ReportList = Database.getAllReports(userID, horseID);
    	Database.close();
    }
    // =========================================================================
    public void setReportData(){
    	
    	for(ReportListObj listReports: ReportList){
    		
    		row = new HashMap<String, String>();
    		row.put("Type", listReports.TYPE);
    		row.put("Subtype", listReports.SUBTYPE);
    		row.put("Date", listReports.DATE);
    		row.put("Status", listReports.STATUS);
    		// --
    		HashReport.add(row);
    	}
    	
    	String[] Captions = new String[]{"Type","Subtype","Date","Status"};
    	int[] Controls = new int[]{R.id.tvReportNameR, R.id.tvSubtypeR, R.id.tvReportDateR, R.id.tvReportStatusR};
    	
    	ListAdapter adapter = new SimpleAdapter(this,HashReport,R.layout.reports_row,Captions,Controls);
    	ReportLV.setAdapter(adapter);
    }
 // =========================================================================
 // TODO Final Destination
}