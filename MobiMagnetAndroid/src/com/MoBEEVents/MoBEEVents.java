package com.MoBEEVents;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.MoBEEVents.AsyncFacebookRunner.RequestListener;
import com.MoBEEVents.Facebook.DialogListener;
import com.mobimagnet.R;
import com.mobimagnet.R.id;
import com.mobimagnet.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.widget.Button;



public class MoBEEVents extends Activity implements Facebook.DialogListener{

	private LocationListener locationListener;
	public static Location currentUserLocation = null;
	//the minimum time interval between notifications
	private int minTime = 0;
	//the minimum change in distance between notifications
	private int minDistance = 0;
	private static final int TWO_MINUTES = 1000 * 60 * 2;

	private Facebook fb;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(com.mobimagnet.R.layout.main);

		// Registering for position providers
		registerForLocationInfos();

		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new SyncClickListener());

		Button buttonfb = (Button) findViewById(R.id.fbconnect);
		buttonfb.setOnClickListener(new OnFacebookConnect());

		// Initialysing the database
		DatabaseHelper.CreateOpenDBManager(this);

		// Adding the contents for testing
		DatabaseHelper.getDBManager().insertTestRecords();

	}


	private class SyncClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			startActivity(new Intent(MoBEEVents.this, MoBEEVentsHome.class));

		}	    
	}

	private class OnFacebookConnect implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			// Testing fb
			fb = new Facebook("139153786137708");
			fb.authorize(MoBEEVents.this, new String[] {"publish_stream", "read_stream", "offline_access"}, MoBEEVents.this);
			try
			{
				fb.request("me"); // get information about the currently logged in user
				
				// Publishing something on the wall

				fb.logout(MoBEEVents.this);
			}

			catch(IOException e)
			{
			}

		}	    
	}

	/**
	 * Updates the current user location if the new one is more accurate
	 * @param nl the new location provided by the location providers
	 */
	private void updateUserLocation(Location nl)
	{
		if(isBetterLocation(nl, currentUserLocation))
		{	currentUserLocation = nl;
		Log.i("updateUserLocation", " current location: "+ currentUserLocation.getLongitude()+" "+currentUserLocation.getLatitude());
		}else
		{
			Log.i("updateUserLocation", " The new location is not better and more accurate then the new one.");
		}

	}

	@Override
	public void onStop() {
		super.onStop();
		// Unregister for the location providers
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(locationListener);
		Log.i("MobiMagnet::onStop", "Unregistering the location providers");
		// Close the database
		Log.i("MobiMagnet::onStop", "Closing the database");
		DatabaseHelper.getDBManager().close();

	}
	/**
	 * Registering for the network and gps location providers
	 */
	private void registerForLocationInfos()
	{
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		currentUserLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		Log.i("registerForLocationInfos", " current location: "+ currentUserLocation.getLongitude()+" "+currentUserLocation.getLatitude());
		// Define a listener that responds to location updates
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				updateUserLocation(location);
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}

			public void onProviderEnabled(String provider) {}

			public void onProviderDisabled(String provider) {}
		};

		// Register the listener with the Location Manager to receive location updates
		// Registering for network provider
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, locationListener);
		// Registering for gps provider
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener);

	}

	/** Determines whether one Location reading is better than the current Location fix
	 * @param location  The new Location that you want to evaluate
	 * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	 */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two location providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}



	@Override
	public void onComplete(Bundle values) {
		// TODO Auto-generated method stub
		//  Handle a successful login

        if (values.isEmpty())
        {
            //"skip" clicked ?
            return;
        }

        // if facebookClient.authorize(...) was successful, this runs
        // this also runs after successful post
        // after posting, "post_id" is added to the values bundle
        // I use that to differentiate between a call from
        // faceBook.authorize(...) and a call from a successful post
        // is there a better way of doing this?
        if (!values.containsKey("post_id"))
        {
            try
            {
                Bundle parameters = new Bundle();
                parameters.putString("message", "this is a test");// the message to post to the wall
                fb.dialog(this, "stream.publish", parameters, this);// "stream.publish" is an API call
            }
            catch (Exception e)
            {
                // TODO: handle exception
                System.out.println(e.getMessage());
            }
        }
		
	}

	@Override
	public void onFacebookError(FacebookError e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(DialogError e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}
}
