package se.robertfoss.ChanImageBrowser.Fetcher;

import java.util.ArrayList;

import se.robertfoss.ChanImageBrowser.NetworkData;
import se.robertfoss.ChanImageBrowser.Viewer;

public class ThreadFetcher extends Thread {
	private FetcherManager manager;
	private boolean isDone;
	private boolean isPaused;
	private String imageRegex;
	private int imageRegexTruncateIndex;
	private String prependToImageUrl;
	private final static int THREAD_SLEEP_TIME = 5000;
	private final static int IMAGE_DOWNLOAD_THRESHOLD = 10;
	private final static int NUMBER_OF_RETRIES = 1;

	ThreadFetcher(FetcherManager manager, String imageRegex, int imageRegexTruncateIndex,
			String prependToImageUrl) {
		this.manager = manager;
		this.imageRegex = imageRegex;
		this.imageRegexTruncateIndex = imageRegexTruncateIndex;
		this.prependToImageUrl = prependToImageUrl;
		isDone = false;
		isPaused = false;
	}

	public void done() {
		isDone = true;
	}
	
	public void pause(Boolean isPaused){
		this.isPaused = isPaused;
	}
	
	public boolean isPaused(){
		return isPaused;
	}

	public void run() {
		isDone = false;

		while (!isDone) {
			String url;
			while (manager.getNbrImageLinks() < IMAGE_DOWNLOAD_THRESHOLD
					&& (url = manager.getNextUrl()) != null
					&& !isPaused
					&& !isDone) {

				String inputHtml = null;
				int retries = NUMBER_OF_RETRIES;
				do {
					retries--;
					Viewer.printDebug(super.getName() +" is fetching images from " + url);
					try {
						inputHtml = NetworkData.getUrlContent(url);
					} catch (Exception e) {
						Viewer.printDebug(" 	Unable to fetch thread - " + url);
					}
				} while (inputHtml == null && retries > 0);

				// Parse for images and add to managers list
				if (inputHtml != null) {
					ArrayList<String> tempList = Parser.parseForStrings(
							inputHtml, imageRegex, imageRegexTruncateIndex);
					for (String str : tempList) {
						manager.addImageUrl(prependToImageUrl + str);
					}
				}

				url = manager.getNextUrl();
			}
			
			// Wait for new data or ot be unpaused
			try {
				Thread.sleep(THREAD_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
