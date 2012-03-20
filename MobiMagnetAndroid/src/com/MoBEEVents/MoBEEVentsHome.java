package com.MoBEEVents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.mobimagnet.*;
import com.mobimagnet.R.drawable;
import com.mobimagnet.R.id;
import com.mobimagnet.R.layout;
import com.mobimagnet.R.styleable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class MoBEEVentsHome extends Activity{

	private Gallery gallery;
	private List<Content> todayContents = new ArrayList<Content>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home);
		
		gallery = (Gallery) findViewById(R.id.todayEventsGallery);
		
		// Get today Contents
		todayContents = DatabaseHelper.getDBManager().getTodayContents();
		
		gallery.setAdapter(new AddImgAdp(this, todayContents));
		
		gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView parent, View v, int position, long id) 
		{
			//on récupère la HashMap contenant les infos de notre item (titre, description, img)
			Content selectedC = todayContents.get(position);
			
			// Start an activity that shows the details with respect to the selected content
			// Create the bundle that will hot the details of the content
			Bundle contentDetails = new Bundle();
			contentDetails.putString(Content.CONTENT_TEXT, selectedC.get(Content.CONTENT_TEXT));
			contentDetails.putString(Content.CONTENT_PUBLISHER, selectedC.get(Content.CONTENT_PUBLISHER));
			contentDetails.putString(Content.CONTENT_IMAGE, selectedC.get(Content.CONTENT_IMAGE));

			// Crate the intent that will be used to start the ContentDetails activity
			Intent goToDetails = new Intent(MoBEEVentsHome.this, ContentDetails.class);
			// Add the details bundle to the intent
			goToDetails.putExtras(contentDetails);

			startActivity(goToDetails);
			
		}

		});
		
		Button button = (Button) findViewById(R.id.moreContentsButton);
		button.setOnClickListener(new SyncMoreContentsClickListener());

	}

	private class SyncMoreContentsClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			startActivity(new Intent(MoBEEVentsHome.this, Categories.class));

		}	    
	}

	private class AddImgAdp extends BaseAdapter 
	{
		int GalItemBg;
		private Context cont;
		// Adding images.
		
		private List<Content> contents = new ArrayList<Content>();
		
		private Integer[] Imgid = {R.drawable.event, R.drawable.event, R.drawable.event, R.drawable.event2};
				                
		public AddImgAdp(Context c, List<Content> l) 
		{
			contents = l;
			cont = c;
			TypedArray typArray = obtainStyledAttributes(R.styleable.TodayEventsGalleryTheme);
			GalItemBg = typArray.getResourceId(R.styleable.TodayEventsGalleryTheme_android_galleryItemBackground, 0);
			typArray.recycle();
		}
		
		private int getImage(int pos)
		{
			return Integer.parseInt(contents.get(pos).get(Content.CONTENT_IMAGE));
		}
		public int getCount() 
		{
			return contents.size();
		}
		public Object getItem(int position) 
		{
			return position;
		}
		public long getItemId(int position) 
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			
			ImageView imgView = new ImageView(cont);
			imgView.setImageResource(getImage(position));
			
			// Fixing width & height for image to display
			//imgView.setLayoutParams(new Gallery.LayoutParams(200, 250));
			imgView.setScaleType(ImageView.ScaleType.FIT_XY);
			imgView.setBackgroundResource(GalItemBg);
			return imgView;
		}
	}
}





