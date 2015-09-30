package com.kyh.objects;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class GalleryAdapter extends BaseAdapter {

	int galleryItem;
	private Context context;
	private ArrayList<Bitmap> bitmap_lst = new ArrayList<Bitmap>();
	
	public GalleryAdapter(Context context,ArrayList<Bitmap> list){
		super();
		
		this.context = context;
		bitmap_lst = list;
	}
	public int getCount() {
		//return galleryConfig.getGallery_list().size();
		return bitmap_lst.size();
	}
	public Object getItem(int position) {
		//return galleryConfig.getGallery_list().get(position);
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout Border = new LinearLayout(context);
		Border.setPadding(5, 5, 5, 5);
		Border.setBackgroundColor(Color.WHITE);
		
		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setAdjustViewBounds(true);
		imageView.setMaxHeight(150);
		imageView.setMaxWidth(150);
		
		imageView.setImageBitmap(bitmap_lst.get(position));
		//imageView.setImageDrawable(loadImageFromURL("http://images.wikia.com/sonic/images/e/e8/Sonic_on_Horse.jpg"));
		//imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));
		//imageView.setLayoutParams(new Gallery.LayoutParams(150,150));
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		
		Border.addView(imageView);
		return Border;
	}

}
