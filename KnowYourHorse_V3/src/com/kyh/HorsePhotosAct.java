package com.kyh;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import codes.libs.NetUtils;
import codes.libs.Utils;

import com.kyh.objects.GalleryAdapter;
import com.kyh.objects.HorseObj;
import com.kyh.objects.PhotoObj;
import com.kyh.utils.JsonParser;
import com.kyh.utils.Notifier;
import com.kyh.utils.PhotoUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class HorsePhotosAct extends Activity implements OnItemClickListener{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private TextView Title;
	private ImageView pic;
	private Bitmap mapbit;
	// ---------------------- //
	private Gallery gallery;
	private ArrayList<Bitmap> bitmap_list = new ArrayList<Bitmap>();
	private ArrayList<Boolean> isImageAvailable = new ArrayList<Boolean>();
	// ---------------------- //
	private ArrayList<PhotoObj> Photos = new ArrayList<PhotoObj>();
	private ArrayList<HorseObj> HorseList = new ArrayList<HorseObj>();
	// ---------------------- //
	private ProgressDialog Dialoading,DownloadDial;
	// ---------------------- //
	public static final int DOWNLOAD_PHOTO = 0;
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_horse_photos);
        
        SetupControls();
        Vars.setPageNavi(true);
        
        CallUrlImages();
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
   	public boolean onCreateOptionsMenu(Menu menu){
   	   	
   	MenuInflater inflater = getMenuInflater();
   	inflater.inflate(R.menu.photo_menu, menu);
   	return true;
   }
    //=========================================================================	
   @Override
   	public boolean onOptionsItemSelected(MenuItem item) {
   	
       switch (item.getItemId()) {
           case R.id.refresh:
        	   CallUrlImages();
               break;
           case R.id.add:
           	
               break;
       }
       return true;
   }
 // =========================================================================
 // TODO onClick View
 // =========================================================================
    public void onBack(View v){
    	this.finish();
    	Vars.setPageNavi(false);
    }
    // =========================================================================
    public void onOptions(View v){
    	openOptionsMenu();
    }
 // =========================================================================
 // TODO Implementation
 // =========================================================================
    public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
		
		if (isImageAvailable.get(position)){
			
			AlertDialog.Builder ImageDialog = new AlertDialog.Builder(this);                 
			ImageDialog.setTitle("Image Selection");  
			
			ImageView imageView = new ImageView(this);
			imageView.setImageBitmap(bitmap_list.get(position));
			
			ImageDialog.setView(imageView);
			
			String type = Vars.getUserType(this);
			if(type.equals("trainer")){
				
					ImageDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int which) {
				    	//openOptionsMenu();
				        return;   
				    }});
				
			} else if (type.equals("owner")){
				
			}
			
			ImageDialog.setPositiveButton("Back", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int whichButton) {  
					
				    return;                  
		    }}); 
			
			ImageDialog.setNeutralButton("Download", new DialogInterface.OnClickListener(){
		    public void onClick(DialogInterface dialog, int which) {
		    	
		    	downloadPics(position);
		    	
		        return;   
		    }});
			
			ImageDialog.show();
		}
		
	}
 // =========================================================================
 // TODO Functions
 // =========================================================================
    @SuppressWarnings("deprecation")
	public void SetupControls(){
    	   	
    	Title = (TextView) findViewById(R.id.tvTitle);
    	Title.setText("Photo Gallery");
    	
    	gallery = (Gallery)findViewById(R.id.galHorse);
    	gallery.setSpacing(5);
    
    	// -- Initialize data ...
    	HorseList = Vars.getHorseListObj();
    }
    // =========================================================================
    @SuppressLint("HandlerLeak")
	private void CallUrlImages(){ //Main
    	// -- 
    	bitmap_list.clear();
    	isImageAvailable.clear();
    	// --
    	Dialoading = ProgressDialog.show(this, "Please Wait..","Processing...", true);
		final Handler handler = new Handler() {
		   public void handleMessage(Message msg) {
			  Dialoading.dismiss();
		      ///// 2nd if the load finish -----
		      callMethods();
			  ///// -----
		      }};
		      Thread checkUpdate = new Thread() {  
		   public void run() {	
			  /// 1st main activity here... -----
			   getUrlImages();
			  ////// -----
		      handler.sendEmptyMessage(0);				      
		      }};
		      checkUpdate.start();
    }
    // =========================================================================
    public void getUrlImages(){ // Sub
    	
    	String horseID = HorseList.get(Vars.getHorsePos()).ID;
    	
    	Photos = JsonParser.GetImageUrl(this, horseID);
    	
    	for (PhotoObj photo : Photos){
    		
    		bitmap_list.add(photo.BITMAP);
    		isImageAvailable.add(photo.AVAILABLE);
    	}
    	
    }
    // =========================================================================
    public void callMethods(){ // Sub
        
		gallery.setAdapter(new GalleryAdapter(this,bitmap_list));
    	gallery.setOnItemClickListener(this);
    }
    // =========================================================================
    public void downloadPics(int pos){
    	
    	if(PhotoUtils.hasStorage(true)){
    		
    		if(NetUtils.isNetworkOn(this)){
    			
    			new DownloadFileAsync(this).execute(Photos.get(pos).URL);
    		} else {
    			Utils.MessageToast(this, "Slow or no Internet Connection");
    			
    		}
    	} else {
    		Utils.MessageToast(this, "Either SD Card is not Mounted or Read Only");
    	}
    	
    	
    }
    // =========================================================================
    // TODO Dialog
    // ========================================================================= 
   @Override
   protected Dialog onCreateDialog(int id){
   	
   	switch(id){
   	
   	case DOWNLOAD_PHOTO:
   		
   		DownloadDial = new ProgressDialog(this);
   		DownloadDial.setMessage("Downloading  file..");
   		DownloadDial.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
   		DownloadDial.setCancelable(false);
   		DownloadDial.setMax(100);
   		DownloadDial.show();
   		
   		return DownloadDial;
	default:
   		return null;
   	}
	
   }
    // =========================================================================
    // TODO Async Task
    // =========================================================================
   public class DownloadFileAsync extends AsyncTask<String, String, String>{
   	
   	private int count;
   	private Context Core;
   	private NotificationManager NotifyManager;
   	private Notification notify;
   	private int ID = 1;
   	
   	public DownloadFileAsync(Context core){
   		this.Core = core;
   	}
   	
   	@Override
   	protected void onPreExecute(){
   		super.onPreExecute();
   		showDialog(DOWNLOAD_PHOTO);
   		DownloadDial.setProgress(0);
   	}

	@Override
	protected String doInBackground(String... params) {
		
		File nameFile = new File(params[0]);
		
	try{
			
		URL url = new URL(params[0]);
	    URLConnection conexion = url.openConnection();
	    conexion.connect();

	    int lenghtOfFile = conexion.getContentLength();
	    // --
	    String FilePath = "/Download/KYHPhotos/";
        File Directory1 = new File( Environment.getExternalStorageDirectory(),FilePath);	
        
  		if (!Directory1.exists()){
  			Directory1.mkdir();
  		}
  		// --
  		String UserName = Vars.getUsername(Core) +"/"; 
  		File Directory2 = new File(Directory1,UserName);
  		
  		if (!Directory2.exists()){
  			Directory2.mkdir();
  		}
  		// --
  		String HorseName = "/" + HorseList.get(Vars.HorsePos).TITLE + "/";
  		File Directory3 = new File(Directory2,HorseName);
  		
  		if (!Directory3.exists()){
  			Directory3.mkdir();
  		}
  		// --
  		File fileCom = new File(Directory3,nameFile.getName());
  		
	    InputStream input = new BufferedInputStream(url.openStream());
	    OutputStream output = new FileOutputStream(fileCom);

	    byte data[] = new byte[1024];

	    long total = 0;

	        while ((count = input.read(data)) != -1) {
	            total += count;
	            publishProgress(""+(int)((total*100)/lenghtOfFile));
	            output.write(data, 0, count);
	        }

	        output.flush();
	        output.close();
	        input.close();
	        
	        String path = fileCom.getAbsolutePath();
	        
	        return path;
	        
	} catch (Exception e) {}
		
		return null;
	}
   	
	protected void onProgressUpdate(String... progress){
		//Utils.LogCat(progress[0]);
		DownloadDial.setProgress(Integer.parseInt(progress[0]));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onPostExecute(String path){
		dismissDialog(DOWNLOAD_PHOTO);
		
		Notifier RecordNotify = new Notifier(Core);
    	RecordNotify.show("Download Complete",path,android.R.drawable.stat_sys_download_done);
		// --<<
	}
   }
 // =========================================================================
 // TODO Final Destination
}
