package com.kyh;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.kyh.objects.ControlMaker;
import com.kyh.objects.HorseObj;
import com.kyh.objects.KYHDatabase;
import com.kyh.objects.ReportObj;
import com.kyh.utils.Global;
import com.kyh.utils.JsonParser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import codes.libs.Utils;
import codes.libs.dialogs.DatePickers;

public class ReportFormFormatAct extends Activity{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private TextView Title;
	private LinearLayout ChildLayout;
	private Button Update;
	// ---------------------- //
	private KYHDatabase Database;
	private ArrayList<ReportObj> ReportList = new ArrayList<ReportObj>();
	private ArrayList<HorseObj> HorseList = new ArrayList<HorseObj>();
	// ---------------------- //
	private LinearLayout[] MainLayout;
    private LinearLayout[] FirstLayout;
    private LinearLayout[] SecondLayout;
    // ---------------------- //
    private TextView[] title;
    private EditText[] Textbox;
    private Spinner[] SelectSpin;
    private Spinner[][] DateSpin;
    private android.widget.CheckBox[] CheckBox;
    private android.widget.CheckBox[][] MultiCheck;
    // ---------------------- //
    private Integer turn = 1;
    private String Index;
    private String Type;
    private String Reports = "";
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_report_form_format);
        
        
        SetupControls();
        Vars.setPageNavi(true);
        
      //--> Calling list of horse owned by User
  		Handler handler = new Handler();
  		handler.postDelayed(new Runnable() { public void run() {callLoadingProgress();}}, 250);
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
    public void onUpdate(View v){
    	
    	postUpdate();
    }
 // =========================================================================
 // TODO Implementation
 // =========================================================================

 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	
    	Bundle getValue = getIntent().getExtras();
    	Type = getValue.getString("type");
        Index = getValue.getString("subtype");
        
        Title = (TextView) findViewById(R.id.tvTitle);
    	Update = (Button) findViewById(R.id.btnUpdate);
    	
    	// -- Initialize data ...
    	HorseList = Vars.getHorseListObj();
    	
    	Title.setText("Report Page");
    	Update.setVisibility(View.INVISIBLE);
    	ChildLayout = (LinearLayout)findViewById(R.id.llChildLayout);
    	
    }
    // =========================================================================
    @SuppressLint("HandlerLeak")
	public void callLoadingProgress(){
    	
        final ProgressDialog dialog = ProgressDialog.show(this, "Please Wait..","Processing...", true);
		final Handler handler = new Handler() {
		   public void handleMessage(Message msg) {
		      dialog.dismiss();
		      ///// 2nd if the load finish -----
		      getDataObject();
			  ///// -----
		      }};
		      Thread checkUpdate = new Thread() {  
		   public void run() {	
			  /// 1st main activity here... -----
			   ReportList = JsonParser.getHorseReport(Index, getBaseContext());
			  ////// -----
		      handler.sendEmptyMessage(0);				      
		      }};
		      checkUpdate.start();
    }
    // =========================================================================
    public void getDataObject(){
    	
    	Update.setVisibility(View.VISIBLE);
    	int size = ReportList.size();
    	int items = 10;
    	
        MainLayout = new LinearLayout[size];
        FirstLayout = new LinearLayout[size];
        SecondLayout = new LinearLayout[size];
        
        title  = new TextView[size];
        Textbox  = new EditText[size];
        SelectSpin = new Spinner[size];
        MultiCheck = new CheckBox[size][items];
        CheckBox = new CheckBox[size];
        DateSpin = new Spinner[size][items];
        // --------------------------------------------------------------
        for(int x = 0; x < size ; x++){
        	
        	MainLayout[x] = ControlMaker.getMainLayout(this);
        	FirstLayout[x] = ControlMaker.getFirstLayout(this);
        	SecondLayout[x] = ControlMaker.getSecondLayout(this);
        	
        	title[x] = ControlMaker.getTextview(this);
	        title[x].setText(ReportList.get(x).LABEL+": ");
	        
	        // ------------- Step 1 >>>
	        if(ReportList.get(x).TYPE.equals("text")){
		        Textbox[x] = ControlMaker.getTextBox(this);
		        Textbox[x].setHint(ReportList.get(x).TYPE);
		        
		        SecondLayout[x].addView(Textbox[x]);
		        //RelRight[x].addView(Textbox[x]);
	        }else if(ReportList.get(x).TYPE.equals("select") || ReportList.get(x).TYPE.equals("radio")){
	        	
	        	postSelection(x);
	        }else if(ReportList.get(x).TYPE.equals("textarea")){
	        	
	        	Textbox[x] = ControlMaker.getTextArea(this);
		        Textbox[x].setHint(ReportList.get(x).TYPE);
		        
		        SecondLayout[x].addView(Textbox[x]);
		        
	        }else if(ReportList.get(x).TYPE.equals("date")){
	        	
	        	SecondLayout[x].setOrientation(LinearLayout.HORIZONTAL);
	        	
	        	final int eks = x;
	        	//--
	        	Textbox[x] = ControlMaker.getTextBox(this);
	        	Textbox[x].setFocusable(false);
	        	Textbox[x].setClickable(true);
	        	Textbox[x].setHint("yyyy-mm-dd");
	        	Textbox[x].setCompoundDrawablesWithIntrinsicBounds(null, null, Global.setCalendarIcon(this), null);
	        	
	        	Textbox[x].setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						
						callDatePicker(Textbox[eks]);
					}
				});
	        	
	        	SecondLayout[x].addView(Textbox[x]);
	        	
	        }else if(ReportList.get(x).TYPE.equals("multi_checkbox") || ReportList.get(x).TYPE.equals("multiselect")){
	        	
	        	postMultiCheckbox(x);
	        }else if(ReportList.get(x).TYPE.equals("checkbox")){
	        	
	        	postChecbox(x);
	        }
	        
	        // ------------- Step 2 >>>
	        ChildLayout.setOrientation(LinearLayout.VERTICAL);
	        ChildLayout.addView(MainLayout[x]);
	        
	        MainLayout[x].addView(FirstLayout[x]);
	        MainLayout[x].addView(SecondLayout[x]);
	        
	        FirstLayout[x].addView(title[x]);
        }
    	// --------------------------------------------------------------
    }
    // =========================================================================
    public void postSelection(int x){
    	
		String[] value = ReportList.get(x).SELECTION.split(",");
    	SelectSpin[x] = ControlMaker.getSpinner(this);
    	ArrayAdapter itemAdap = new ArrayAdapter(this,android.R.layout.simple_spinner_item, value);
    	itemAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	SelectSpin[x].setAdapter(itemAdap);
    	
    	SelectSpin[x].setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {}
			public void onNothingSelected(AdapterView<?> arg0) {}	});
    	
    	SecondLayout[x].addView(SelectSpin[x]);
    }
    // =========================================================================
    public void callDatePicker(EditText text){
    	
    	DatePickers DateTest = new DatePickers(this,text);
    	DateTest.show();
    }
    // ========================================================================= 
    public void postMultiCheckbox(Integer x){
    	
    	String[] value = ReportList.get(x).SELECTION.split(",");
    	
    	for(int z = 0; z < value.length; z++){
    		MultiCheck[x][z] = ControlMaker.getCheckBox(this);
    		MultiCheck[x][z].setText(value[z].toString());
    		
    		SecondLayout[x].addView(MultiCheck[x][z]);
    	}
    }
    // =========================================================================  
    public void postChecbox(Integer x){
    	
    	if(Index.equals("18")){
    		
        	title[x].setText("");
        	title[x].setVisibility(View.GONE);
        	
        	CheckBox[x] = ControlMaker.getCheckBox(this);
        	CheckBox[x].setChecked(true);
        	CheckBox[x].setText(ReportList.get(x).LABEL);
        	
        	SecondLayout[x].addView(CheckBox[x]);
    	} else {
        	CheckBox[x] = ControlMaker.getCheckBox(this);
        	CheckBox[x].setChecked(true);
        	CheckBox[x].setText(ReportList.get(x).LABEL);
        	
        	SecondLayout[x].addView(CheckBox[x]);
    	}
    }
    // =========================================================================
    public void postUpdate(){
    	
    	
    	//String output = "";
    	int size = ReportList.size();
    	
    	for (int x = 0; x < size; x++){
		//-->
        if(ReportList.get(x).TYPE.equals("text")){
        	Reports += ReportList.get(x).LABEL + "\n";
        	Reports += Textbox[x].getText().toString() + "\n\n";
        	
        }else if(ReportList.get(x).TYPE.equals("select") || ReportList.get(x).TYPE.equals("radio")){
        	
        	Reports += ReportList.get(x).LABEL + "\n";
        	Reports += SelectSpin[x].getSelectedItem().toString() + "\n\n";
        }else if(ReportList.get(x).TYPE.equals("textarea")){
        	
        	Reports += ReportList.get(x).LABEL + "\n";
        	Reports += Textbox[x].getText().toString() + "\n\n";
        }else if(ReportList.get(x).TYPE.equals("date")){
        	
        	Reports += ReportList.get(x).LABEL + "\n";
        	Reports += Textbox[x].getText().toString() + "\n\n";
        	
        }else if(ReportList.get(x).TYPE.equals("multi_checkbox")){
        	
        	Reports += ReportList.get(x).LABEL + "\n";
        	String[] value = ReportList.get(x).SELECTION.split(",");
        	
        	for(int z = 0; z < value.length; z++){
        		if (MultiCheck[x][z].isChecked()){
        			
        			Reports += MultiCheck[x][z].getText().toString() + " ";
        		} else {
        			Reports += "No ";
        		}
        	}
        	
        	Reports += "\n\n";
        	
        }else if(ReportList.get(x).TYPE.equals("checkbox")){
        	
        	Reports += ReportList.get(x).LABEL + "\n";
        	if(CheckBox[x].isChecked()){
        		Reports += CheckBox[x].getText() + "\n\n";
        	}
        }
        //--<
    	}
    	// Setup Dialog Preview
    	final Dialog dialogSpell = new Dialog(this);
    	dialogSpell.setTitle("Report Preview");
    	
    	ScrollView mScrollView = new ScrollView(this);
    	ViewGroup.LayoutParams Layout = new ViewGroup.LayoutParams(270, LayoutParams.WRAP_CONTENT);
    	mScrollView.setLayoutParams(Layout);
    	
    	LinearLayout linear = new LinearLayout(this);
    	linear.setOrientation(LinearLayout.VERTICAL);
    	// -- >>
    	TextView Text = new TextView(this);
    	Text.setGravity(Gravity.LEFT);
    	Text.setTextSize(15);
    	Text.setText(Reports);
    	
    	LinearLayout linear2 = new LinearLayout(this);
    	linear2.setOrientation(LinearLayout.HORIZONTAL);
    	
    	ViewGroup.LayoutParams btnLayout = Global.getButtonLayout();
    	Button btnHT = Global.getButtonControls("Update",btnLayout,this);
    	Button btnHT2 = Global.getButtonControls("Cancel",btnLayout,this);
    	// --
    	btnHT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveReport(Reports);
				dialogSpell.dismiss();
			}
		});
    	
    	btnHT2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialogSpell.dismiss();
			}
		});
    	// --
    	linear2.addView(btnHT,120,LayoutParams.WRAP_CONTENT);
    	linear2.addView(btnHT2,120,LayoutParams.WRAP_CONTENT);
    	// -- <<
    	linear.addView(Text, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	linear.addView(linear2, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	
    	mScrollView.addView(linear);
    	
    	//--> setup main dialog layout
    	LinearLayout layoutSpellDialog = Global.getLayoutDialog(this);
    	dialogSpell.setContentView(layoutSpellDialog);
    	layoutSpellDialog.addView(mScrollView);
    	//--<
    	dialogSpell.show();
    }
    // =========================================================================
    @SuppressLint("SimpleDateFormat")
	public void saveReport(String reports){
    	// --
    	int userid = Integer.parseInt(Vars.getUserID(this)); 
    	int horseid  = Integer.parseInt(HorseList.get(Vars.getHorsePos()).ID);
    	String Types = Type;
    	
    	String Subtype = getSubtype(Index);
    	
    	String Status = "Pending";
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy");
    	String Date = sdf.format(new Date());
    	// --
    	Database = new KYHDatabase(this);
    	Database.openToWrite();
    	Database.insertReport(userid, horseid, Types, Subtype, Status, Date, reports);
    	Database.close();
    	
    	Utils.MessageToast(this, "Save Success");
    	this.finish();
    	Vars.setPageNavi(false);
    }
    // =========================================================================
    public String getSubtype(String index){
    	
    	String Subtype = "";
    	
    	if(index.equals("7")){
    		Subtype = "Horse Spelling";
    	} else if(index.equals("92")){
    		Subtype = "Horse Injury";
    		
    	} else if(index.equals("15")){
    		Subtype = "Horse in Training";
    	} else if(index.equals("16")){
    		Subtype = "Pre Training Report";
    	} else if(index.equals("17")){
    		Subtype = "Track work";
    		
    	} else if(index.equals("20")){
    		Subtype = "Post Race Report";
    	} else if(index.equals("19")){
    		Subtype = "Pre Racing";
    	} else if(index.equals("18")){
    		Subtype = "Race Day Morning";
    	}
    
    	return Subtype;
    }
 // =========================================================================
 // TODO Final Destination
}
