package com.banda.bringme;

import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapters.RequestsAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

@SuppressLint("DefaultLocale")
public class MainActivity extends Activity {

	private WebServer			server;
	public RequestDataSource	requestDataSource;
	EditText					text;
	String						requestString;
	ListView					requestsList;
	RequestsAdapter				requestsAdapter;
	List<Request>				requests;
		
	public static final String	MIME_JAVASCRIPT	= "text/javascript";
	public static final String	MIME_CSS		= "text/css";
	public static final String	MIME_JPEG		= "image/jpeg";
	public static final String	MIME_PNG		= "image/png";
	public static final String	MIME_SVG		= "image/svg+xml";
	public static final String	MIME_JSON		= "application/json";
	public static final String	MIME_GIF		= "image/gif";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		requestDataSource = new RequestDataSource(this);
		server = new WebServer(this);
		try {
			server.start();
			String ipAddress = Helper.checkWifiConnection(this);
			Log.w("Httpd", "IP: " + ipAddress);
		}
		catch (Exception ioe) {
			Log.w("Httpd", ioe.getMessage());
			Log.w("Httpd", "The server could not start.");
		}
		Log.w("Httpd", "Web server initialized.");
		//text = (EditText) findViewById(R.id.requests);
		
		requests = new ArrayList<Request>();
		
		requestDataSource.open();
		requests = requestDataSource.getAllEntries();
		requestDataSource.close();		
		
		requestsAdapter = new RequestsAdapter(MainActivity.this, requests);
		requestsList = (ListView) findViewById(R.id.requestsList);
		requestsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		requestsList.setAdapter(requestsAdapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_tables) {
			Intent intent = new Intent(this, Tables.class);
			startActivity(intent);
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// DON'T FORGET to stop the server
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (server != null)
			server.stop();
	}

	private class WebServer extends NanoHTTPD {

		MainActivity	main;

		public WebServer(MainActivity main) {
			super(8080);
			this.main = main;
		}

		void refreshRequests() {
			main.runOnUiThread(new Runnable() {
				public void run() {
					requestsAdapter.updateRequests(requests);
					requestsAdapter.notifyDataSetChanged();
					//text.setText(requestString);
				}
			});
		}

		@Override
		public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parameters, Map<String, String> files) {
			
			String mimeType = MIME_HTML;

			Log.w("Httpd", "Request received");
			
			if (uri.startsWith("/api")) {
				String[] uriPieces = uri.split("/");

				if (uriPieces.length > 1) {
					if (uriPieces[2].equals("request")) {
						if (parameters.containsKey("table")) {
							
							Request r = new Request();
							r.setTable(parameters.get("table"));
							r.setType(parameters.get("type")); 
							r.setComment(parameters.get("comment"));
							r.setIpAddr(header.get("remote-addr"));
							
							requestDataSource.open();
							requestDataSource.addNewRequest(r);
							requests = requestDataSource.getAllEntries();
							requestDataSource.close();
							
							refreshRequests();

							return new NanoHTTPD.Response("Zahteva poslana. Miza " + r.table + "." + " IP " + r.ipAddr);
						}
					}
				}
				return new NanoHTTPD.Response("Ni zaznane metode za obdevalo zahteve!");
			}
			else if (uri.equals("/")) {
				uri = "/index.html";
			}
			else if (uri.toLowerCase().endsWith(".html") || uri.toLowerCase().endsWith(".htm")) {
				mimeType = MIME_HTML;
			}
			else if (uri.toLowerCase().endsWith(".js")) {
				mimeType = MIME_JAVASCRIPT;
			}
			else if (uri.toLowerCase().endsWith(".css")) {
				mimeType = MIME_CSS;
			}
			else if (uri.toLowerCase().endsWith(".json")) {
				mimeType = MIME_JSON;
			}
			else if (uri.toLowerCase().endsWith(".png")) {
				mimeType = MIME_PNG;
			}
			else if (uri.toLowerCase().endsWith(".jpeg") || uri.toLowerCase().endsWith(".jpg")) {
				mimeType = MIME_JPEG;
			}
			else if (uri.toLowerCase().endsWith(".svg")) {
				mimeType = MIME_SVG;
			}
			else if (uri.toLowerCase().endsWith(".gif")) {
				mimeType = MIME_GIF;
			}
			else {
				FileNameMap fileNameMap = URLConnection.getFileNameMap();
				mimeType = fileNameMap.getContentTypeFor(uri);
			}

			InputStream is = null;
			try {
				is = getAssets().open("www" + uri);
				if(is == null) {
					return new NanoHTTPD.Response(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "Not found");
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				return new NanoHTTPD.Response(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Internal error");
			}
			return new NanoHTTPD.Response(Response.Status.OK, mimeType, is);
			
			/*
			 * FileInputStream mbuffer = null;
			 * try {
			 * 	File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/www" + uri);
			 * 	mbuffer = new FileInputStream(file);
			 * }
			 * catch (FileNotFoundException e) {
			 * 	e.printStackTrace();
			 * 	return new NanoHTTPD.Response(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "Not found");
			 * }
			 * return new NanoHTTPD.Response(Response.Status.OK, mimeType, mbuffer);
			 */			

			/*
			 * try { // Opening file from SD Card File root =
			 * Environment.getExternalStorageDirectory(); FileReader index = new
			 * FileReader(root.getAbsolutePath() + "/www" + uri); BufferedReader
			 * reader = new BufferedReader(index); String line = ""; while
			 * ((line = reader.readLine()) != null) { answer += line; }
			 * reader.close(); } catch(IOException ioe) { Log.w("Httpd",
			 * ioe.toString()); }
			 */

			/*
			 * String answer = ""; try { InputStream inputStream =
			 * this.main.getResources().openRawResource(R.raw.index);
			 * BufferedReader reader = new BufferedReader(new
			 * InputStreamReader(inputStream, "UTF-8")); String line = ""; while
			 * ((line = reader.readLine()) != null) { answer += line; }
			 * reader.close();
			 * 
			 * } catch(IOException ioe) { Log.w("Httpd", ioe.toString()); }
			 * 
			 * return new NanoHTTPD.Response(answer);
			 */
		}
	}
}
