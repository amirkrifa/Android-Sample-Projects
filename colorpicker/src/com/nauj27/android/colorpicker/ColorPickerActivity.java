/**
 *  Color Picker by Juan Martín
 *  Copyright (C) 2010 nauj27.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.nauj27.android.colorpicker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nauj27.android.colorpicker.ral.RalColor;


/**
 * This activity receive a picture and show touched color.
 * @author nauj27
 * Extends Activity
 */
public class ColorPickerActivity extends Activity {
	// Private constants.
	//private static final String TAG = "ColorPickerActivity";
	private static final String JPEG_PICTURE = "JPEG_PICTURE";
	
	// Magic numbers :)
	private static final int DIALOG_RESULT_ID = 0;
	
	private RalColor ralColor = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        
    	// From http://www.designerandroid.com/?p=73
    	// This is the only way camera preview work on all android devices at
    	// full screen
    	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    	// No title, no name: Full screen
    	getWindow().setFormat(PixelFormat.TRANSLUCENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		// Set the default layout for this activity
        setContentView(R.layout.color_picker_layout);
        
        // Get context and ImageView view for later usage
        Context context = getApplicationContext();
        ImageView imageView = (ImageView)findViewById(R.id.ivPicture);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        
        // Get JPEG image from extras from the Intent that launch this activity
        Bundle bundleExtras = getIntent().getExtras();
        if (bundleExtras.containsKey(JPEG_PICTURE)) {
        	byte[] jpegPicture = bundleExtras.getByteArray(JPEG_PICTURE);    			
			
			int offset = 0;
			int length = jpegPicture.length;
			
			// Obtain bitmap from the JPEG data object
			Bitmap bitmap = BitmapFactory
				.decodeByteArray(jpegPicture, offset, length);
			
			// Rotate the image for Motorola phones
			if (Utils.isMotorola(android.os.Build.MODEL)) {
				Matrix matrix = new Matrix();
				matrix.postRotate(-90);
				
				// Recreate the bitmap
				Bitmap rotatedBitmap = Bitmap.createBitmap(
					bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), 
					matrix, true);
				
				imageView.setImageBitmap(rotatedBitmap);
			} else {
				imageView.setImageBitmap(bitmap);
			}
			
			// Show a bit of help to the user :)
	    	CharSequence charSequence = getString(R.string.color_picker_photo_help);
	    	int duration = Toast.LENGTH_SHORT;
	    	Toast toast = Toast.makeText(context, charSequence, duration);
	    	toast.show();

        } else {
        	// JPEG data is not in the bundle extra received
	    	finishActivity(RESULT_CANCELED);
        }
        
        /**
         * Set the listener for touch event into the image view.
         */
        imageView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				
				int action = motionEvent.getAction();
				switch(action) {
					case(MotionEvent.ACTION_DOWN):
						int x = (int)motionEvent.getX();
						int y = (int)motionEvent.getY();
						int color;

						// Must check for null pointer cause "Droid" report
						// this error once in the market developer website
						try {
							color = Utils.findColor(view, x, y);
						} catch (NullPointerException e) {
							return false;
						}
						
						// Check if ralColor already exist as RalColor
						if (ralColor == null) { 
							ralColor = new RalColor(color);
						} else {
							ralColor.setColor(color);
						}
						
						showDialog(DIALOG_RESULT_ID);
				}
				return false;
			}
		});
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	super.onCreateDialog(id);
    	
    	Dialog dialog;
    	
    	switch(id) {
    	case DIALOG_RESULT_ID:
    		dialog = new Dialog(this);
			dialog.setContentView(R.layout.result_layout);
    		break;
    	default:
    		dialog = null;
    	}
    	return dialog;
    }
    
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
    	super.onPrepareDialog(id, dialog);
    	
    	// I really don't know if Android calls onPrepareDialog when
    	// dialog is null.
    	if ((dialog == null) || (ralColor == null)) {
    		return;
    	}
    	
    	switch(id) {
    	case DIALOG_RESULT_ID:
    		int index = 0;
    		int red = Color.red(ralColor.getColor());
    		int green = Color.green(ralColor.getColor());
    		int blue = Color.blue(ralColor.getColor());
			
    		// Set the color name from localized resource
			index = ralColor.getIndex();
			String[] colorNames = getResources().getStringArray(R.array.color_names);
			try {
				dialog.setTitle(colorNames[index]);
			} catch (ArrayIndexOutOfBoundsException e) {
				// FIXME: something was wrong with indexes. Maybe there is one
				// more color than color names :-?
				// Maybe the user can see odd color names!! Please test!!
				dialog.setTitle(colorNames[colorNames.length-1]);
			}
			
			ImageView imageViewColor = (ImageView)dialog.findViewById(R.id.ImageViewColor);
			imageViewColor.setBackgroundColor(ralColor.getColor());
			
			TextView textViewRal = (TextView)dialog.findViewById(R.id.TextViewRal);
			textViewRal.setText(
				"RAL: ".concat(Integer.toString(ralColor.getCode(), 10)));
			
			TextView textViewRgb = (TextView)dialog.findViewById(R.id.TextViewRgb);
			textViewRgb.setText(
				"RGB: ".concat(Integer.toString(red , 10)).
				concat(", ").concat(Integer.toString(green, 10)).
				concat(", ").concat(Integer.toString(blue, 10)));
				
			TextView textViewHex = (TextView)dialog.findViewById(R.id.TextViewHex);
			textViewHex.setText(
				"HEX: #".concat(Utils.beautyHexString(Integer.toHexString(red))).
				concat(Utils.beautyHexString(Integer.toHexString(green))).
				concat(Utils.beautyHexString(Integer.toHexString(blue))));
    		break;
    	default:
    		break;
    	}
    }
    
    @Override 
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    }
}
