package se.robertfoss.ChanImageBrowser.Fetcher;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import se.robertfoss.ChanImageBrowser.Viewer;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Process;

public class FetcherManager extends AsyncTask<String, Void, Void> {

	private ProgressDialog dialog;
	private HashMap<String, Boolean> visitedUrls;
	private LinkedList<String> imageUrlList;
	private LinkedList<String> linkUrlList;

	private IndexFetcher indexfetcher;
	private ArrayList<ImageFetcher> imagefetchers;
	private ArrayList<ThreadFetcher> threadfetchers;

	private final int MAX_IMAGEFETCHERS = 3;
	private final int MAX_THREADFETCHERS = 1;
	private Viewer parent;
	private int imagesToDownload;

	/**
	 * 
	 * @param view
	 *            - Parent view
	 * @param runType
	 *            - Type of run, first or any later run
	 * @param imagesToDownload
	 *            - The number of images to download
	 * @param linkTarget
	 *            - Urlpackage for the links
	 * @param imageTarget
	 *            - Urlpackage for the images
	 */
	public FetcherManager(Viewer view, int imagesToDownload, String seedUrl,
			String pageRegex, int pageRegexTruncateIndex,
			String prependToPageUrl, String imageRegex,
			int imageRegexTruncateIndex, String prependToImageUrl) {
		Viewer.printDebug("Creating Fetchers \n");
		parent = view;
		visitedUrls = new HashMap<String, Boolean>();
		imageUrlList = new LinkedList<String>();
		linkUrlList = new LinkedList<String>();
		imagefetchers = new ArrayList<ImageFetcher>();
		threadfetchers = new ArrayList<ThreadFetcher>();
		dialog = new ProgressDialog(view);

		this.imagesToDownload = imagesToDownload;

		indexfetcher = new IndexFetcher(this, seedUrl, pageRegex, pageRegexTruncateIndex,
				prependToPageUrl, imageRegex, imageRegexTruncateIndex, prependToImageUrl);
		indexfetcher.setName("IndexFetcher");
		indexfetcher.setPriority(Process.THREAD_PRIORITY_BACKGROUND);

		for (int i = 0; i < MAX_IMAGEFETCHERS; i++) {
			Viewer.printDebug("ImageFetcher-" + i + " created");
			ImageFetcher temp = new ImageFetcher(this);
			temp.setName("ImageFetcher-" + i);
			temp.setPriority(Process.THREAD_PRIORITY_BACKGROUND - 2
					* Process.THREAD_PRIORITY_LESS_FAVORABLE);
			imagefetchers.add(temp);
		}

		for (int i = 0; i < MAX_THREADFETCHERS; i++) {
			Viewer.printDebug("ThreadFetcher-" + i + " created");
			ThreadFetcher temp = new ThreadFetcher(this, imageRegex, imageRegexTruncateIndex,
					prependToImageUrl);
			temp.setName("ThreadFetcher-" + i);
			temp.setPriority(Process.THREAD_PRIORITY_BACKGROUND
					- Process.THREAD_PRIORITY_LESS_FAVORABLE);
			threadfetchers.add(temp);
		}
	}

	@Override
	protected void onPreExecute() {
		setDialogMessage("Fetching index..");
		enableDialog();
	}

	@Override
	public Void doInBackground(String... params) {

		indexfetcher.runOneIteration();
		setDialogMessage("Index fetched. Fetching images");

		for (int i = 0; i < imagefetchers.size(); i++) {
			Viewer.printDebug("ImageFetcher-" + i + " started");
			imagefetchers.get(i).start();
		}

		for (int i = 0; i < threadfetchers.size(); i++) {
			Viewer.printDebug("ThreadFetcher-" + i + " started");
			threadfetchers.get(i).start();
		}
		indexfetcher.start();

		// Wait until images can be downloaded
		do {
			try {
				Thread.sleep(750);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (imageUrlList.size() == 0);
		return null;
	}

	@Override
	protected void onPostExecute(Void param) {
		destroyDialog();
	}

	public void destroyFetchers() {
		for (int i = 0; i < imagefetchers.size(); i++) {
			imagefetchers.get(i).done();
			imagefetchers.remove(i);
			Viewer.printDebug("ImageFetcher-" + i + " destroyed");
		}
		for (int i = 0; i < threadfetchers.size(); i++) {
			threadfetchers.get(i).done();
			threadfetchers.remove(i);
			Viewer.printDebug("ThreadFetcher-" + i + " destroyed");
		}
		if (indexfetcher != null) {
			indexfetcher.done();
			indexfetcher = null;
		}
	}

	public void setFetchersPause(boolean pause) {
		for (int i = 0; i < imagefetchers.size(); i++) {
			imagefetchers.get(i).pause(pause);
			Viewer.printDebug("ImageFetcher-" + i + " pause = " + pause);
		}
		for (int i = 0; i < threadfetchers.size(); i++) {
			threadfetchers.get(i).pause(pause);
			Viewer.printDebug("ThreadFetcher-" + i + " pause = " + pause);
		}
		if (indexfetcher != null) {
			indexfetcher.pause(pause);
			Viewer.printDebug("IndexFetcher" + " pause = " + pause);
		}
	}

	public synchronized void addCompleteImage(File file) {
		Viewer.printDebug("Saving complete Image \n");
		final File temp = file;

		// Test if the file is a legitimate image and then add
		Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.toString(), options);

		if (options.outHeight != -1) {
			imagesToDownload -= 1;
			parent.runOnUiThread(new Runnable() {
				public void run() {
					parent.addCompleteImage(temp);
				}
			});
		}
	}

	public synchronized String getNextImageName() {
		if (imageUrlList.size() != 0) {
			String temp = imageUrlList.poll();
			Viewer.printDebug("Delivered next image-url " + temp);
			return temp;
		}
		return null;
	}

	public synchronized String getNextUrl() {
		if (linkUrlList.size() != 0) {

			String temp = linkUrlList.poll();
			Viewer.printDebug("Delivered next link-url " + temp);
			Viewer.printDebug("Link-urls left: " + linkUrlList.size());
			return temp;
		}
		return null;
	}

	public synchronized void addImageUrl(String url) {
		if (!imageUrlList.contains(url) && visitedUrls.get(url) == null) {
			System.out.println("Added image-url  -  " + url);
			visitedUrls.put(url, true);
			imageUrlList.add(url);
		} else {
			Viewer.printDebug("		Couldn't image-add url - " + url);
		}
	}

	public synchronized void addLinkUrl(String url) {
		if (!linkUrlList.contains(url) && visitedUrls.get(url) == null) {
			System.out.println("Added link-url  -  " + url);
			visitedUrls.put(url, true);
			linkUrlList.add(url);
		} else {
			Viewer.printDebug("		Couldn't add link-url - " + url);
		}
	}

	private void enableDialog() {
		parent.runOnUiThread(new Runnable() {
			public void run() {
				dialog.show();
			}
		});
	}

	private void destroyDialog() {
		parent.runOnUiThread(new Runnable() {
			public void run() {
				dialog.dismiss();
			}
		});
	}

	private void setDialogMessage(final String str) {
		parent.runOnUiThread(new Runnable() {
			public void run() {
				dialog.setMessage(str);
			}
		});
	}

	public void downloadXAdditionalImages(int number) {
		imagesToDownload += number;
	}

	public int getNumberOfImageToDownload() {
		return imagesToDownload;
	}

	public int getNbrImagesToDownload() {
		return imagesToDownload;
	}

	public int getNbrImageLinks() {
		return imageUrlList.size();
	}

	public int getNbrUrlLinks() {
		return linkUrlList.size();
	}

}
