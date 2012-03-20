package com.MoBEEVents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.location.Location;
import android.util.Log;

public class Content extends HashMap<String, String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String CONTENT_ID = "c_remote_id";
	public static String CONTENT_TEXT = "c_text";
	public static String CONTENT_IMAGE = "c_image";
	public static String CONTENT_AUDIO = "c_audio";
	public static String CONTENT_VIDEO = "c_video";
	public static String CONTENT_EXPIRATION_DATE = "c_expiration_date";
	public static String CONTENT_LATITUDE = "c_latitude";
	public static String CONTENT_LONGITUDE = "c_longitude";
	public static String CONTENT_PUBLISHER = "c_publisher";
	public static String CONTENT_RANK = "c_rank";
	public static String CONTENT_RECEPTION_DATE = "c_reception_date";
	public static String CONTENT_DISTANCE = "c_distance";
	public static String CONTENT_CATEGORY = "c_category";


	private String c_id;
	private String c_text;
	private String c_image;
	private String c_audio;
	private String c_video;
	private String c_expiration_date;
	private Double c_latitude;
	private Double c_longitude;
	private String c_publisher;
	private int c_rank;
	private String c_reception_date;
	private float c_distance;
	private String c_category;
	
	public Content(String id, String text, String img, String audio, String video, String exp, double lat, double Long, String pub, int rank, String recp, float dist, String cat)
	{
		c_category = cat;
		c_id = id;
		c_text = text;
		c_image = img;
		c_audio = audio;
		c_video = video;
		c_expiration_date = exp;
		c_latitude = lat;
		c_longitude = Long;
		c_publisher = pub;
		c_rank = rank;
		c_reception_date = recp;
		c_distance = dist;
	}

	/**
	 * 
	 * @return The content date according to the format dd/MM/yyyy
	 */
	private String getContentDate()
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date conDate = null;
		try {
			conDate = df.parse(c_expiration_date);
		} catch (ParseException e) {
			Log.i("getContentDate ", "Error occured when parsing the content date: "+c_expiration_date);
		}
		// Calculating how many days left to the event
		Date today = new Date();
		
		 // Get msec from each, and subtract.
	    Long diff = today.getTime() - conDate.getTime();
		
	    // Diff in days:
	    Long diffDays = diff / (1000 * 60 * 60 * 24);
		
		return "dans "+diffDays.toString(); 
	}

	private String getDistanceFrom()
	{
		float [] result = new float[10];
		Location.distanceBetween(MoBEEVents.currentUserLocation.getLatitude(), MoBEEVents.currentUserLocation.getLongitude(), c_latitude, c_longitude, result);
		c_distance = result[0];
		return String.valueOf(result[0]);
	}
	
	public int getLatitudeE6()
	{
		return (int)(c_latitude * 1E6);
	}

	public int getLongitudeE6()
	{
		return (int)(c_longitude * 1E6);
	}

	@Override
	public String get(Object k) {

		String key = (String) k;

		if (CONTENT_AUDIO.equals(key))
			return c_audio;
		else if (CONTENT_EXPIRATION_DATE.equals(key))
			return c_expiration_date;
		else if(CONTENT_ID.equals(key))
			return c_id;
		else if(CONTENT_IMAGE.equals(key))
			return c_image;
		else if(CONTENT_LATITUDE.equals(key))
			return c_latitude.toString();
		else if(CONTENT_LONGITUDE.equals(key))
			return c_longitude.toString();
		else if(CONTENT_PUBLISHER.equals(key))
			return c_publisher;
		else if(CONTENT_RANK.equals(key))
			return String.valueOf(c_rank);
		else if(CONTENT_RECEPTION_DATE.equals(key))
			return c_reception_date;
		else if(CONTENT_TEXT.equals(key))
			return c_text;
		else if(CONTENT_TEXT.equals(key))
			return c_text;
		else if(CONTENT_VIDEO.equals(key))
			return c_video;
		else if(CONTENT_DISTANCE.equals(key))
			return getDistanceFrom() + " m";
		else if (CONTENT_EXPIRATION_DATE.equals(key))
			return getContentDate();
		else if (CONTENT_CATEGORY.equals(key))
			return c_category;

		return null;
	}

}


