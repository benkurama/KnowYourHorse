package com.kyh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import codes.libs.Utils;
import codes.libs.network.NetBroadcast;

import com.kyh.objects.HorseSearchObj;
import com.kyh.utils.JsonParser;

public class HorseSearchAct extends Activity implements OnItemClickListener, Observer{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private ListView HorseListLV;
	private TextView SearchTitleTV,DisplayPageTV;
	private EditText SearchTextET;
	// ---------------------- //
	private ArrayList<HorseSearchObj> HorseSearchList = new ArrayList<HorseSearchObj>();
	private ArrayList<HashMap<String, String>> Horse = new ArrayList<HashMap<String,String>>();
	// ---------------------- //
	private int CurrPage = 0;
	private int MaxPage = 0;
	private String Keyword = "";
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_horse_search);
        
        new NetBroadcast(getApplicationContext(), this);
        SetupControls();
        Vars.setPageNavi(true);
        
        getSearchHorse(Keyword,CurrPage);
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
    public void onPrev(View v){
    	
    	if(CurrPage > 0){
    		
    		CurrPage -= 1;
    		getSearchHorse(Keyword,CurrPage);
    	}
    }
    // =========================================================================
    public void onNext(View v){
    	
    	if(CurrPage < (MaxPage - 1)){
    		
    		CurrPage += 1;
    		getSearchHorse(Keyword,CurrPage);
    	}
    }
    // =========================================================================
    public void onSearch(View v){
    	
    	Keyword = SearchTextET.getText().toString();
    	
    	if (Keyword.length() != 0){
    		
    		CurrPage = 0;
        	getSearchHorse(Keyword,CurrPage);
    		
    	} else {
    		getSearchHorse(Keyword,CurrPage);
    	}
    }
 // =========================================================================
 // TODO Implementation
 // =========================================================================
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		
		Vars.setHorseSearchPos(pos);
		startActivity(new Intent(this,HorseSearchViewAct.class));
	}
 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	   	
    	HorseListLV = (ListView)findViewById(R.id.lvHorse);
    	HorseListLV.setOnItemClickListener(this);
    	
    	SearchTitleTV = (TextView)findViewById(R.id.tvtitleSearch);
    	DisplayPageTV = (TextView)findViewById(R.id.tvDisplayPage);
    	SearchTextET = (EditText)findViewById(R.id.etSearch);
    	
    	// Initialize Data ......
    	SearchTitleTV.setText("Search a Horse");
    }
    // =========================================================================
    @SuppressLint("HandlerLeak")
	public void getSearchHorse(final String key,final int num){
    	
    	HorseSearchList.clear();
    	Horse.clear();
    	
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
        			   HorseSearchList = JsonParser.getHorseSearch(key,num);
        			  ////// -----
        		      handler.sendEmptyMessage(0);				      
        		      }};
        		      checkUpdate.start();
    }
    // =========================================================================
    public void nextStep(){
    	// -- >>
    	if(HorseSearchList.get(0).SUCCESS.equals("1")){
    		
    		MaxPage = Integer.parseInt(HorseSearchList.get(0).MAXPAGE) ;
        	
        	int CurrentPage =  CurrPage + 1;
        	
        	String display = "Display " +CurrentPage+ " of " +MaxPage;
        	DisplayPageTV.setText(display);
        	
        	Vars.setHorseSearchList(HorseSearchList);
        	// -- <<
        	for(HorseSearchObj horselist : HorseSearchList){
        		
        		String gender = "";
        		HashMap<String, String > row = new HashMap<String, String>();
        		
        		if(horselist.GENDER.equals("1")){
        			
        			gender = "Mare";
        		} else {
        			gender = "Gelding";
        		}
        		
        		row.put("Title",horselist.TITLE);
        		row.put("Old",horselist.AGE +" yo");
        		row.put("Gender", gender);
        		row.put("Weight", horselist.WEIGHT + " kg");
        		row.put("Height", horselist.HEIGHT + " cm");
        		Horse.add(row);
        	}
        	
        	String[] Columns = new String[]{"Title","Old","Gender","Weight","Height"};
        	int[] Controls = new int[]{R.id.tvTitleHorse,R.id.tvHorseYear,R.id.tvHorseGender,R.id.tvHorseWeight,R.id.tvHorseHeight};
        	
        	ListAdapter adapter = new SimpleAdapter(this,Horse, R.layout.horse_search_row, Columns, Controls);
        	HorseListLV.setAdapter(adapter);
        	
    	} else if (HorseSearchList.get(0).SUCCESS.equals("0")){
    		
    		String display = "Display " +"0"+ " of " +"0";
        	DisplayPageTV.setText(display);
        	
    		HorseListLV.setAdapter(null);
    	} else if (HorseSearchList.get(0).SUCCESS.equals("False")){
    		
    		String display = "Display " +"0"+ " of " +"0";
        	DisplayPageTV.setText(display);
        	
    		HorseListLV.setAdapter(null);
    		Utils.MessageBox(this, "Slow or No Internet Connection");
    	}
    	
    }
 // =========================================================================
 // TODO Final Destination
}
