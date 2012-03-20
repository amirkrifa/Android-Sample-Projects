package com.MoBEEVents;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.mobimagnet.R;
import com.mobimagnet.R.drawable;

import android.content.ContentValues;
import android.content.Context;
import android.database.*;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {


	// Contents Table
	private static final String CONTENT_ID = "c_remote_id";
	private static final String CONTENT_TEXT = "c_text";
	private static final String CONTENT_IMAGE = "c_image";
	private static final String CONTENT_AUDIO = "c_audio";
	private static final String CONTENT_VIDEO = "c_video";
	private static final String CONTENT_EXPIRATION_DATE = "c_expiration_date";
	private static final String CONTENT_LATITUDE = "c_latitude";
	private static final String CONTENT_LONGITUDE = "c_longitude";
	private static final String CONTENT_PUBLISHER = "c_publisher";
	private static final String CONTENT_RANK = "c_rank";
	private static final String CONTENT_RECEPTION_DATE = "c_reception_date";
	private static final String CONTENT_DISTANCE = "c_distance";
	private static final String CONTENT_CATEGORY = "c_category";
	
	//Statistics Table
	private static final String STAT_ID = "s_remote_id";
	private static final String CONTENT_APPRECIATION = "c_appreciation";

	// Categories Table
	private static final String CATEGORY_NAME = "c_name";
	private static final String CATEGORY_NBR_CONTENTS = "c_nbr_contents";
	private static final String CATEGORY_IMAGE = "c_image";
	
	// Database related constants    
	private static final String DATABASE_NAME = "MobiMagnetDB";
	private static String DB_PATH = "/data/data/com.mobimagnet/databases/";
	private static final String DATABASE_STATISTICS_TABLE = "statistics";
	private static final String DATABASE_CONTENTS_TABLE = "contents";
	private static final String DATABASE_CATEGORIES_TABLE = "categories";
	private final Context myContext;
	private SQLiteDatabase db;
	private static final String commonCategory = "Tous";

	private static DatabaseHelper dbManager = null;

	public static void CreateOpenDBManager(Context ctx)
	{
		if(dbManager == null){
			dbManager = new DatabaseHelper(ctx);
			try
			{
				dbManager.createDataBase();
				dbManager.openDataBase();
			}
			catch(SQLException  ioe)
			{
			} 

			catch (IOException e) {
			}
		}
	}
	
	public void insertTestRecords()
	{
		// We start by inserting the categories then the contents
		dbManager.insertCategoriesForTest();
		// Adding the contents
		dbManager.insertContentsForTest();
	}
	
	public static DatabaseHelper getDBManager()
	{
		
		if(!dbManager.IsOpen())
		{
			dbManager.openDataBase();
		}

		return dbManager;
	}

	/**
	 * Constructor
	 * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	 * @param context
	 */
	public DatabaseHelper(Context context) {

		super(context, DATABASE_NAME, null, 1);
		this.myContext = context;
	}	

	/**
	 * Creates a empty database on the system and rewrites it with your own database.
	 * */
	private void createDataBase() throws IOException{

		boolean dbExist = checkDataBase();

		if(dbExist){

			//database already exist - delete the old database
			myContext.deleteDatabase(DB_PATH + DATABASE_NAME);
			//copy the new one
			copyDataBase();

			Log.i("createDataBase: ", "The database already exist, do nothing");
		}else
		{
			//By calling this method and empty database will be created into the default system path
			//of your application so we are gonna be able to overwrite that database with our database.
			this.getReadableDatabase();
			copyDataBase();
			Log.i("createDataBase: ", "A new database has been copied");
		}


	}  

	/**
	 * Check if the database already exist to avoid re-copying the file each time you open the application.
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase(){

		SQLiteDatabase checkDB = null;

		try{
			String myPath = DB_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY|SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		}catch(SQLiteException e){

			//database does't exist yet.
		}

		if(checkDB != null){
			Log.i("checkDataBase: ", "The database already exist, closing it");
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created empty database in the
	 * system folder, from where it can be accessed and handled.
	 * This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException{

		//Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
		if(myInput != null)
			Log.i("CopyDatabase: ", "Input stream loaded");
		else
			Log.i("CopyDatabase: ", "Input stream not loaded");

		// Path to the just created empty db
		String outFileName = DB_PATH + DATABASE_NAME;

		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		if(myOutput != null)
			Log.i("CopyDatabase: ", "Output stream loaded");
		else
			Log.i("CopyDatabase: ", "Output stream not loaded");


		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0){
			myOutput.write(buffer, 0, length);
		}

		Log.i("copyDataBase: ", "New database copied.");

		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException{

		//Open the database
		String myPath = DB_PATH + DATABASE_NAME;
		db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	}

	public boolean IsOpen()
	{
		return db.isOpen();
	}

	@Override
	public synchronized void close() {

		if(db != null)
			db.close();

		super.close();
	}

	public long insertCategory(String category, long nbr_contents, String img)
	{
		if(!isCategoryAvailable(category))
		{
			ContentValues values = new ContentValues();

			values.put(CATEGORY_NAME, category);
			values.put(CATEGORY_NBR_CONTENTS, nbr_contents);
			values.put(CATEGORY_IMAGE, img);
			
			Long ret = db.insert(DATABASE_CATEGORIES_TABLE, null, values); 
			Log.i("insertCategory: ", ret.toString() + " new category inserted.");
			return ret; 

		}
		else
		{
			Log.i("insertCategory: ", "category already available.");
			return -1;	
		}
		
	}

	//---insert a new content into the database---
	public long insertContent(int contentId, String contentText, String contentImage, String contentAudio, String contentVideo, String contentExpirationDate, double contentLatitude, double contentLongitude, String contentPublisher, int contentRank, String contentReceptionDate, long distance, String cat) 
	{
		if(!isContentAvailable(contentId))
		{
			ContentValues values = new ContentValues();

			values.put(CONTENT_AUDIO, contentAudio);
			values.put(CONTENT_EXPIRATION_DATE, contentExpirationDate);
			values.put(CONTENT_ID, contentId);
			values.put(CONTENT_IMAGE, contentImage);
			values.put(CONTENT_LATITUDE, contentLatitude);
			values.put(CONTENT_LONGITUDE, contentLongitude);
			values.put(CONTENT_PUBLISHER, contentPublisher);
			values.put(CONTENT_RANK, contentRank);
			values.put(CONTENT_RECEPTION_DATE, contentReceptionDate);
			values.put(CONTENT_TEXT, contentText);
			values.put(CONTENT_VIDEO, contentVideo);
			values.put(CONTENT_DISTANCE, distance);
			values.put(CONTENT_CATEGORY, cat);
			
			Long ret = db.insert(DATABASE_CONTENTS_TABLE, null, values); 
			Log.i("insertContent: ", ret.toString() + " new contents inserted.");
			
			// Updates the associated category number of contents
			long nbrAAC = getCategoryNbrContents(cat);
			updateCategoryNbrContents(cat, nbrAAC+1);
		
			// Updates the all category
			long tNbr = getCategoryNbrContents(commonCategory);
			updateCategoryNbrContents(commonCategory, tNbr + 1);
		
			return ret; 

		}
		else
		{
			Log.i("insertContent: ", "content already available.");
			return -1;	
		}
	}

	public long insertStatEntry(String statId, int contentAppreciation) 
	{
		ContentValues values = new ContentValues();
		values.put(STAT_ID, statId);
		values.put(CONTENT_APPRECIATION, contentAppreciation);
		return db.insert(DATABASE_CONTENTS_TABLE, null, values);
	}

	//---deletes a particular Content---
	public boolean deleteTitle(String contentId) 
	{
		return db.delete(DATABASE_CONTENTS_TABLE, CONTENT_ID + "=" + contentId, null) > 0;
	}


	//---retrieves all the contents---
	public List<Content> getAllContents() 
	{
		Cursor cur = db.query(false, DATABASE_CONTENTS_TABLE, new String[] {CONTENT_ID, CONTENT_TEXT, CONTENT_IMAGE, CONTENT_AUDIO, CONTENT_VIDEO, CONTENT_EXPIRATION_DATE, CONTENT_LATITUDE, CONTENT_LONGITUDE, CONTENT_PUBLISHER, CONTENT_RANK, CONTENT_RECEPTION_DATE, CONTENT_DISTANCE, CONTENT_CATEGORY}, null, null, null, null, null, null);
		List<Content> listContents = new ArrayList<Content>();
		cur.moveToFirst();
		while (cur.isAfterLast() == false) {
			//Content(String id, String text, String img, String audio, String video, String exp, double lat, double Long, String pub, int rank, String recp, String category)
			Content c = new Content(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5), cur.getDouble(6), cur.getDouble(7), cur.getString(8), cur.getInt(9), cur.getString(10), cur.getInt(11), cur.getString(12));
			listContents.add(c);
			cur.moveToNext();
		}
		cur.close();
		return listContents;
	}

	public List<Content> getContentsForCategory(String cat) 
	{
		if(cat.compareTo(commonCategory) == 0)
			return getAllContents();
		
		Cursor cur = db.query(false, DATABASE_CONTENTS_TABLE, new String[] {CONTENT_ID, CONTENT_TEXT, CONTENT_IMAGE, CONTENT_AUDIO, CONTENT_VIDEO, CONTENT_EXPIRATION_DATE, CONTENT_LATITUDE, CONTENT_LONGITUDE, CONTENT_PUBLISHER, CONTENT_RANK, CONTENT_RECEPTION_DATE, CONTENT_DISTANCE, CONTENT_CATEGORY},
		CONTENT_CATEGORY + "=" + "\"" + cat + "\"", null, null, null, null, null);
		List<Content> listContents = new ArrayList<Content>();
		cur.moveToFirst();
		while (cur.isAfterLast() == false) {
			//Content(String id, String text, String img, String audio, String video, String exp, double lat, double Long, String pub, int rank, String recp, String category)
			Content c = new Content(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5), cur.getDouble(6), cur.getDouble(7), cur.getString(8), cur.getInt(9), cur.getString(10), cur.getInt(11), cur.getString(12));
			listContents.add(c);
			cur.moveToNext();
		}
		cur.close();
		return listContents;
	}

	public List<Content> getTodayContents() 
	{
		// Getting today date 
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = new java.util.Date();
		String today = df.format(date);

		Cursor cur = db.query(false, DATABASE_CONTENTS_TABLE, new String[] {CONTENT_ID, CONTENT_TEXT, CONTENT_IMAGE, CONTENT_AUDIO, CONTENT_VIDEO, CONTENT_EXPIRATION_DATE, CONTENT_LATITUDE, CONTENT_LONGITUDE, CONTENT_PUBLISHER, CONTENT_RANK, CONTENT_RECEPTION_DATE, CONTENT_DISTANCE, CONTENT_CATEGORY}
		,CONTENT_EXPIRATION_DATE + "=" + "\"" + today + "\""
		, null, null, null, null, null);

		List<Content> listContents = new ArrayList<Content>();
		cur.moveToFirst();
		while (cur.isAfterLast() == false) {
			//Content(String id, String text, String img, String audio, String video, String exp, double lat, double Long, String pub, int rank, String recp, String category)
			Content c = new Content(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5), cur.getDouble(6), cur.getDouble(7), cur.getString(8), cur.getInt(9), cur.getString(10), cur.getInt(11), cur.getString(12));
			listContents.add(c);
			cur.moveToNext();
		}
		
		cur.close();
		return listContents;
	}
	
	// retrive all the available categories
	public List<Category> getAllCategories()
	{
		Cursor cur = db.query(false, DATABASE_CATEGORIES_TABLE, new String[] {CATEGORY_NAME, CATEGORY_NBR_CONTENTS, CATEGORY_IMAGE}, null, null, null, null, null, null);
		List<Category> listCategories = new ArrayList<Category>();
		cur.moveToFirst();
		while (cur.isAfterLast() == false) {
			Category c = new Category(cur.getString(0), cur.getString(2), cur.getLong(1));
			listCategories.add(c);
			cur.moveToNext();
		}
		cur.close();
		
		return listCategories;
	}

	//---retrieves all the stat entries---
	public Cursor getAllStatEntries() 
	{
		return db.query(false, DATABASE_STATISTICS_TABLE, new String[] {STAT_ID, CONTENT_APPRECIATION}, null, null, null, null, null, null);
	}

	//---updates a Content Appreciation Level ---
	public boolean updateContentAppreciation(String contentId, int appreciation) 
	{
		ContentValues args = new ContentValues();
		args.put(CONTENT_APPRECIATION, appreciation);
		return db.update(DATABASE_STATISTICS_TABLE, args, STAT_ID + "=" + contentId, null) > 0;
	}

	//---updates a Content Appreciation Level ---
	public boolean updateCategoryNbrContents(String cat, long l) 
	{
		ContentValues args = new ContentValues();
		args.put(CATEGORY_NBR_CONTENTS, l);
		return db.update(DATABASE_CATEGORIES_TABLE, args, CATEGORY_NAME + "=" + "\"" + cat + "\"", null) > 0;
	}

	//--returns a Channel TTL
	public Cursor getContentAppreciation(String contentId) throws SQLException
	{
		Cursor mCursor =
			db.query(true, DATABASE_STATISTICS_TABLE, new String[] {
					CONTENT_APPRECIATION
			}, 
			STAT_ID + "=" + contentId, 
			null,
			null, 
			null, 
			null, 
			null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	// Verifies if a given content entry is available within the database
	private boolean isContentAvailable(int contentId) throws SQLException
	{
		try
		{
			Cursor mCursor =
			db.query(true, DATABASE_CONTENTS_TABLE, new String[] {
					CONTENT_PUBLISHER
			}, 
			CONTENT_ID + "=" + contentId, 
			null,
			null, 
			null, 
			null, 
			null);

			return mCursor.getCount() != 0;
		}
		catch(SQLiteException e)
		{
			Log.i("isContentAvailable", "Exception occured while trying to find a content");
			return false;
		}
	}
	
	private boolean isCategoryAvailable(String category)
	{
		try
		{
			Cursor mCursor =
			db.query(true, DATABASE_CATEGORIES_TABLE, new String[] {
					CATEGORY_NBR_CONTENTS
			}, 
			CATEGORY_NAME + "=" + "\"" +category+"\"", 
			null,
			null, 
			null, 
			null, 
			null);
			
			Log.i("isCategoryAvailable", "Query done");
			return mCursor.getCount() != 0;
		}
		catch (SQLiteException e)
		{
			Log.i("isCategoryAvailable", "Exception occured while trying to find a category");
			e.printStackTrace();
			return false;
		}
	}
	
	public long getCategoryNbrContents(String cat)
	{
		try{
			
			Cursor mCursor =
			db.query(true, DATABASE_CATEGORIES_TABLE, new String[] {
					CATEGORY_NBR_CONTENTS
			}, 
			CATEGORY_NAME + "=" + "\""+ cat+"\"", 
			null,
			null, 
			null, 
			null, 
			null);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			return mCursor.getLong(0);
		}
		
		catch(SQLiteException e)
		{
			Log.i("getCategoryNbrContents", " error occured while trying to retreive the number of available contents");
			e.printStackTrace();
			return 0;
		}
	}

	// Verifies if a given stat entry is available within the database
	public boolean isStatAvailable(int statId) throws SQLException
	{
		Cursor mCursor =
			db.query(true, DATABASE_CONTENTS_TABLE, new String[] {
					STAT_ID
			}, 
			STAT_ID + "=" + statId, 
			null,
			null, 
			null, 
			null, 
			null);

		return mCursor != null;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	private void insertContentsForTest()
	{
		//int contentId, String contentText, String contentImage, String contentAudio, String contentVideo, String contentExpirationDate, double contentLatitude, double contentLongitude, String contentPublisher, int contentRank, String contentReceptionDate, in distance) 
		dbManager.insertContent(1, "Concert a ne pas rater ;)", String.valueOf(com.mobimagnet.R.drawable.alicia), "audio", "video", "15/12/2010", 43.37, 7.2, "Office de tourisme Nice", 1, "12/12/2012",300, "Concerts");
		dbManager.insertContent(2, "Concert a ne pas rater ;)", String.valueOf(com.mobimagnet.R.drawable.event), "audio", "video", "15/12/2010", 43.27, 7.1, "Office de tourisme BBBBB", 1, "12/12/2012",300,"Grand Spectacles");
		dbManager.insertContent(3, "Concert a ne pas rater ;)", String.valueOf(com.mobimagnet.R.drawable.event2), "audio", "video", "15/12/2010", 45.27, 7.1, "Office de tourisme Nice", 1, "12/12/2012",300, "Dance");
		dbManager.insertContent(4, "Concert a ne pas rater ;)", String.valueOf(com.mobimagnet.R.drawable.bora), "audio", "video", "15/12/2010", 43.27, 7.3, "Office de tourisme Nice", 1, "12/12/2012",300, "Cinema");

		dbManager.insertContent(5, "Concert a ne pas rater ;)", String.valueOf(com.mobimagnet.R.drawable.bora), "audio", "video", "13/12/2010", 43.27, 7.3, "Office de tourisme Nice", 1, "12/12/2012",300, "Sport");
		dbManager.insertContent(6, "Concert a ne pas rater ;)", String.valueOf(com.mobimagnet.R.drawable.bora), "audio", "video", "13/12/2010", 43.27, 7.3, "Office de tourisme Nice", 1, "12/12/2012",300, "Sport");
		dbManager.insertContent(7, "Concert a ne pas rater ;)", String.valueOf(com.mobimagnet.R.drawable.bora), "audio", "video", "13/12/2010", 43.27, 7.3, "Office de tourisme Nice", 1, "12/12/2012",300, "Sport");

	}

	private void insertCategoriesForTest()
	{
		dbManager.insertCategory(commonCategory, 0, String.valueOf(com.mobimagnet.R.drawable.concert));
		dbManager.insertCategory("Concerts", 0, String.valueOf(com.mobimagnet.R.drawable.clubbing));
		dbManager.insertCategory("Grand Spectacles",0, String.valueOf(com.mobimagnet.R.drawable.conference));
		dbManager.insertCategory("Dance",0, String.valueOf(com.mobimagnet.R.drawable.concert));
		dbManager.insertCategory("Cinema",0, String.valueOf(com.mobimagnet.R.drawable.clubbing));

		dbManager.insertCategory("Clubbing",0, String.valueOf(com.mobimagnet.R.drawable.conference));
		dbManager.insertCategory("Humour",0, String.valueOf(com.mobimagnet.R.drawable.conference));
		dbManager.insertCategory("Theatre",0, String.valueOf(com.mobimagnet.R.drawable.conference));
		dbManager.insertCategory("Sport",0, String.valueOf(com.mobimagnet.R.drawable.conference));
		dbManager.insertCategory("Grand Spectacles",0, String.valueOf(com.mobimagnet.R.drawable.conference));
		dbManager.insertCategory("Danse",0, String.valueOf(com.mobimagnet.R.drawable.conference));
		dbManager.insertCategory("Salon",0, String.valueOf(com.mobimagnet.R.drawable.conference));

	}
	
	private  Address convertAddressToPosition(String adr)
	{
		Geocoder gc =  new Geocoder(myContext);
		List<Address> foundAdresses = new ArrayList<Address>();
		Address selectedAddress = null;
		try {
			foundAdresses = gc.getFromLocationName(adr, 5);

			if (foundAdresses.size() == 0) { 
				Log.i("DatabaseHelper", " O coordinates were found for the address:"+adr);
					
			}else { //else display address on map
				// Show the first result
				selectedAddress = foundAdresses.get(1);
				/*for (int i = 0; i < foundAdresses.size(); ++i) {
					//Save results as Longitude and Latitude
					//@todo: if more than one result, then show a select-list
					Address x = foundAdresses.get(i);
					lat = x.getLatitude();
					lon = x.getLongitude();
				}
				 */
			} 
		}
		catch (IOException e) {
			Log.i("DatabaseHelper", " Exception occured when trying to resolve the adr to Address oject");
			e.printStackTrace();
		}
		
		return selectedAddress;
	}
}

