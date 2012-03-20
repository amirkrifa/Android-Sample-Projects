package com.MoBEEVents;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class ContentAdapter extends SimpleAdapter {

	private List<Content> contents;
	/*
	* Alternating color list -- you could initialize this from anywhere.
	* Note that the colors make use of the alpha here, otherwise they would be
	* opaque and wouldn't give good results!
	*/
	private int[] colors = new int[] {  0x00eaea, 0x00ffff };

	public ContentAdapter(Context context, 
			List<? extends HashMap<String, String>> contents, 
	        int resource, 
	        String[] from, 
	        int[] to) 
	{
	  super(context, contents, resource, from, to);
	  this.contents = (List<Content>) contents;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	  View view = super.getView(position, convertView, parent);

	  int colorPos = position % colors.length;
	  view.setBackgroundColor(colors[colorPos]);
	  
	  return view;
	}

	}