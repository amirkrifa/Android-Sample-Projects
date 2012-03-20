package com.MoBEEVents;

import java.util.HashMap;
import java.util.List;

import com.mobimagnet.R;
import com.mobimagnet.R.id;
import com.mobimagnet.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Categories extends Activity {

	private ListView categoriesListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(com.mobimagnet.R.layout.listcategories);


		//Récupération de la listview créée dans le fichier main.xml
		categoriesListView = (ListView) findViewById(com.mobimagnet.R.id.categoriesList);

		// Create our own version of the list adapter
		List<Category> categories = DatabaseHelper.getDBManager().getAllCategories();

		ListAdapter adapter = new CategoryAdapter(this, categories,
				com.mobimagnet.R.layout.categoryresume, new String[] {
				Category.CATEGORY_IMAGE,  Category.CATEGORY_NAME ,  Category.CATEGORY_NUMBER_AVAILABLE_EVENTS},  
				new int[] {com.mobimagnet.R.id.categoryImg, com.mobimagnet.R.id.categoryTitle, com.mobimagnet.R.id.categoryNbrEvents});

		categoriesListView.setAdapter(adapter);

		//on met un écouteur d'évènement sur notre listView
		categoriesListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				//on récupère la HashMap contenant les infos de notre item (titre, description, img)
				HashMap<String, String> map = (HashMap<String, String>) categoriesListView.getItemAtPosition(position);

				// Start an activity that shows the details with respect to the selected content
				// Create the bundle that will hot the details of the content
				Bundle categoryDetails = new Bundle();
				categoryDetails.putString(Category.CATEGORY_NAME, map.get(Category.CATEGORY_NAME));

				// Crate the intent that will be used to start the ContentDetails activity
				Intent goToDetails = new Intent(Categories.this, Contents.class);
				// Add the details bundle to the intent
				goToDetails.putExtras(categoryDetails);

				startActivity(goToDetails);
			}
		});
	}
}
