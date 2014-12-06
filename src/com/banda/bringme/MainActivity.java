package com.banda.bringme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	
	private WebServer server;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        server = new WebServer(this);
        try {
            server.start();
        } catch(IOException ioe) {
        	Log.w("Httpd", ioe.getMessage());
            Log.w("Httpd", "The server could not start.");
        }
        Log.w("Httpd", "Web server initialized.");
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

        @Override
        public Response serve(String uri, Method method, 
                              Map<String, String> header,
                              Map<String, String> parameters,
                              Map<String, String> files) {
        	
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
