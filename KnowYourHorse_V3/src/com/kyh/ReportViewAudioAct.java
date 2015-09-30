package com.kyh;

import java.util.ArrayList;
import java.util.HashMap;

import com.kyh.objects.HorseObj;
import com.kyh.objects.KYHDatabase;
import com.kyh.objects.RecordObj;

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

public class ReportViewAudioAct extends Activity implements OnItemClickListener{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private ListView RecordsLV;
	// ---------------------- //
	private KYHDatabase Database;
	// ---------------------- //
	private ArrayList<RecordObj> RecordList = new ArrayList<RecordObj>();
	private ArrayList<HorseObj> HorseList = new ArrayList<HorseObj>();
	private ArrayList<HashMap<String, String>> HashRecords = new ArrayList<HashMap<String,String>>();
	private HashMap<String, String> row;
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_report_view_audio);
        
        SetupControls();
        Vars.setPageNavi(true);
        
        getRecordList();
        // --
        setRecordData();
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
		// --
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
    	
    	Bundle passValue = new Bundle();
    	passValue.putString("recordname", RecordList.get(pos).RECORDNAME);
    	passValue.putString("description", RecordList.get(pos).DESCRIPTION);
    	passValue.putString("path",RecordList.get(pos).PATH);
    	
    	Intent PlaybackIntent = new Intent(this,RecordPlaybackAct.class);
    	PlaybackIntent.putExtras(passValue);
		
    	startActivity(PlaybackIntent);
	}
 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	
    	RecordsLV = (ListView)findViewById(R.id.lvRecordList);
    	RecordsLV.setOnItemClickListener(this);
    	
    	// Initialize -------------------------------------------------------
    	HorseList = Vars.getHorseListObj();
    }
    // =========================================================================
    public void getRecordList(){
    	
    	int userID = Integer.parseInt(Vars.getUserID(this)) ;
    	int horseID = Integer.parseInt(HorseList.get(Vars.getHorsePos()).ID) ;
    	
    	Database = new KYHDatabase(this);
    	Database.openToRead();
    	RecordList = Database.gelAllRecords(userID, horseID);
    	Database.close();
    }
    // =========================================================================
    public void setRecordData(){
    	
    	for(RecordObj listRecords: RecordList){
    		
    		row = new HashMap<String, String>();
    		row.put("RecordName", listRecords.RECORDNAME);
    		row.put("Duration", listRecords.DURATION);
    		row.put("Date", listRecords.DATE);
    		row.put("Status",listRecords.STATUS);
    		// --
    		HashRecords.add(row);
    	}
    	
    	String[] Captions = new String[]{"RecordName","Duration","Date","Status"};
    	int[] Controls = new int[]{R.id.tvRecordNameR,R.id.tvDurationR,R.id.tvDateR,R.id.tvStatusR};
    	
    	ListAdapter adapter = new SimpleAdapter(this,HashRecords,R.layout.records_row,Captions,Controls);
    	RecordsLV.setAdapter(adapter);
    }
 // =========================================================================
 // TODO Final Destination
}
