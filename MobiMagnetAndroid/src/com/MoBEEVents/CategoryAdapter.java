package com.MoBEEVents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class CategoryAdapter extends SimpleAdapter{

	private List<Category> categories;
	private int[] colors = new int[] { 0x00eaea, 0x00ffff };

	public CategoryAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.categories = (List<Category>) data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		int colorPos = position % colors.length;
		view.setBackgroundColor(0xf9f4ea);
		return view;
	}
}
