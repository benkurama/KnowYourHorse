package com.kyh;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class WebsiteAct extends Activity{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private WebView FansiteWV;
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_website);
        
        SetupControls();
    }
 // =========================================================================
 // TODO onClick View
 // =========================================================================
    public void onBack(View v){
    	this.finish();
    }
 // =========================================================================
 // TODO Implementation
 // =========================================================================

 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	   	
    	FansiteWV = (WebView)findViewById(R.id.wvFansite);
    	
    	// -- Initialize data ...
    	Bundle GetValue = getIntent().getExtras();
    	String value = GetValue.getString("value");
    	
    	if(value.equals("facebook")){
    		
    		FansiteWV.loadUrl("http://www.facebook.com/plugins/likebox.php?href=http%3A%2F%2Fwww.facebook.com%2Fpages%2FKnow-Your-Horse%2F130076287065974&amp;width=292&amp;colorscheme=light&amp;show_faces=false&amp;stream=false&amp;header=false&amp;height=80");
    	} else if(value.equals("twitter")){
    		
    		FansiteWV.loadUrl("https://twitter.com/intent/follow?original_referer=http%3A%2F%2Fknowyourhorse.com.au%2F&screen_name=knowyourhorse&tw_p=followbutton&variant=2.0");
    	}
    }
 // =========================================================================
 // TODO Final Destination
}
