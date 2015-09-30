package com.kyh;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import codes.libs.NetUtils;
import codes.libs.Utils;

import com.kyh.objects.HorseObj;
import com.kyh.utils.JsonParser;

public class HorseListAct extends Activity implements OnItemClickListener{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private TextView Title;
	private ListView HorseView;
	
	// ---------------------- //
	private ArrayList<HorseObj> HorseList = new ArrayList<HorseObj>();
	private ArrayList<HashMap<String, String>> horse = new ArrayList<HashMap<String,String>>();
	private HashMap<String, String> row;
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_horse_list);
        
        SetupControls();
        Vars.setPageNavi(true);
    }
    // =========================================================================
    @Override
	protected void onResume() {
		super.onResume();
		// -- calling VArs object
		HorseList = Vars.getHorseListObj();
		
		if(HorseList == null){
			handleHorseLoading();
		} else {
			
			if(Vars.getRefresh()){
				handleHorseLoading();
				Vars.setRefresh(false);
			} else{
				callHorseListView();
			}
		}
		// --
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
    public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
    	
    	Vars.setHorsePos(pos);
    	
    	startActivity(new Intent(this,HorseMenuAct.class));
    }
 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	
    	Title = (TextView) findViewById(R.id.tvTitle);
    	Title.setText("Horse List");
    	
    	HorseView = (ListView)findViewById(R.id.lvHorseList);   	
    }
    // =========================================================================
    public void callHorseListDialog(){
    	
    final String userid = Vars.getUserID(this);
    final Context Core = this;
    	
//	HorseList.clear();
//	horse.clear();
    
	final ProgressDialog dialog = ProgressDialog.show(this, "Please Wait..","Processing...", true);
    		final Handler handler = new Handler() {
    		   public void handleMessage(Message msg) {
    		      dialog.dismiss();
    		      ///// 2nd if the load finish -----
    		      callHorseListView();
    			  ///// -----
    		      }};
    		      Thread checkUpdate = new Thread() {  
    		   public void run() {	
    			  /// 1st main activity here -----
    			   HorseList = JsonParser.getOwnedHorse(userid, Core);  
    			  ////// -----
    		      handler.sendEmptyMessage(0);				      
    		      }};
    		      checkUpdate.start();
    }
    // =========================================================================
    public void callHorseListView(){
    	
    	horse.clear();
    	
    	if(HorseList.get(0).SUCCESS.equals("1")){
        	
            Vars.setHorseListObj(HorseList);
            //-->
            for(HorseObj listHorse: HorseList){
            	
            	row = new HashMap<String, String>();
            	row.put("title", listHorse.TITLE);
            	
            	horse.add(row);
            }
            //--<
            ListAdapter adapter = new SimpleAdapter(this,horse,R.layout.horse_row,new String[] {"title"},new int[] {R.id.tvTitle});
            HorseView.setAdapter(adapter);
            HorseView.setOnItemClickListener(this);
            
        	}else if(HorseList.get(0).SUCCESS.equals("0")){
        		
        		Utils.MessageBox(this, "There are no horse/s affiliated with this account");
        	} else if(HorseList.get(0).SUCCESS.equals("false")){
        		
        		Utils.MessageBox(this, "Slow or No Internet");
        	}
    	
    }
    // =========================================================================
    public void handleHorseLoading(){
    	
    	Handler handler = new Handler();
		if(NetUtils.isNetworkOn(this)){
			// calling network state
			handler.postDelayed(new Runnable() { public void run() {callHorseListDialog();}}, 250);
		} else {
			
			Utils.MessageBox(this, "Slow or No Internet Connection");
		}
    }
 // =========================================================================
 // TODO Final Destination
}
