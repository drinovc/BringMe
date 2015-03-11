package com.banda.bringme;

import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.Map;

import DataSources.Request;
import DataSources.RequestDataSource;
import DataSources.Table;
import DataSources.TableDataSource;
import android.widget.EditText;
import android.widget.TextView;

import adapters.TablesAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

@SuppressLint("DefaultLocale")
public class MainActivity extends Activity {

	private WebServer			server;
	public RequestDataSource	requestDataSource;
	public TableDataSource		tableDataSource;
	ListView					tablesList;
	TablesAdapter				tablesAdapter;
	Hashtable<Long,Table>		tables = new Hashtable<Long,Table>();;
		
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
		tableDataSource = new TableDataSource(this);
		
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
		
		tableDataSource.open();
		for(Table table : tableDataSource.getAllEntries()){
			tables.put(table.getID(),table);
		}
		tableDataSource.close();		
		
		tablesAdapter = new TablesAdapter(MainActivity.this, tables);
		tablesList = (ListView) findViewById(R.id.tablesList);
		tablesList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		tablesList.setAdapter(tablesAdapter);
	}
	
	public void showTableDetails(View view){
		long lastClick;
		Object tag = view.getTag(R.string.last_click);
		if(tag != null)
			lastClick = (long) tag;
		else
			lastClick = 0;
		long currentClick = System.currentTimeMillis();
		view.setTag(R.string.last_click,currentClick);
		if(currentClick - lastClick < 400){
			View parent = (View)view.getParent();
			long id = (long)parent.getTag(R.string.table_id);
			Table table = tables.get(id);
			View details = getLayoutInflater().inflate(R.layout.table_details, null, false);
			((TextView)details.findViewById(R.id.tableID)).setText("Table ID: " + String.valueOf(table.getID()));
			((TextView)details.findViewById(R.id.tableNumber2)).setText("Table ID: " + String.valueOf(table.getNumber()));
			((EditText)details.findViewById(R.id.tableName2)).setText("Table ID: " + table.getTableName());
			((EditText)details.findViewById(R.id.tableDescription2)).setText("Table ID: " + table.getDescription());
			details.setVisibility(View.VISIBLE);
			details.setTag(R.string.table_id,id);
		}
	}
	
	public void removeAlerts(View view){
		long lastClick;
		Object tag = view.getTag(R.string.last_click);
		if(tag != null)
			lastClick = (long) tag;
		else
			lastClick = 0;
		long currentClick = System.currentTimeMillis();
		view.setTag(R.string.last_click,currentClick);
		if(currentClick - lastClick < 400){
			View parent = (View)view.getParent();
			long id = (long)parent.getTag(R.string.table_id);
			requestDataSource.open();
			requestDataSource.closeEntriesByTableID(id);
			requestDataSource.close();
			tables.get(id).setRequests(0);
			tablesAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_tables) {
			Intent intent = new Intent(this, Tables.class);
			startActivity(intent);			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

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

		void refreshTables() {
			main.runOnUiThread(new Runnable() {
				public void run() {
					
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
							r.setTableID(Long.parseLong(parameters.get("table")));
							r.setType(Request.Type.valueOf(parameters.get("type"))); 
							r.setComment(parameters.get("comment"));
							r.setIpAddr(header.get("remote-addr"));
							
							requestDataSource.open();
							requestDataSource.addNewRequest(r);
							requestDataSource.close();
							
							refreshTables();

							return new NanoHTTPD.Response("Zahteva poslana. Miza " + String.valueOf(r.getTableID()) + "." + " IP " + r.getIpAddr());
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
