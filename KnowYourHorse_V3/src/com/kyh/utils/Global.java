package com.kyh.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kyh.R;
import com.kyh.Vars;

public class Global {
 // =========================================================================
 	public static Drawable setCalendarIcon(Context context){ // For Calendar set icon in right side
    	
    	Drawable Icon;    	
    	Icon =  context.getResources().getDrawable(R.drawable.calendar_icon);
    	
    	return Icon;
    }
 // =========================================================================
 	public static Drawable setEditIcon(Context context){ // For Textbox set icon in right side
    	
    	Drawable Icon;    	
    	Icon =  context.getResources().getDrawable(R.drawable.edit_icon);
    	
    	return Icon;
    }
 // =========================================================================
    public static boolean checkUserdata(Context core){
    	
    	if(!TextUtils.isEmpty(Vars.getEmail(core))){
    		return true;
    	} else {
    		return false;
    	}
    }
    // =========================================================================    
    public static ViewGroup.LayoutParams getButtonLayout(){
    	
    	LinearLayout.LayoutParams Layout = new LinearLayout.LayoutParams(250, LayoutParams.WRAP_CONTENT);
    	Layout.setMargins(0, 5, 0, 5);
    	return Layout;
    }
    // =========================================================================
    public static Button getButtonControls(String caption, ViewGroup.LayoutParams layout,Context context){
    	
    	Button buttonControl = new Button(context);
    	buttonControl.setText(caption);
    	buttonControl.setLayoutParams(layout);
    	
    	return buttonControl;
    }
    // ========================================================================= 
    public static LinearLayout getLayoutDialog(Context context){
    	
    	LinearLayout layoutDialog = new LinearLayout(context);
    	//layoutDialog.setBackgroundColor(android.R.color.transparent);
    	layoutDialog.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    	layoutDialog.setGravity(Gravity.CENTER);
    	layoutDialog.setOrientation(LinearLayout.VERTICAL);
    	
    	return layoutDialog;
    }
    // =========================================================================
	public static void LogCat(String value){
		Log.i("KYH3",value);
	}
 	// =========================================================================	
	public static void lockOrientationPortrait(Context core){
		
		lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,core);
	}
	
	public static void lockOrientationLandscape(Context core){
		
		lockOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,core);
	}
	// =========================================================================
	/**
	 * Locks the orientation to a specific type.  Possible values are:
	 * <ul>
	 * <li>{@link ActivityInfo#SCREEN_ORIENTATION_BEHIND}</li>
	 * <li>{@link ActivityInfo#SCREEN_ORIENTATION_LANDSCAPE}</li>
	 * <li>{@link ActivityInfo#SCREEN_ORIENTATION_NOSENSOR}</li>
	 * <li>{@link ActivityInfo#SCREEN_ORIENTATION_PORTRAIT}</li>
	 * <li>{@link ActivityInfo#SCREEN_ORIENTATION_SENSOR}</li>
	 * <li>{@link ActivityInfo#SCREEN_ORIENTATION_UNSPECIFIED}</li>
	 * <li>{@link ActivityInfo#SCREEN_ORIENTATION_USER}</li>
	 * </ul>
	 * @param orientation
	 */
	public static void lockOrientation(int orientation, Context core) {
		
		((Activity)core).setRequestedOrientation(orientation);
	}
	// =========================================================================
	public static Typeface getFontBebas(Context core){
		
		Typeface tf = Typeface.createFromAsset(core.getAssets(), "fonts/BEBAS.TTF");
		return tf;
	}
 // =========================================================================
 // TODO Final
}
