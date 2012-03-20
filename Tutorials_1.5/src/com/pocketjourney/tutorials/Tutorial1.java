package com.pocketjourney.tutorials;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.MapActivity;

public class Tutorial1 extends MapActivity {

    @Override
	public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);

        setContentView(R.layout.tutorial1);
        
        Button button = (Button) findViewById(R.id.button_click_me);
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Toast.makeText(Tutorial1.this, "Button Clicked",Toast.LENGTH_SHORT).show();
        }});
    }

    /**
     * Must let Google know that a route will not be displayed
     */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	} 
}
