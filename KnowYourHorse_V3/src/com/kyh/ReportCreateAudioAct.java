package com.kyh;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import codes.libs.Utils;
import codes.libs.media.TimeConvertion;

import com.kyh.objects.HorseObj;
import com.kyh.objects.KYHDatabase;

public class ReportCreateAudioAct extends Activity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private TextView RecordTitleTV,CurrentDuration,TotalDuration;
	private Button RecordBtn,PlayBtn,StopBtn,CancelBtn,SaveBtn;
	private Chronometer ChronoTime;
	private SeekBar SeekPlayer;
	// ---------------------- //
	private String RecordNameStr,RecordDescStr;
	private Boolean isRecord = false;
	private Boolean isPlay = false;
	private Boolean oncePlayed = false;
	// ---------------------- //
	private MediaRecorder Recorder;
	private MediaPlayer Player;
	private File MainDirectory,AudioFile;
	private TimeConvertion TimeCon;
	private KYHDatabase Database;
	// ---------------------- //
	private ArrayList<HorseObj> HorseList = new ArrayList<HorseObj>();
	private Handler HandlerPlayer = new Handler();
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_report_create_audio);
        
        SetupControls();
        Vars.setPageNavi(true);
        
        popAddDialog();
       
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
	protected void onPause() {
		super.onPause();
		
		if(isRecord){
			stopRecAction();
		}
		
		if(Player.isPlaying() || isPlay){
			Player.stop();
		}
	}
	// =========================================================================
 // TODO onClick View
 // =========================================================================
    public void onCancel(View v){
    	
    	returnPage();
    }
    // =========================================================================
    @SuppressLint("SimpleDateFormat")
	public void onSave(View v){
    	
    	try{
    		
    		Player.reset();
    		Player.setDataSource(AudioFile.getAbsolutePath());
    		Player.prepare();
    		
    	}catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	// -- Convert temporary file to permanent
    	File newFile = new File(MainDirectory, RecordNameStr +".3gp");
    	AudioFile.renameTo(newFile);
    	// --
    	int Userid =  Integer.parseInt(Vars.getUserID(this)) ;
    	int Horseid = Integer.parseInt(HorseList.get(Vars.getHorsePos()).ID) ;
    	String RecordName = RecordNameStr;
    	String RecordDesc = RecordDescStr;
    	
    	long totalDuration = Player.getDuration();
    	String Duration = TimeCon.milliSecondsToTimer(totalDuration);
    	
    	String Type = "3gp";
    	String Status = "Pending";
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy");
    	String Date = sdf.format(new Date());
    	
    	String Path = newFile.getAbsolutePath();
    	// --
    	Database = new KYHDatabase(this);
    	Database.openToWrite();
    	Database.insertRecord(Userid, Horseid, RecordName, RecordDesc, Duration, Type, Status, Date, Path);
    	Database.close();
    	// --
    	Utils.MessageToast(this, "Audio Record has Successfully Save");
    	this.finish();
    	
    }
    // =========================================================================
    public void onRec(View v){
    	
    	recordAction();
    }
    // =========================================================================
    public void onPlay(View v){
    	
    	playAction();
    }
    // =========================================================================
    public void onStop(View v){
    	
    	
    	if(isRecord){
        	// --
    		stopRecAction();
    	}
    	// --
    	if (isPlay || Player.isPlaying()){
    		stopPlayAction();
    	}
    }
    // =========================================================================
    public void onRewind(View v){
    	
    	int seekBackward = (Player.getDuration() / 20);
    	int currentPosition = Player.getCurrentPosition();

    	if((currentPosition - seekBackward) >= 0){
    		
    		Player.seekTo(currentPosition - seekBackward);
    	} else {
    		Player.seekTo(0);
    	}
    }
    // =========================================================================
    public void onForward(View v){
    	
    	int seekForward = (Player.getDuration() / 20);
    	int currentPosition = Player.getCurrentPosition();
    	
    	if ((currentPosition + seekForward) <= Player.getDuration()){
    		
    		Player.seekTo(currentPosition + seekForward);
    	} else {
    		Player.seekTo(Player.getDuration());
    	}
    }
 // =========================================================================
 // TODO Implementation
 // =========================================================================
	private Runnable UpdateTimeTask = new Runnable(){
		@Override
		public void run() {
			
			long totalDuration = Player.getDuration();
			long currentDuration = Player.getCurrentPosition(); 
			
			TotalDuration.setText(""+ TimeCon.milliSecondsToTimer(totalDuration));
			CurrentDuration.setText(""+ TimeCon.milliSecondsToTimer(currentDuration));
			
			int progress = (int)(TimeCon.getProgressPercentage(currentDuration, totalDuration));
			SeekPlayer.setProgress(progress);
			
			HandlerPlayer.postDelayed(this, 100);
		}
	};
    // =========================================================================
    @Override
	public void onCompletion(MediaPlayer mp) {
		
    	HandlerPlayer.removeCallbacks(UpdateTimeTask);
    	stopPlayAction();
	}
	// =========================================================================
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
		HandlerPlayer.removeCallbacks(UpdateTimeTask);
	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
		HandlerPlayer.removeCallbacks(UpdateTimeTask);
		int totalDuration = Player.getDuration();
		int currentDuration = TimeCon.progressToTimer(seekBar.getProgress(), totalDuration);
		
		Player.seekTo(currentDuration);
		
		updateProgress();
	}
 // =========================================================================
 // TODO Functions
 // =========================================================================
    public void SetupControls(){
    	
    	RecordTitleTV = (TextView)findViewById(R.id.tvRecordTitle);
    	
    	RecordBtn = (Button)findViewById(R.id.btnRecordH);
    	PlayBtn = (Button)findViewById(R.id.btnPlayH);
    	StopBtn = (Button)findViewById(R.id.btnStopH);
    	CancelBtn = (Button)findViewById(R.id.btnCancelH);
    	SaveBtn = (Button)findViewById(R.id.btnSaveH);
    	
    	ChronoTime = (Chronometer)findViewById(R.id.chrmTime);
    	
    	TotalDuration = (TextView)findViewById(R.id.tvTotalDuration);
    	CurrentDuration = (TextView)findViewById(R.id.tvCurrentDuration);
    	
    	SeekPlayer = (SeekBar)findViewById(R.id.sbProgressBar);
    	SeekPlayer.setOnSeekBarChangeListener(this);
    	SeekPlayer.setEnabled(false);
    	
    	Player = new MediaPlayer();
    	Player.setOnCompletionListener(this);
    	
    	TimeCon = new TimeConvertion();
    	
    	// Initialize -------------------------------------------------------
    	HorseList = Vars.getHorseListObj();
    	
    	PlayBtn.setEnabled(false);
        StopBtn.setEnabled(false);
        SaveBtn.setEnabled(false);
    }
    // =========================================================================
    public void popAddDialog(){
    	
    	final Dialog dialog = new Dialog(this,android.R.style.Theme_Dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.getWindow().setBackgroundDrawableResource(R.drawable.blanc);
		dialog.setContentView(R.layout.dialog_add_rec);
		// --------------------------------------------------------------
		final EditText RecordName = (EditText)dialog.findViewById(R.id.etAddRec);
		final EditText RecordDesc = (EditText)dialog.findViewById(R.id.etAddRecDesc);
		// --
		Button AddBtn = (Button)dialog.findViewById(R.id.btnAdd);
		Button CancelBtn = (Button)dialog.findViewById(R.id.btnCancel);
		// --
		AddBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			 	RecordNameStr = RecordName.getText().toString();
				RecordDescStr = RecordDesc.getText().toString();
				RecordTitleTV.setText(RecordNameStr);
				
				dialog.dismiss();
				
				createDirectory();
			}
		});
		// --
		CancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				dialog.dismiss();
				returnPage();
			}
		});
		// --------------------------------------------------------------
		dialog.setCancelable(true);
		dialog.show();
    }
    // =========================================================================
    public void returnPage(){

    	if(!(AudioFile == null)){
    		
    		if(AudioFile.exists()){
    			AudioFile.delete();
    		}
    	}
    	// --
    	this.finish();
    	Vars.setPageNavi(false);
    }
    // =========================================================================
    public void createDirectory(){
    	// ------------- Step 1 >>>
    	String filePath = "/KYHAudio/";
    	MainDirectory = new File(Environment.getExternalStorageDirectory(),filePath);
    	
    	if(!MainDirectory.exists()){
    		MainDirectory.mkdir();
    	}
    	// ------------- Step 2 >>>
    	filePath += "/user-id_" +Vars.getUserID(this)+ "/";
    	MainDirectory = new File(Environment.getExternalStorageDirectory(),filePath);
    	
    	if (!MainDirectory.exists()){
    		MainDirectory.mkdir();
    	}
    	// ------------- Step 3 >>>
    	filePath += "/horse-id_" +HorseList.get(Vars.getHorsePos()).ID+ "/";
    	MainDirectory = new File(Environment.getExternalStorageDirectory(),filePath);
    	
    	if(!MainDirectory.exists()){
    		MainDirectory.mkdir();
    	}
    }
    // =========================================================================
    public void recordAction(){
    	
    	try{
    		// ------------- Step 1 >>>
    		AudioFile = File.createTempFile(RecordNameStr, ".3gp",MainDirectory);
    		
    		Recorder = new MediaRecorder();
    		Recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    		Recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    		Recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    		Recorder.setOutputFile(AudioFile.getAbsolutePath());
    		Recorder.prepare();
    		Recorder.start();
    		// --
    		isRecord = true;
    		// --
    		ChronoTime.setBase(SystemClock.elapsedRealtime());
    		ChronoTime.start();
    		// --
    		StopBtn.setEnabled(true);
    		RecordBtn.setEnabled(false);
    		// --
    	}catch(Exception e){
    		
    	}
    }
    // =========================================================================
    public void stopRecAction(){
    	
    	Recorder.stop();
    	Recorder.release();
    	// --
    	isRecord = false;
    	// -- 
    	PlayBtn.setEnabled(true);
    	StopBtn.setEnabled(false);
    	RecordBtn.setEnabled(false);
    	SaveBtn.setEnabled(true);
    	// -- 
    	ChronoTime.stop();
    	ChronoTime.setText("0:00");
    }
    // =========================================================================
    public void playAction(){
    	
    	try {
    		
    		if(!oncePlayed){
    			
    			SeekPlayer.setProgress(0);
    			SeekPlayer.setMax(100);
    			SeekPlayer.setEnabled(true);
    			// -- 
    			Player.reset();
    	    	Player.setDataSource(AudioFile.getAbsolutePath());
    	    	Player.prepare();
    	    	Player.start();
    	    	// --
    	    	updateProgress();
    	    	// -- 
    	    	isPlay = true;
    	    	oncePlayed = true;
    	    	// --
    	    	PlayBtn.setText("Pause");
    	    	StopBtn.setEnabled(true);
    		
    		} else {
    			
    			if(Player.isPlaying()){
    				
    				Player.pause();
    				PlayBtn.setText("Play");
    			}else{
    				
    				Player.start();
    				PlayBtn.setText("Pause");
    			}
    		}
    		
    	} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    // =========================================================================
    public void stopPlayAction(){
    	
    	Player.stop();
    	isPlay = false;
    	oncePlayed = false;
    	// -- 
    	SeekPlayer.setEnabled(false);
    	// --
    	StopBtn.setEnabled(false);
    	
    	PlayBtn.setText("Play");
    }
    // =========================================================================
    public void updateProgress(){
    	
    	HandlerPlayer.postDelayed(UpdateTimeTask, 1000);
    }
 // =========================================================================
 // TODO Final Destination
}
