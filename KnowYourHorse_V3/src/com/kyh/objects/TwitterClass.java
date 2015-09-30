package com.kyh.objects;

import java.util.concurrent.ExecutionException;

import twitter4j.Relationship;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class TwitterClass  {
	
    /*<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data android:scheme="oauth" android:host="t4jsample"/>
    </intent-filter>*/
	
	static String TWITTER_CONSUMER_KEY = "r1loI3SV8DmPL6ebgjWg";
    static String TWITTER_CONSUMER_SECRET = "kbF6JmRcY3sNlQqeYcGxoPKKjevYUtFVaMDrC53e0";
    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_OAUTH_USERID= "id"; 
    static final String PREF_KEY_OAUTH_USERNAME= "name"; 
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
    static final String TWITTER_CALLBACK_URL = "myapp://knowyourhorse";
   // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    public Context context;
    public Activity act;
    private static SharedPreferences mSharedPreferences;

    private static Twitter twitter;
    private static RequestToken requestToken;

// =========================================================================
//Constructor
// =========================================================================
	public TwitterClass(Context context,Activity act) {
		this.context = context;
		this.act = act;
		 mSharedPreferences = this.context.getSharedPreferences("MyPref", 0);
	}
// =========================================================================
//Log-in 
// =========================================================================
	public void login() {
	LogInRun logInRun = new LogInRun();
	logInRun.execute();
	}
	// =========================================================================
    private void logInTweeter(){
    	ConfigurationBuilder builder = new ConfigurationBuilder();
    	builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
        Configuration configuration = builder.build();

        TwitterFactory factory = new TwitterFactory(configuration);
        twitter = factory.getInstance();
        try {
		    requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
		    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
		    this.context.startActivity(intent);
        } catch (Exception e) {
		    e.printStackTrace();
		    Log.e("error",e.toString() + "eror in log in");
        }
   }
    // =========================================================================
    private class LogInRun extends AsyncTask<Void, Void, Boolean>{
	   public ProgressDialog dialog;
	   @Override
	   protected void onPreExecute() {
		super.onPreExecute();
			dialog = new ProgressDialog(context);
			dialog.setMessage("Please Wait.....");
			dialog.show();	
	   }
	   @Override
	   protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			dialog.dismiss();
	   }
	   @Override
	   protected Boolean doInBackground(Void... params) {
			logInTweeter();return null;
	   }
}
// =========================================================================
//Set Account
// =========================================================================	
	public void setAccountTask(){
		GetRequest getRequest = new GetRequest();
		getRequest.execute();
	}
	// =========================================================================
    public void setAccount(){
		Uri uri = act.getIntent().getData();
		if(uri!=null && uri.toString().startsWith(TWITTER_CALLBACK_URL)){
			String verifier = uri.getQueryParameter("oauth_verifier");
			try {
	            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
	            Editor e = mSharedPreferences.edit();
	            e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
	            e.putString(PREF_KEY_OAUTH_SECRET,accessToken.getTokenSecret());
	            e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
	            e.putLong(PREF_KEY_OAUTH_USERID, accessToken.getUserId());
	            e.putString(PREF_KEY_OAUTH_USERNAME, accessToken.getScreenName());
	            e.commit();
	            Log.e("token", accessToken.getScreenName());
	            Log.e("token", accessToken.getToken());
	            Log.e("token",accessToken.getTokenSecret());
	            Log.e("token",String.valueOf(accessToken.getUserId()));
			}catch (Exception e) {
				Log.e("error",e.toString());
			}
		}
	}
    // =========================================================================
	private class GetRequest extends AsyncTask<Void, Void, Boolean>{
    	public ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(context);
			dialog.setMessage("Please Wait.....");
			dialog.show();
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			dialog.dismiss();
		}
		protected Boolean doInBackground(Void... params) {
			setAccount();
			return null;
		}
    }
// =========================================================================
//Follow 
// =========================================================================
	public void follow(){
		FollowUs followUs = new FollowUs();
		followUs.execute();
	}
    private void followUs(){
		String accessToken = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
		String accessTokenKey = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
		ConfigurationBuilder builder = new ConfigurationBuilder();
		Configuration conf = builder
		.setOAuthAccessToken(accessToken)
		.setOAuthAccessTokenSecret(accessTokenKey)
		.setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
        .setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET)
        .setDebugEnabled(true).build();
		Twitter twitterf = new TwitterFactory(conf).getInstance();
		Log.i("Token", accessToken);
		Log.i("Token", accessTokenKey);
		
        try{
        	twitterf.createFriendship("knowyourhorse",true);
    	}catch (Exception e) {
    		e.printStackTrace();
    		Log.e("error",e.toString());
    	}
    }
    // =========================================================================
	private class FollowUs extends AsyncTask<Void, Void, Boolean>{
    	public ProgressDialog dialog;
    	
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(context);
			dialog.setMessage("Please Wait.....");
			dialog.show();
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			dialog.dismiss();
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			followUs();
			return null;
		}
		
    }
// =========================================================================
//Unfollow
// =========================================================================
	public void unfollowUs(){
		unFollow destroyFriendship = new unFollow();
		destroyFriendship.execute();
	}
	// =========================================================================
    private void unfollow(){
		String accessToken = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
		String accessTokenKey = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
		ConfigurationBuilder builder = new ConfigurationBuilder();
		Configuration conf = builder
		.setOAuthAccessToken(accessToken)
		.setOAuthAccessTokenSecret(accessTokenKey)
		.setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
        .setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET)
        .setDebugEnabled(true).build();
		Twitter twitterf = new TwitterFactory(conf).getInstance();
		Log.i("Token", accessToken);
		Log.i("Token", accessTokenKey);
		
        try{
        	twitterf.destroyFriendship("knowyourhorse");
    	}catch (Exception e) {
    		e.printStackTrace();
    		Log.e("error",e.toString());
    	}
    }
    // =========================================================================
	private class unFollow extends AsyncTask<Void, Void, Boolean>{
    	public ProgressDialog dialog;
    	
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(context);
			dialog.setMessage("Please Wait.....");
			dialog.show();
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			dialog.dismiss();
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			unfollow();
			return null;
		}
    }
// =========================================================================
//Check Friendship/Following
// =========================================================================
	public boolean checkFriendShipTask(){
		CheckFriendShipTask checkFriendShipTask = new CheckFriendShipTask();
		checkFriendShipTask.execute();
		try {
			return checkFriendShipTask.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}
	// =========================================================================
	public boolean checkFriendShip(){
		Relationship relationship;
		try {
			ConfigurationBuilder builder = new ConfigurationBuilder();
		   	builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
		    builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
		    Configuration configuration = builder.build();

		    TwitterFactory factory = new TwitterFactory(configuration);
		    Twitter twitterf = factory.getInstance();
			twitterf = factory.getInstance();
			relationship = twitterf.showFriendship(mSharedPreferences.getString(PREF_KEY_OAUTH_USERNAME, ""),"knowyourhorse");
			return relationship.isSourceFollowingTarget();
		} catch (TwitterException e) {
			e.printStackTrace();
			return false;
		}
	}
	// =========================================================================
	private class CheckFriendShipTask extends AsyncTask<Void, Void,Boolean>{
		@Override
		protected Boolean doInBackground(Void... params) {
			return checkFriendShip();
		}
	}
// =========================================================================
//Check if Login
// =========================================================================
    public boolean isTwitterLoggedInAlready() {
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }
 // =========================================================================
 //Log out
 // =========================================================================  
    public void logOutTweeter(){
    	
        Editor e = mSharedPreferences.edit();
        e.putString(PREF_KEY_OAUTH_TOKEN, "");
        e.putString(PREF_KEY_OAUTH_SECRET,"");
        e.putBoolean(PREF_KEY_TWITTER_LOGIN, false);
        e.putLong(PREF_KEY_OAUTH_USERID, 0);
        e.commit();
        
    }
// =========================================================================
//End
// ========================================================================= 
}