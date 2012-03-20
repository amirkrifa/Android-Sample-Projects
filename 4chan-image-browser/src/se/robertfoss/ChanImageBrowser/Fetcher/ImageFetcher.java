package se.robertfoss.ChanImageBrowser.Fetcher;

import java.io.File;

import se.robertfoss.ChanImageBrowser.NetworkData;
import se.robertfoss.ChanImageBrowser.Viewer;

public class ImageFetcher extends Thread {

	private FetcherManager manager;
	private boolean isDone;
	private boolean isPaused;
	private final static int THREAD_SLEEP_TIME = 3000;


	ImageFetcher(FetcherManager manager) {
		this.manager = manager;
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
		String inputUrl;
		while (!isDone) {
			
			while (manager.getNbrImagesToDownload() > 0 && 
					(inputUrl = manager.getNextImageName()) != null 
					&& !isPaused
					&& !isDone) {
				System.out.println("Images left to download: " + manager.getNbrImagesToDownload());
				Viewer.printDebug(super.getName() + " is fetching picture  -  " + inputUrl);
				String[] fileName = inputUrl.split("/");

				File pic = null;
				try {
					pic = NetworkData.getFileFromUrl(inputUrl,
							fileName[fileName.length - 1]);
				} catch (Exception e) {
					Viewer.printDebug(" 	Unable to fetch image - " + inputUrl);
					e.printStackTrace();
				}

				Viewer
						.printDebug(super.getName() + " is adding downloaded a picture");
				if (pic != null) {
					manager.addCompleteImage(pic);
				} else {
					Viewer.printDebug(" 	" + inputUrl
							+ " could'nt be parsed into a Bitmap");
				}
				inputUrl = manager.getNextImageName();
				
				//Let UI work for a bit.
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			// Wait for new data or to be unpaused
			try {
				Thread.sleep(THREAD_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
