package com.banda.bringme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import android.annotation.SuppressLint;
import android.app.Activity;

@SuppressLint("DefaultLocale")
public class MainActivity extends Activity {

	private WebServer			server;
	public RequestDataSource	requestDataSource;
	EditText					text;
	String						requestString;
	ListView					requestsList;
	
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
		}
		catch (IOException ioe) {
			Log.w("Httpd", ioe.getMessage());
			Log.w("Httpd", "The server could not start.");
		}
		Log.w("Httpd", "Web server initialized.");

		//text = (EditText) findViewById(R.id.requests);
		requestsList = (ListView) findViewById(R.id.requestsList);

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
					text.setText(requestString);
				}
			});
		}

		@Override
		public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parameters, Map<String, String> files) {

			String mimeType = MIME_HTML;

			if (uri.startsWith("/api")) {
				String[] uriPieces = uri.split("/");

				if (uriPieces.length > 1) {
					if (uriPieces[2].endsWith("request")) {
						if (parameters.containsKey("table")) {
							Request r = new Request();
							r.TABLE = parameters.get("table");
							requestDataSource.open();
							requestDataSource.addNewRequest(r);
							List<Request> requests = requestDataSource.getAllEntries();

							main.requestString = "";

							for (int i = 0; i < requests.size(); i++) {
								Request s = requests.get(i);
								main.requestString += "Request at table: " + s.TABLE + "\n";
							}
							refreshRequests();
							requestDataSource.close();

							return new NanoHTTPD.Response("API request handled. Table = " + r.TABLE);
						}
					}
				}
				return new NanoHTTPD.Response("Handle API request...");
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

			FileInputStream mbuffer = null;
			try {
				File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/www" + uri);
				mbuffer = new FileInputStream(file);
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
				return new NanoHTTPD.Response(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "Not found");
			}
			return new NanoHTTPD.Response(Response.Status.OK, mimeType, mbuffer);

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
