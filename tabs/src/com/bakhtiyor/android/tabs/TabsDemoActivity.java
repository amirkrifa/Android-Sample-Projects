package com.bakhtiyor.android.tabs;

import android.app.TabActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class TabsDemoActivity extends TabActivity implements TabHost.TabContentFactory {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TabHost tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("ebay").setIndicator("eBay").setContent(this));
		tabHost.addTab(tabHost.newTabSpec("flickr").setIndicator("Flickr").setContent(this));
		tabHost.addTab(tabHost.newTabSpec("skype").setIndicator("skype").setContent(this));
		tabHost.addTab(tabHost.newTabSpec("you_tube").setIndicator("YouTube").setContent(this));
		setupUI();
	}

	@Override
	public View createTabContent(String tag) {
		TextView tv = new TextView(this);
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(20);
		if (tag.equals("flickr")) {
			tv.setText(R.string.flickr);
		} else if (tag.equals("ebay")) {
			tv.setText(R.string.ebay);
		} else if (tag.equals("skype")) {
			tv.setText(R.string.skype);
		} else if (tag.equals("you_tube")) {
			tv.setText(R.string.you_tube);
		}
		return tv;
	}

	private void setupUI() {
		RadioButton rbFirst = (RadioButton) findViewById(R.id.first);
		RadioButton rbSecond = (RadioButton) findViewById(R.id.second);
		RadioButton rbThird = (RadioButton) findViewById(R.id.third);
		RadioButton rbFourth = (RadioButton) findViewById(R.id.fourth);
		rbFirst.setButtonDrawable(R.drawable.ebay);
		rbSecond.setButtonDrawable(R.drawable.flickr);
		rbThird.setButtonDrawable(R.drawable.skype);
		rbFourth.setButtonDrawable(R.drawable.you_tube);
		RadioGroup rg = (RadioGroup) findViewById(R.id.states);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, final int checkedId) {
				switch (checkedId) {
				case R.id.first:
					getTabHost().setCurrentTab(0);
					break;
				case R.id.second:
					getTabHost().setCurrentTab(1);
					break;
				case R.id.third:
					getTabHost().setCurrentTab(2);
					break;
				case R.id.fourth:
					getTabHost().setCurrentTab(3);
					break;
				}
			}
		});
	}

}