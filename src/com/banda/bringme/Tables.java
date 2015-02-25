package com.banda.bringme;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.widget.AbsoluteLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Tables extends Activity {

	AbsoluteLayout tablesView;
	TextView miza1; 
	TextView miza2; 
	TextView miza3;
	TextView miza4; 
	TextView miza5;
	
	TextView mizaDrag;
	
	
	private static final String IMAGEVIEW_TAG = "The Android Logo";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tables);
		
		tablesView = (AbsoluteLayout )findViewById(R.id.view_tables);
		miza1 = (TextView )findViewById(R.id.miza1);
		miza2 = (TextView )findViewById(R.id.miza2);
		miza3 = (TextView )findViewById(R.id.miza3);
		miza4 = (TextView )findViewById(R.id.miza4);
		miza5 = (TextView )findViewById(R.id.miza5);
		
		miza1.setTag(IMAGEVIEW_TAG);
		miza2.setTag(IMAGEVIEW_TAG);
		miza3.setTag(IMAGEVIEW_TAG);
		miza4.setTag(IMAGEVIEW_TAG);
		miza5.setTag(IMAGEVIEW_TAG);
		    
	    // set the listener to the dragging data
		miza1.setOnLongClickListener(new MyClickListener());
		miza2.setOnLongClickListener(new MyClickListener());
		miza3.setOnLongClickListener(new MyClickListener());
		miza4.setOnLongClickListener(new MyClickListener());
		miza5.setOnLongClickListener(new MyClickListener());
		
	    tablesView.setOnDragListener(new MyDragListener());
		
	}

	private final class MyClickListener implements OnLongClickListener {

	    // called when the item is long-clicked
		@Override
		public boolean onLongClick(View view) {
		// TODO Auto-generated method stub
		
			// create it from the object's tag
			ClipData.Item item = new ClipData.Item((CharSequence)view.getTag());

	        String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
	        ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
	        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
	   
	        view.startDrag( data, //data to be dragged
	        				shadowBuilder, //drag shadow
	        				view, //local data about the drag and drop operation
	        				0   //no needed flags
	        			  );
	        
	        mizaDrag = (TextView) view;
	        
	        view.setVisibility(View.INVISIBLE);
	        return true;
		}	
	}

	class MyDragListener implements OnDragListener {
		@Override
		public boolean onDrag(View v, DragEvent event) {
	  
			// Handles each of the expected events
		    switch (event.getAction()) {
		    
		    //signal for the start of a drag and drop operation.
		    case DragEvent.ACTION_DRAG_STARTED:
		        // do nothing
		        break;
		        
		    //the drag point has entered the bounding box of the View
		    case DragEvent.ACTION_DRAG_ENTERED:
		        break;
		        
		    //the user has moved the drag shadow outside the bounding box of the View
		    case DragEvent.ACTION_DRAG_EXITED:
		        break;
		        
		    //drag shadow has been released,the drag point is within the bounding box of the View
		    case DragEvent.ACTION_DROP:		
		    	int newX = Math.round((event.getX() - mizaDrag.getWidth()/2)/30)*30;
		    	int newY = Math.round((event.getY() - mizaDrag.getHeight()/2)/30)*30;
		    	
		    	mizaDrag.setX(newX);
		    	mizaDrag.setY(newY);
		    	mizaDrag.setVisibility(View.VISIBLE);
		    	break;
		    	  
		    //the drag and drop operation has concluded.
		    case DragEvent.ACTION_DRAG_ENDED:
		       break;
		    
		    default:
		        break;
		    }
		    return true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tables, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
