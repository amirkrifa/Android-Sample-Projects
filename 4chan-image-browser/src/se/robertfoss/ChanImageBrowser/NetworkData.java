package se.robertfoss.ChanImageBrowser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import android.os.Environment;


public abstract class NetworkData {
	/** Called when the activity is first created. */

	public static final File tempDir = new File(Environment
			.getExternalStorageDirectory(), "/4Chan/temp/");
	public static final File baseDir = new File(Environment
			.getExternalStorageDirectory(), "/4Chan/");

	

	public static File getFileFromUrl(String sUrl, String outputName)
			throws Exception {
		InputStream in = getHTTPConnection(sUrl);
		tempDir.mkdirs();
		File file = new File(tempDir, outputName);

		System.out.println("Saving image to: " + file.toString());
		FileOutputStream out = new FileOutputStream(file);

		byte[] buf = new byte[4 * 1024]; // 4K buffer
		int bytesRead;
		while ((bytesRead = in.read(buf)) != -1) {
			out.write(buf, 0, bytesRead);
		}
		in.close();
		return new File(outputName);
	}

	public static String getUrlContent(String sUrl){
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				getHTTPConnection(sUrl)));
		String content = "", line;
		try {
			while ((line = rd.readLine()) != null) {
				content += line + "\n";
			}
		} catch (IOException e) {
			Viewer.printDebug(" 	Couldnt read from: " + sUrl);
			e.printStackTrace();
		}
		return content;
	}

	private static InputStream getHTTPConnection(String sUrl) {

		URL url = null;
		InputStream is = null;
		try {
			url = new URL(sUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(60000);
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			connection.connect();
			String encoding = connection.getContentEncoding();
			
			//create the appropriate stream wrapper based on
			//the encoding type
			if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
				Viewer.printDebug("Opening compressed HTTP connection, gzip");
				is = new GZIPInputStream(connection.getInputStream());
			}
			else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
				Viewer.printDebug("Opening compressed HTTP connection, deflate");
				is = new InflaterInputStream(connection.getInputStream(), new Inflater(true));
			}
			else {
				Viewer.printDebug("Opening plain HTTP connection");
				is = connection.getInputStream();
			}
		} catch (Exception e) {
			Viewer.printDebug(" 	Couldnt connect to: " + sUrl);
		}
		return is;
	}
}