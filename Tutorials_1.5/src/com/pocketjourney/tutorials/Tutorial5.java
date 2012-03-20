package com.pocketjourney.tutorials;

import com.pocketjourney.view.TransparentPanel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class Tutorial5 extends Activity {
	
	private Animation animShow, animHide;
	
	@Override
	public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);

        setContentView(R.layout.tutorial5);
     
        initPopup();
    }
    
    private void initPopup() {
    	
    	final TransparentPanel popup = (TransparentPanel) findViewById(R.id.popup_window);

    	//  Start out with the popup initially hidden.
    	popup.setVisibility(View.GONE);
    	
    	
    	animShow = AnimationUtils.loadAnimation( this, R.anim.popup_show);
    	animHide = AnimationUtils.loadAnimation( this, R.anim.popup_hide);
    	
    	final Button   showButton = (Button) findViewById(R.id.show_popup_button);
    	final Button   hideButton = (Button) findViewById(R.id.hide_popup_button);
    	showButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				popup.setVisibility(View.VISIBLE);
				popup.startAnimation( animShow );
				showButton.setEnabled(false);
				hideButton.setEnabled(true);
        }});
        
        hideButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				popup.startAnimation( animHide );
				showButton.setEnabled(true);
				hideButton.setEnabled(false);
				popup.setVisibility(View.GONE);
        }});


    	final TextView locationName = (TextView) findViewById(R.id.location_name);
        final TextView locationDescription = (TextView) findViewById(R.id.location_description);
        
        locationName.setText("Chinatown");
        locationDescription.setText("Chinatown has always been one of the first places tourists go in San Francisco."
        							+ "  Not only can you find great chinese food and trinket shopping, but learn"
        							+ " about the history of the city and the Chinese culture that is such a strong"
        							+ " part if it");
	}
}
