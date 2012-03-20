package com.MoBEEVents;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.mobimagnet.R;
import com.mobimagnet.R.id;
import com.mobimagnet.R.layout;

public class ContentsMap extends MapActivity {


	private MapView mapView;
	private List<Overlay> mapOverlays;

	// Initial focus point 
	double initLatitude = MoBEEVents.currentUserLocation.getLatitude();
	double initLongitude = MoBEEVents.currentUserLocation.getLongitude();
	private String selectedCategory;
	@Override
	protected boolean isRouteDisplayed() 
	{
		return false;
	}

	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);

		Bundle extras = getIntent().getExtras();
		selectedCategory = extras.getString("SelectedCategory");
		
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(false);
		mapView.setStreetView(false);
		mapOverlays = mapView.getOverlays();

		// Configuring the sat checkbox
		CheckBox cbSat = (CheckBox)findViewById(R.id.checkboxSat);
		cbSat.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if(isChecked) {                    
					// Activate satellite view
					mapView.setSatellite(true);
				}
				else{
					// Disactivate sattelite view
					mapView.setSatellite(false);
				}
			}    
		}
		);

		// Configuring the StreetView checkbox
		CheckBox cbStreet = (CheckBox)findViewById(R.id.checkboxStreet);
		cbStreet.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if(isChecked) {                    
					// Activate satellite view
					mapView.setStreetView(true);
				}
				else{
					// Disactivate sattelite view
					mapView.setStreetView(false);
				}
			}    
		}
		);

		LoadAllContents();
		
		navigateToLocation(initLatitude*1E6, initLongitude*1E6, mapView);
	}

	private void LoadAllContents()
	{
		List<Content> lc = DatabaseHelper.getDBManager().getContentsForCategory(selectedCategory);

		for(Content c : lc)
		{
			// Create a new item to be added 
			Drawable drawable = this.getResources().getDrawable(Integer.parseInt(c.get(Content.CONTENT_IMAGE)));
			ContentsMapItemizedOverlay itemsOverlay = new ContentsMapItemizedOverlay(drawable, this);
			GeoPoint point = new GeoPoint(c.getLatitudeE6(), c.getLongitudeE6());
			OverlayItem overlayitem = new OverlayItem(point, "testfffffffffff", "test2");
			itemsOverlay.addOverlay(overlayitem);
			// Add to the map	
			mapOverlays.add(itemsOverlay);
		}
	}

	public void navigateToLocation (double latitude, double longitude, MapView mv) 
	{
		GeoPoint p = new GeoPoint((int) latitude, (int) longitude); //new GeoPoint
		mv.displayZoomControls(true); //display Zoom (seems that it doesn't work yet)
		MapController mc = mv.getController();
		mc.animateTo(p); //move map to the given point
		int zoomlevel = mv.getMaxZoomLevel(); //detect maximum zoom level
		mc.setZoom(zoomlevel - 1); //zoom
	}

}
