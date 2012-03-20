package com.MoBEEVents;

import com.mobimagnet.R;
import com.mobimagnet.R.id;
import com.mobimagnet.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentDetails extends Activity{
	
	private String contentText;
	private String contentDescription;
	private String contentPublisher;
	private String contentImage;
	
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(com.mobimagnet.R.layout.contentdetails);
	   
	    Bundle extras = getIntent().getExtras();
		contentPublisher = extras.getString(Content.CONTENT_PUBLISHER);
		contentText = extras.getString(Content.CONTENT_TEXT);
		contentImage = extras.getString(Content.CONTENT_IMAGE);
		
		ImageView img = (ImageView) findViewById(com.mobimagnet.R.id.contentDetailedImg);
		img.setImageResource(Integer.parseInt(contentImage));
		
		TextView publisher = (TextView) findViewById(com.mobimagnet.R.id.contentDetailedTitle);
		publisher.setText(contentPublisher);
	
	
	}

}
