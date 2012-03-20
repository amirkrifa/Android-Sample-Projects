package se.robertfoss.ChanImageBrowser;

import java.io.File;

import se.robertfoss.MultiTouch.TouchImageView;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ExpandImage extends Activity {

	private static final int TARGET_HEIGHT = 800;
	private static final int TARGET_WIDTH = 480;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expandimage);
		TouchImageView tiv = (TouchImageView) findViewById(R.id.touchimageview);

		String file = this.getIntent().getStringExtra("fileURI");

		Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(file, options);
		if (options.outHeight != -1 && options.outWidth != -1) {
			Viewer.printDebug("Image is valid - " + file.toString());
			// Only scale if we need to
			Boolean scaleByHeight = !(Math.abs(options.outHeight
					- TARGET_HEIGHT) >= Math.abs(options.outWidth
					- TARGET_WIDTH));

			// Load, scaling to smallest power of 2 that'll get it <=
			// desired
			// dimensions
			double sampleSize = scaleByHeight ? options.outHeight
					/ TARGET_HEIGHT : options.outWidth / TARGET_WIDTH;
			options.inSampleSize = (int) Math.pow(2d, Math.floor(Math
					.log(sampleSize)
					/ Math.log(2d)));

			// Do the actual decoding
			options.inJustDecodeBounds = false;
			Bitmap img = BitmapFactory.decodeFile(file, options);
			
			if (img == null){
				Viewer.printDebug("	ExpandImage: img is null");
			}
			
			tiv.setImage(img, tiv.getWidth(), tiv.getHeight());
			tiv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ExpandImage.this.finish();
				}
			});
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		Viewer.printDebug("onConfigurationChanged()");
	}

}
