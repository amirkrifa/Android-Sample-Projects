package se.robertfoss.ChanImageBrowser;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	int mGalleryItemBackground;
	private Context mContext;

	private ArrayList<Bitmap> thumbnails;
	private ArrayList<File> fileList;
	private LinkedList<File> bufferFileList;
	private static final int TARGET_HEIGHT = 150;
	private static final int TARGET_WIDTH = 120;
	private int maxImagesToDisplay;

	public ImageAdapter(Context c, int maxImagesToDisplay) {
		super();
		thumbnails = new ArrayList<Bitmap>();
		fileList = new ArrayList<File>();
		bufferFileList = new LinkedList<File>();
		mContext = c;
		this.maxImagesToDisplay = maxImagesToDisplay;
	}

	public int getCount() {
		return thumbnails.size();
	}

	public File getItem(int position) {
		return fileList.get(position);
	}

	public void deleteXFirstImages(int number){
		number = thumbnails.size() < number ? thumbnails.size() : number ;
		
		for (int i = 0; i < number; i++){
			thumbnails.remove(i);
			fileList.remove(i);
		}
		
		ArrayList<Bitmap> newThumbnails = new ArrayList<Bitmap>();
		ArrayList<File> newFileList = new ArrayList<File>();
		newThumbnails.addAll(thumbnails);
		newFileList.addAll(fileList);
		thumbnails = newThumbnails;
		fileList = newFileList;
		ImageAdapter.this.notifyDataSetChanged();
	}
	
	public void clearContents(){
		thumbnails = new ArrayList<Bitmap>();
		fileList = new ArrayList<File>();
		ImageAdapter.this.notifyDataSetChanged();
	}
	
	public void addItem(File file) {
		if (getCount() >= maxImagesToDisplay){
			bufferFileList.add(file);
		} else if (!bufferFileList.isEmpty()){
			privAddItem(bufferFileList.poll());
			bufferFileList.add(file);
		} else {
			privAddItem(file);
		}
	}
	
	private void privAddItem(File file) {
		if (!fileList.contains(file)) {
			Viewer.printDebug("Adding image to adapter - " + file.toString());

			Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			BitmapFactory.decodeFile(file.toString(), options);
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
				Bitmap img = BitmapFactory.decodeFile(file.toString(), options);

				fileList.add(file);
				thumbnails.add(img);
				ImageAdapter.this.notifyDataSetChanged();
			} else {
				Viewer.printDebug("	Image is invalid" + file.toString());
			}
		}
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(TARGET_WIDTH,
					TARGET_HEIGHT));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			//imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}

		imageView.setImageBitmap(thumbnails.get(position));

		return imageView;

	}
}