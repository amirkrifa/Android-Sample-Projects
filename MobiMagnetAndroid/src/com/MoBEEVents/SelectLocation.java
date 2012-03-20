package com.MoBEEVents;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.mobimagnet.R;
import com.mobimagnet.R.drawable;
import com.mobimagnet.R.id;
import com.mobimagnet.R.layout;

public class SelectLocation extends MapActivity {

	LinearLayout linearLayout;
	MapView mapView;
	
	private List<Overlay> mapOverlays;
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);
	
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

		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(false);
		mapView.setTraffic(true);
		mapView.setLongClickable(true);
		
		mapOverlays = mapView.getOverlays();
		
		Drawable drawable = this.getResources().getDrawable(R.drawable.android);
		
		SelectLocationItemizedOverlay itemsOverlay = new SelectLocationItemizedOverlay(drawable, this);
		
		GeoPoint point = new GeoPoint((int)(MoBEEVents.currentUserLocation.getLatitude()*1E6), (int)(MoBEEVents.currentUserLocation.getLongitude()*1E6));
		OverlayItem overlayitem = new OverlayItem(point, "testfffffffffff", "test2");
		itemsOverlay.addOverlay(overlayitem);
		
		mapOverlays.add(itemsOverlay);

		
	}
	
	
}
