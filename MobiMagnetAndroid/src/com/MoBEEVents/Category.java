package com.MoBEEVents;

import java.util.HashMap;

public class Category extends HashMap<String, String>{

	private static final long serialVersionUID = 1L;
	
	public static String CATEGORY_NAME = "c_name";
	public static String CATEGORY_IMAGE = "c_image";
	public static String CATEGORY_NUMBER_AVAILABLE_EVENTS = "c_number_events";
	
	private String c_name;
	private String c_image;
	private long c_number_events;
	
	public Category(String name, String image, long nbr_events)
	{
		c_name = name;
		c_image = image;
		c_number_events = nbr_events;
	}

	@Override
	public String get(Object k) {

		String key = (String) k;
		if (CATEGORY_NAME.equals(key))
			return c_name;
		else if (CATEGORY_IMAGE.equals(key))
			return c_image;
		else if (CATEGORY_NUMBER_AVAILABLE_EVENTS.equals(key))
			return "("+ String.valueOf(c_number_events) +" contenus)";

		return null;
	}
}
