package com.TwitterJSONXML;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TwitterJSONXMLMain extends Activity {
	Button btnXML;
	Button btnJSON;
	TextView tvData;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


		tvData = (TextView) findViewById(R.id.txtData);
		btnXML = (Button) findViewById(R.id.btnXML);
		btnXML.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{ 
				examineXMLFile();
			}

		});


		btnJSON = (Button) findViewById(R.id.btnJSON);
		btnJSON.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				//examineJSONFile();
			}
		});

	}

	private void examineXMLFile()
	{
		try {
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet("http://twitter.com/statuses/user_timeline/amirkrifa.xml");
			HttpResponse response = httpClient.execute(httpGet, localContext);

			
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
			    Log.d("TwitterJSONXML", "HTTP error, invalid server status code: " + response.getStatusLine());  
			}

			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = documentFactory.newDocumentBuilder();
			
			//InputSource is = new InputSource(getResources().openRawResource(R.raw.amirkrifaxml));
			InputSource is = new InputSource(response.getEntity().getContent());
			// create the factory
			SAXParserFactory factory = SAXParserFactory.newInstance();
			// create a parser
			SAXParser parser = factory.newSAXParser();
			// create the reader (scanner)
			XMLReader xmlreader = parser.getXMLReader();
			// instantiate our handler
			TwitterFeedHandler tfh = new TwitterFeedHandler();

			// assign our handler
			xmlreader.setContentHandler(tfh);
			// perform the synchronous parse
			xmlreader.parse(is);
			// should be done... let's display our results
			tvData.setText(tfh.getResults());
		}
		catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
			Log.e("Error", "ClientProtocolException");
        } catch (IOException e) {
            // TODO Auto-generated catch block
			Log.e("Error", "IOException");
        }	
        catch (SAXException e) {
            // TODO Auto-generated catch block
			Log.e("Error", "SAXException");
        }
        catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
			Log.e("Error", "ParserConfigurationException");
        }
	}
}