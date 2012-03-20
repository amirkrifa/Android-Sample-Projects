package com.pocketjourney.tutorials;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Tutorials extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
    
        Button tutorial1Button = (Button) findViewById(R.id.button_view_tutorial_1);
        tutorial1Button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		startActivity(new Intent(Tutorials.this, Tutorial1.class));
	    }});

        Button tutorial2Button = (Button) findViewById(R.id.button_view_tutorial_2);
        tutorial2Button.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View view) {
	    	startActivity(new Intent(Tutorials.this, Tutorial2.class));
	    }});
        
        Button tutorial3Button = (Button) findViewById(R.id.button_view_tutorial_3);
        tutorial3Button.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View view) {
	    	startActivity(new Intent(Tutorials.this, Tutorial3.class));
	    }});

        Button tutorial4Button = (Button) findViewById(R.id.button_view_tutorial_4);
        tutorial4Button.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View view) {
	    	startActivity(new Intent(Tutorials.this, Tutorial4.class));
	    }});

        Button tutorial5Button = (Button) findViewById(R.id.button_view_tutorial_5);
        tutorial5Button.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View view) {
	    	startActivity(new Intent(Tutorials.this, Tutorial5.class));
	    }});
   
	    Button tutorial6Button = (Button) findViewById(R.id.button_view_tutorial_6);
	    tutorial6Button.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View view) {
	    	startActivity(new Intent(Tutorials.this, Tutorial6.class));
	   	}});
	}
}    