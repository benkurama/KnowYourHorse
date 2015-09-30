package com.kyh.objects;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import codes.libs.Utils;

public class DatePickers {
	
	private Context core;
	private String DateConcat = "";
	private EditText BOD;
 // =========================================================================
 // TODO Constructors
 // =========================================================================		
	public DatePickers(Context context,EditText DOB){
	core = context;	
	BOD = DOB;
	}
 // =========================================================================
 // TODO Methods
 // =========================================================================
	public void show(){
		
		// -- setup dialog
    	final Dialog DateDialog = new Dialog(core);
    	DateDialog.setTitle("Date Picker");
    	// -- Main Layout
		LinearLayout MainLayout = getLayoutDialog(core);
		// -- Date Picker
		final DatePicker Date = new DatePicker(core);
		// -- set content view
    	DateDialog.setContentView(MainLayout);
    	
    	// -- setup buttons layout and alignment -- >>
    	LinearLayout horiBtnLayout = new LinearLayout(core);
    	horiBtnLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    	horiBtnLayout.setOrientation(LinearLayout.HORIZONTAL);
    	horiBtnLayout.setWeightSum(100);
    	// --
    	LinearLayout.LayoutParams btnLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	btnLayout.weight = 50;
    	// -- 
    	Button SetBtn = getButtonControls("Set",btnLayout, core);
    	Button CancelBtn = getButtonControls("Cancel", btnLayout, core);
    	// --
    	horiBtnLayout.addView(SetBtn);
    	horiBtnLayout.addView(CancelBtn);
    	// -- <<
    	// -- Add all Cotrols to main layout
    	MainLayout.addView(Date);
    	MainLayout.addView(horiBtnLayout);
    	// -- Button OnClick Listener -- >>
    	SetBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				String Year = String.valueOf(Date.getYear());
				String Month = String.valueOf(Date.getMonth() + 1);
				String Day = String.valueOf(Date.getDayOfMonth());
				
				if(Month.length() < 2){
					Month = "0" + Month;
				}
				
				if(Day.length() < 2){
					Day = "0" + Day;
				}
				
				DateConcat = Year + "-" + Month+ "-" + Day;
				
				BOD.setText(DateConcat);
				DateDialog.dismiss();
			}
		});
    	// --
    	CancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				DateDialog.dismiss();
			}
		});
    	// -- <<
    	// -- Dialog show
    	DateDialog.show();
	}
 // =========================================================================
 // TODO Functions
 // =========================================================================
    public LinearLayout getLayoutDialog(Context context){
    	
    	LinearLayout layoutDialog = new LinearLayout(context);
    	//layoutDialog.setBackgroundColor(android.R.color.transparent);
    	layoutDialog.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    	layoutDialog.setGravity(Gravity.CENTER);
    	layoutDialog.setOrientation(LinearLayout.VERTICAL);
    	
    	return layoutDialog;
    }
    // =========================================================================
    public Button getButtonControls(String caption, ViewGroup.LayoutParams layout,Context context){
    	
    	Button buttonControl = new Button(context);
    	buttonControl.setText(caption);
    	buttonControl.setLayoutParams(layout);
    	
    	return buttonControl;
    }
 // =========================================================================
 // TODO Final
}
