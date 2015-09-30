package com.kyh.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class Notifier {

	private Notification notify;
	private Context Core;
	private NotificationManager NotifyManager;
	private static final int ID_ = 1;
	
	public Notifier(Context core){
		this.Core = core;
	}
	
	public void show(String titleS,String contentS,int iconTheme){
		
		// -- >> Create notification download
		int icon = iconTheme;
		CharSequence caption = titleS;
		long time = System.currentTimeMillis();
		
		notify = new Notification(icon,caption,time);
		// --
		CharSequence title = titleS;
		CharSequence content = contentS;
		Intent intent = new Intent();
		PendingIntent pIntent = PendingIntent.getActivity(Core, 0, intent, 0);
		// --
		notify.setLatestEventInfo(Core, title, content, pIntent);
		notify.flags = Notification.FLAG_AUTO_CANCEL;
		// --
		NotifyManager = (NotificationManager)Core.getSystemService(Context.NOTIFICATION_SERVICE);
		NotifyManager.notify(ID_,notify);
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() { public void run() {NotifyManager.cancel(ID_);}}, 15000);
		// --<<
	}
}
