package se.robertfoss.ChanImageBrowser.Fetcher;

import java.util.ArrayList;

import se.robertfoss.ChanImageBrowser.NetworkData;
import se.robertfoss.ChanImageBrowser.Viewer;

public class IndexFetcher extends Thread {

	private FetcherManager manager;
	private boolean isDone;
	private boolean isPaused;
	private final static int THREAD_SLEEP_TIME = 3000;
	private final static int LINK_DOWNLOAD_THRESHOLD = 2;
	private final static int NUMBER_OF_RETRIES = 3;

	private String seedUrl;
	private String pageRegex;
	private int pageRegexTruncateIndex;
	private String imageRegex;
	private int imageRegexTruncateIndex;
	private String prependToPageUrl;
	private String prependToImageUrl;

	IndexFetcher(FetcherManager manager, String seedUrl, String pageRegex, int pageRegexTruncateIndex,
			String prependToPageUrl, String imageRegex, int imageRegexTruncateIndex, String prependToImageUrl) {
		this.manager = manager;
		this.seedUrl = seedUrl;
		this.imageRegex = imageRegex;
		this.imageRegexTruncateIndex = imageRegexTruncateIndex;
		this.pageRegex = pageRegex;
		this.pageRegexTruncateIndex = pageRegexTruncateIndex;
		this.prependToImageUrl = prependToImageUrl;
		this.prependToPageUrl = prependToPageUrl;
		isDone = false;
		isPaused = false;
	}

	public void run() {

		while (!isDone) {
			while (manager.getNbrUrlLinks() < LINK_DOWNLOAD_THRESHOLD
					&& !isPaused && !isDone) {
				runOneIteration();
			}

			// Wait for new data or to be unpaused
			try {
				Thread.sleep(THREAD_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void runOneIteration() {
		String inputHtml = null;
		int retries = NUMBER_OF_RETRIES;
		do {
			retries--;
			inputHtml = NetworkData.getUrlContent(seedUrl);
		} while (inputHtml == null && retries > 0);

		// Parse for images and add to managers list
		Viewer.printDebug("Fetching images from " + seedUrl);
		ArrayList<String> tempList = Parser.parseForStrings(inputHtml,
				imageRegex, imageRegexTruncateIndex);
		for (String str : tempList) {
			manager.addImageUrl(prependToImageUrl + str);
		}

		yield();

		// Parse for links and add to managers list
		Viewer.printDebug("Fetching links from " + seedUrl);
		tempList = Parser.parseForStrings(inputHtml, pageRegex, pageRegexTruncateIndex);
		for (String str : tempList) {
			manager.addLinkUrl(prependToPageUrl + str);
		}
	}

	public void done() {
		isDone = true;
	}
	
	public void pause(Boolean isPaused) {
		this.isPaused = isPaused;
	}

	public boolean isPaused() {
		return isPaused;
	}
}
