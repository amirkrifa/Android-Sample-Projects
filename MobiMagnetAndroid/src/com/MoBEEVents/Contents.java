package com.MoBEEVents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.HashMap;
import java.util.List;

import com.mobimagnet.R;
import com.mobimagnet.R.id;
import com.mobimagnet.R.layout;


public class Contents extends Activity {

	private ListView contentsListView;
	private String selectedCategory;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(com.mobimagnet.R.layout.listcontents);

		Bundle extras = getIntent().getExtras();
		
		if(extras != null)
		{
			selectedCategory = extras.getString(Category.CATEGORY_NAME);
		}
		
		Button button = (Button) findViewById(com.mobimagnet.R.id.switchToMapView);
		button.setOnClickListener(new switchToMapView());

		//Récupération de la listview créée dans le fichier main.xml
		contentsListView = (ListView) findViewById(com.mobimagnet.R.id.mainListView);

		// Create our own version of the list adapter
		List<Content> contents = DatabaseHelper.getDBManager().getContentsForCategory(selectedCategory);

		ListAdapter adapter = new ContentAdapter(this, contents,
				com.mobimagnet.R.layout.contentresume, new String[] {
				Content.CONTENT_IMAGE,  Content.CONTENT_PUBLISHER , Content.CONTENT_TEXT, Content.CONTENT_DISTANCE, Content.CONTENT_EXPIRATION_DATE},  
				new int[] {com.mobimagnet.R.id.contentImage, com.mobimagnet.R.id.contentTitle, com.mobimagnet.R.id.contentDescription, com.mobimagnet.R.id.contentDistance, com.mobimagnet.R.id.contentDate });

		contentsListView.setAdapter(adapter);

		//on met un écouteur d'évènement sur notre listView
		contentsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				//on récupère la HashMap contenant les infos de notre item (titre, description, img)
				HashMap<String, String> map = (HashMap<String, String>) contentsListView.getItemAtPosition(position);

				// Start an activity that shows the details with respect to the selected content
				// Create the bundle that will hot the details of the content
				Bundle contentDetails = new Bundle();
				contentDetails.putString(Content.CONTENT_TEXT, map.get(Content.CONTENT_TEXT));
				contentDetails.putString(Content.CONTENT_PUBLISHER, map.get(Content.CONTENT_PUBLISHER));
				contentDetails.putString(Content.CONTENT_IMAGE, map.get(Content.CONTENT_IMAGE));

				// Crate the intent that will be used to start the ContentDetails activity
				Intent goToDetails = new Intent(Contents.this, ContentDetails.class);
				// Add the details bundle to the intent
				goToDetails.putExtras(contentDetails);

				startActivity(goToDetails);
			}
		});
	}

	// Is called to switch from the list view of the contents to the map view one
	private class switchToMapView implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			Intent i = new Intent(Contents.this, ContentsMap.class);
			i.putExtra("SelectedCategory", selectedCategory);
			startActivity(i);
		}	    
	}

	@Override
	public void onStop() {
		super.onStop();
	}
}



