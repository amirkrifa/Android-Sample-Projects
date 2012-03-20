package se.robertfoss.ChanImageBrowser;

import java.io.File;
import java.util.ArrayList;

import se.robertfoss.ChanImageBrowser.Fetcher.FetcherManager;
import se.robertfoss.MultiTouch.TouchImageView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class Viewer extends Activity {

	public static final boolean isDebug = true;
	public static final File tempDir = new File(Environment
			.getExternalStorageDirectory(), "/4Chan/temp/");
	public static final File baseDir = new File(Environment
			.getExternalStorageDirectory(), "/4Chan/");
	
	public static final int NBR_IMAGES_TO_DOWNLOAD_DIRECTLY = 45; //16
	public static final int NBR_IMAGES_TO_DOWNLOAD_INCREMENT = 16; //16
	public static final int NBR_IMAGES_TO_DOWNLOAD_AHEAD = 30; //32
	public static final int NBR_IMAGES_TO_DISPLAY_MAX = 75; //38
	
	private static final String SEED_URL = "http://img.4chan.org/b/imgboard.html";
	private static final String PAGE_REGEX = "\\<a href=\\\"res/([0-9]){1,20}";
	private static final int PAGE_REGEX_TRUNCATE_TO_INDEX = 14;
	private static final String PREPEND_TO_RELATIVE_PAGE_URL = "http://boards.4chan.org/b/res/";
	private static final String IMAGE_REGEX = "http://images.4chan.org/b/src/([0-9])*.(jpg|png)";
	private static final int IMAGE_REGEX_TRUNCATE_TO_INDEX = 30;
	private static final String PREPEND_TO_RELATIVE_IMAGE_URL = "http://images.4chan.org/b/src/";
	
	
	// Parse images from reddit.com/pics
//	 private static final String SEED_URL = "http://www.reddit.com/r/pics/";
//	 private static final String IMAGE_REGEX = "http://imgur.com/([A-Za-z0-9]*.jpg|png)";
//	 private static final String PREPEND_TO_RELATIVE_IMAGE_URL = "http://imgur.com/";
//	 private static final String PAGE_REGEX = "http://www.reddit.com/r/(\\S+)\"";
//	 private static final String PREPEND_TO_RELATIVE_PAGE_URL = "http://www.reddit.com/r/";
	
	private ArrayList<File> fileList;
	private GridView gridView;
	private ImageAdapter imgAdapter;
	private FetcherManager man;
	
	private static final int MENU_RELOAD = 0;
	//private static final int MENU_MORE = 1;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		printDebug("onCreate()");
	
		
		fileList = new ArrayList<File>();
		imgAdapter = new ImageAdapter(this, NBR_IMAGES_TO_DISPLAY_MAX);

		
		printDebug("Initializing manager thread");
		man = new FetcherManager(this,
				NBR_IMAGES_TO_DOWNLOAD_DIRECTLY,
				SEED_URL, PAGE_REGEX, PAGE_REGEX_TRUNCATE_TO_INDEX,
				PREPEND_TO_RELATIVE_PAGE_URL, 
				IMAGE_REGEX, IMAGE_REGEX_TRUNCATE_TO_INDEX,
				PREPEND_TO_RELATIVE_IMAGE_URL);

		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(imgAdapter);
		
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				printDebug("Image " +  position + " was long-clicked!");
				printDebug("Trying to open file: " + Uri.parse("file:/"+ (File) imgAdapter.getItem(position)));
				Intent sendIntent = new Intent(Intent.ACTION_SEND);
			    sendIntent.setType("image/*");
			    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile((File) imgAdapter.getItem(position)));
			    sendIntent.putExtra(Intent.EXTRA_TEXT, "Image found with 4chan Image Browser");
			    startActivity(Intent.createChooser(sendIntent, "Email:"));

				return false;
			}
			
		});
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				printDebug("Image " + position + " was clicked!");
				
				File file = (File) imgAdapter.getItem(position);
				printDebug("	Expanding image: " +  file.getAbsoluteFile());
				Intent i = new Intent(Viewer.this, ExpandImage.class);
				i.putExtra("fileURI", file.getAbsoluteFile().toString());
				
				Viewer.this.startActivity(i);
			}
		});

		printDebug("Running manager thread");
		man.execute();
		
	}

	public void setCurrentView(View view) {
		Viewer.this.setContentView(view);
	}

	
	@Override
	protected void onResume() {	
		super.onResume();
		printDebug("onResume()");
		man.setFetchersPause(false);
	}
	
	@Override
	protected void onRestart() {	
		super.onRestart();
		printDebug("onRestart()");
	}

	@Override
	protected void onPause() {
		super.onPause();
		printDebug("onPause()");
		man.setFetchersPause(true);
	}

	@Override
	public void onLowMemory(){
		super.onLowMemory();
		printDebug("onLowMemory()");
		Toast.makeText(this, "Pausing downloads due to low memory", Toast.LENGTH_SHORT);
		man.setFetchersPause(true);
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		printDebug("onStop()");
		man.setFetchersPause(true);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		printDebug("onDestroy()");
		man.setFetchersPause(true);
		
		if (man != null){
			man.destroyFetchers();
		}
		if (tempDir.exists()) {
			File[] files = tempDir.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		}
		man.destroyFetchers();
		man = null;
		super.finish();
		
		// Doesnt work on 1.6
		// Debug.stopMethodTracing();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		printDebug("onConfigurationChanged()");
	}
	
	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, MENU_RELOAD, 0, "Reload");
	    //menu.add(0, MENU_MORE, 0, "More");
	    return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case MENU_RELOAD:
	        imgAdapter.clearContents();
	        man.setFetchersPause(false);
	        man.downloadXAdditionalImages(NBR_IMAGES_TO_DOWNLOAD_DIRECTLY - man.getNumberOfImageToDownload());
	        return true;
	    /*case MENU_MORE:
	        int imagesForDisplay = man.getNbrImagesToDownload() + imgAdapter.getCount();
	        int imagesToAdd = NBR_IMAGES_TO_DISPLAY_MAX - imagesForDisplay;
	        imagesToAdd = imagesToAdd > 16 ? 16 : imagesToAdd;
	        return true;
	    */
	    }
	    return false;
	}

	public void addCompleteImage(File file) {

		Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(file.toString(), options);

		fileList.add(file);
		printDebug("New picture addded, " + file.toString()
				+ " for a total of " + fileList.size());
		addFileToAdapter(file);
	}

	private void addFileToAdapter(File file) {
		imgAdapter.addItem(new File(tempDir, file.toString()));

	}
	
	public void downloadMoreImages(int number){
		imgAdapter.deleteXFirstImages(number);
		man.downloadXAdditionalImages(number);
	}

	public synchronized static void printDebug(String str) {
		if (isDebug) {
			System.out.println(str);
		}
	}
}