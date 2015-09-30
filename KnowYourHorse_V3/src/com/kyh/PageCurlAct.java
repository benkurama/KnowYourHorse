package com.kyh;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class PageCurlAct extends Activity{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_page_curl);
        
        SetupControls();
    }
    // =========================================================================
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	System.gc();
    	finish();
    }

 // =========================================================================
 // TODO Implementation
 // =========================================================================

 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	   	
    }
    // =========================================================================
    /**
	 * Set the current orientation to landscape. This will prevent the OS from changing
	 * the app's orientation.
	 */
	public void lockOrientationLandscape() {
		lockOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	/**
	 * Set the current orientation to portrait. This will prevent the OS from changing
	 * the app's orientation.
	 */
	public void lockOrientationPortrait() {
		lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
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
	public void lockOrientation( int orientation ) {
		setRequestedOrientation(orientation);
	}
 // =========================================================================
 // TODO Final Destination
}
