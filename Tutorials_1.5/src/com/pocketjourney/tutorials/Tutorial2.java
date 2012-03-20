package com.pocketjourney.tutorials;

import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class Tutorial2 extends MapActivity {

    @Override
	public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);

        setContentView(R.layout.tutorial2);
    }

    /**
     * Must let Google know that a route will not be displayed
     */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	} 
}
