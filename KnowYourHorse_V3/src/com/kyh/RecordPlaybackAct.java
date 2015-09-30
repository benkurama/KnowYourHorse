package com.kyh;

import java.io.IOException;

import codes.libs.media.TimeConvertion;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class RecordPlaybackAct extends Activity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener{
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private TextView RecordNameTV,DescriptionTV,TotalDurationTV,CurrentDurationTV;
	private Button PlayBtn;
	private SeekBar SeekPlayer;
	// ---------------------- //
	private Bundle getValues;
	private MediaPlayer Player;
	private TimeConvertion TimeCon;
	private Handler HandlerPlayer = new Handler();
	// ---------------------- //
	private Boolean oncePlayed = false;
 // =========================================================================
 // TODO Activity Life Cycle
 // =========================================================================		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle sonicInstance) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(sonicInstance);
        setContentView(R.layout.act_record_playback);
        
        SetupControls();
    }
    @Override
	protected void onPause() {
		super.onPause();
		
		if(Player.isPlaying()){
			
			HandlerPlayer.removeCallbacks(UpdateTimeTask);
			
			Player.stop();
			Player.reset();
			Player.release();
		}
	}
 // =========================================================================
 // TODO onClick View
 // =========================================================================
    public void onBack(View v){
    	this.finish();
    }
    // =========================================================================
    public void onPlay(View v){
    	playAction();
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
    	public void run(){
    		
    		long totalDuration = Player.getDuration();
    		long currentDuration = Player.getCurrentPosition();
    		
    		TotalDurationTV.setText("" +TimeCon.milliSecondsToTimer(totalDuration));
    		CurrentDurationTV.setText("" +TimeCon.milliSecondsToTimer(currentDuration));
    		
    		int progress = (int)(TimeCon.getProgressPercentage(currentDuration, totalDuration));
    		SeekPlayer.setProgress(progress);
    		
    		HandlerPlayer.postDelayed(this, 100);
    	}
    };
    // =========================================================================
    @Override
	public void onCompletion(MediaPlayer mp) {
    	
		HandlerPlayer.removeCallbacks(UpdateTimeTask);
		stop();
	}
    // =========================================================================    
    @Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		
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
    	   	
    	RecordNameTV = (TextView)findViewById(R.id.tvRecordNameP);
    	DescriptionTV = (TextView)findViewById(R.id.tvDescP);
    	PlayBtn = (Button)findViewById(R.id.btnPlayP);
    	SeekPlayer = (SeekBar)findViewById(R.id.sbSeekPlayerP);
    	TotalDurationTV = (TextView)findViewById(R.id.tvTotalDurationP);
    	CurrentDurationTV = (TextView)findViewById(R.id.tvCurrentDurationP);
    	
    	// Initialize -------------------------------------------------------
    	
    	getValues = getIntent().getExtras();
    	
    	RecordNameTV.setText(getValues.getString("recordname"));
    	DescriptionTV.setText(getValues.getString("description"));
    	
    	Player = new MediaPlayer();
    	Player.setOnCompletionListener(this);
    	TimeCon = new TimeConvertion();
    	SeekPlayer.setOnSeekBarChangeListener(this);
    	SeekPlayer.setEnabled(false);
    }
    // =========================================================================
    public void playAction(){
    	
    	try {
    		if(!oncePlayed){
    			
    			SeekPlayer.setProgress(0);
    			SeekPlayer.setMax(100);
    			SeekPlayer.setEnabled(true);
    			//--
    			Player.reset();
    			Player.setDataSource(getValues.getString("path"));
    			Player.prepare();
    			Player.start();
    			// --
    			oncePlayed = true;
    			// --
    			updateProgress();
    			// --
    			PlayBtn.setText("Pause");
    		} else {
    			
    			if(Player.isPlaying()){
    				
    				Player.pause();
    				PlayBtn.setText("Play");
    			} else {
    				
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
    public void updateProgress(){
    	HandlerPlayer.postDelayed(UpdateTimeTask, 1000);
    }
    // =========================================================================
    public void stop(){
    	
    	Player.stop();
    	PlayBtn.setText("Play");
    	SeekPlayer.setEnabled(false);
    	// --
    	oncePlayed = false;
    }
 // =========================================================================
 // TODO Final Destination
}
