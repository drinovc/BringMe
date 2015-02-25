package com.banda.bringme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import android.app.Activity;

public class MainActivity extends Activity {
	
	private WebServer server;
	public RequestDataSource requestDataSource;
	EditText text;
	String requestString;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		requestDataSource = new RequestDataSource(this);
        server = new WebServer(this);
        try {
            server.start();
        } catch(IOException ioe) {
        	Log.w("Httpd", ioe.getMessage());
            Log.w("Httpd", "The server could not start.");
        }
        Log.w("Httpd", "Web server initialized.");
        
        text = (EditText)findViewById(R.id.requests);
    }

    // DON'T FORGET to stop the server
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (server != null)
            server.stop();
    }

    private class WebServer extends NanoHTTPD {

    	MainActivity main;
    	
        public WebServer(MainActivity main)
        {
            super(8080);
        	this.main = main;
        }

        void refreshRequests(){
			main.runOnUiThread(new Runnable() {
			    public void run() {
			    	text.setText(requestString);
			    }
			});
		}
        
        @Override
        public Response serve(String uri, Method method, 
                              Map<String, String> header,
                              Map<String, String> parameters,
                              Map<String, String> files) {
        	
        	if(method.name().equals("GET")){
        		if(parameters.containsKey("table")){
                	Request r = new Request();
                	r.TABLE = parameters.get("table");
                	requestDataSource.open();
                	requestDataSource.addNewRequest(r);
                	List<Request> requests = requestDataSource.getAllEntries();
                	
                	main.requestString = "";
                	
                    for(int i = 0; i < requests.size(); i++){
                    	Request s = requests.get(i);
                    	main.requestString += "Request at table: " + s.TABLE + "\n";
                    }
                	refreshRequests();
                    requestDataSource.close();
        		}
        	}
        	
            String answer = "";
            try {
            	InputStream inputStream = this.main.getResources().openRawResource(R.raw.index);
            	BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    answer += line;
                }
                reader.close();

            } catch(IOException ioe) {
                Log.w("Httpd", ioe.toString());
            }
            
            return new NanoHTTPD.Response(answer);
        }
    }
}
