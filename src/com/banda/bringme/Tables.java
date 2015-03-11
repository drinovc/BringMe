package com.banda.bringme;

import java.util.Hashtable;

import com.banda.bringme.ColorPickerDialog.OnColorChangedListener;

import DataSources.Table;
import DataSources.TableDataSource;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;

public class Tables extends Activity implements OnColorChangedListener{

	LinearLayout tablesToolbar;
	RelativeLayout mainLayout;
	DrawView drawView;
	public TableDataSource tableDataSource;
	Hashtable<Long,Table> objects;
	View details;
	boolean settingEvents = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tables);
		details = createEditorSettings(this);
		objects = new Hashtable<Long,Table>();
		tableDataSource = new TableDataSource(this);
		tableDataSource.open();
		for(Table table : tableDataSource.getAllEntries()){
			objects.put(table.getID(),table);
		}
		tableDataSource.close();
		
		mainLayout = (RelativeLayout)findViewById(R.id.view_tables);
		tablesToolbar = (LinearLayout)findViewById(R.id.tablesToolbar);
		drawView = new DrawView((Context)this,this);
		mainLayout.addView(drawView);
		drawView.setObjects(objects);
		
	    //tablesView.setOnDragListener(new MyDragListener());
		
	}

	/*private final class MyClickListener implements OnLongClickListener {

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
	        
	        //mizaDrag = (TextView) view;
	        
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
	}*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.tables, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_editor:
	        	setEditorVisibility((tablesToolbar.getVisibility()==View.GONE)?View.VISIBLE:View.GONE);
	        	item.setTitle((tablesToolbar.getVisibility()==View.GONE)?R.string.editor:R.string.editor_disable);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void setEditorVisibility(int visibility){
		tablesToolbar.setVisibility(visibility);
	}
	
	public void addObject(View view){
		Table table = new Table();
		tableDataSource.open();
		table.setID(tableDataSource.insertTable(table));
		tableDataSource.close();
		objects.put(table.getID(),table);
		drawView.setObjects(objects);
		setEditorSettings(details,table,this,this);
		details.setVisibility(View.VISIBLE);
	}

	@Override
	public void colorChanged(String key, int color) {
		long ID = Long.parseLong(key);
		Table object = objects.get(ID);
		object.setColor(color);
		tableDataSource.open();
		tableDataSource.updateTable(object);
		tableDataSource.close();
	}
	
	public ColorPickerDialog color;
	
	public View createEditorSettings(final Context context){
		View view = getLayoutInflater().inflate(R.layout.object_details, mainLayout);

		final Spinner spinnerType = (Spinner) view.findViewById(R.id.spinnerType);
		final Spinner spinnerShape = (Spinner) view.findViewById(R.id.spinnerShape);
		final EditText tableNumber = (EditText) view.findViewById(R.id.tableNumber2);
		final EditText tableName = (EditText) view.findViewById(R.id.tableName);
		final EditText tableDescription = (EditText) view.findViewById(R.id.tableDescription);
		final SeekBar sizeA = (SeekBar) view.findViewById(R.id.sizeA);
		final SeekBar sizeB = (SeekBar) view.findViewById(R.id.sizeB);
		final EditText tableCount = (EditText) view.findViewById(R.id.tableCount);
		final Button pickColor = (Button) view.findViewById(R.id.colorPicker);
		
		spinnerType.setAdapter(new ArrayAdapter<Table.Type>(this, android.R.layout.simple_spinner_item, Table.Type.values()));
		spinnerShape.setAdapter(new ArrayAdapter<Table.Shape>(this, android.R.layout.simple_spinner_item, Table.Shape.values()));
 
		spinnerType.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if(settingEvents)
					return;
				long ID = (Long)details.getTag(R.string.table_id);
				Table object = objects.get(ID);
				object.setType(arg2);
				tableDataSource.open();
				tableDataSource.updateTable(object);
				tableDataSource.close();
				if(arg2 == Table.Type.OBJECT.ordinal()){
					tableNumber.setVisibility(View.GONE);
					tableCount.setVisibility(View.GONE);
				}else{
					tableNumber.setVisibility(View.VISIBLE);
					tableCount.setVisibility(View.VISIBLE);							
				}
			}
			@Override public void onNothingSelected(AdapterView<?> arg0) {}
		});
		spinnerShape.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if(settingEvents)
					return;
				long ID = (Long)details.getTag(R.string.table_id);
				Table object = objects.get(ID);
				object.setShape(arg2);
				tableDataSource.open();
				tableDataSource.updateTable(object);
				tableDataSource.close();
				if(arg2 == Table.Shape.CIRCLE.ordinal()){
					sizeB.setVisibility(View.GONE);
				}else{
					sizeB.setVisibility(View.VISIBLE);					
				}
			}
			@Override public void onNothingSelected(AdapterView<?> arg0) {}
		});		
		sizeA.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				if(settingEvents)
					return;
				long ID = (Long)details.getTag(R.string.table_id);
				Table object = objects.get(ID);
				object.setAsize(arg1);
				tableDataSource.open();
				tableDataSource.updateTable(object);
				tableDataSource.close();
			}
			@Override public void onStartTrackingTouch(SeekBar arg0) {}
			@Override public void onStopTrackingTouch(SeekBar arg0) {}
		});
		sizeB.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				if(settingEvents)
					return;
				long ID = (Long)details.getTag(R.string.table_id);
				Table object = objects.get(ID);
				object.setBsize(arg1);
				tableDataSource.open();
				tableDataSource.updateTable(object);
				tableDataSource.close();
			}
			@Override public void onStartTrackingTouch(SeekBar arg0) {}
			@Override public void onStopTrackingTouch(SeekBar arg0) {}
		});
		tableNumber.addTextChangedListener(new TextWatcher(){
	        public void afterTextChanged(Editable s) {
				if(settingEvents)
					return;
	        	long ID = (Long)details.getTag(R.string.table_id);
				Table object = objects.get(ID);
				object.setNumber(Integer.parseInt(tableNumber.getText().toString()));
				tableDataSource.open();
				tableDataSource.updateTable(object);
				tableDataSource.close();
	        }
			@Override public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			@Override public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
	    });
		tableName.addTextChangedListener(new TextWatcher(){
	        public void afterTextChanged(Editable s) {
				if(settingEvents)
					return;
	        	long ID = (Long)details.getTag(R.string.table_id);
				Table object = objects.get(ID);
				object.setTableName(tableName.getText().toString());
				tableDataSource.open();
				tableDataSource.updateTable(object);
				tableDataSource.close();
	        }
			@Override public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			@Override public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
	    });
		tableDescription.addTextChangedListener(new TextWatcher(){
	        public void afterTextChanged(Editable s) {
				if(settingEvents)
					return;
	        	long ID = (Long)details.getTag(R.string.table_id);
				Table object = objects.get(ID);
				object.setDescription(tableDescription.getText().toString());
				tableDataSource.open();
				tableDataSource.updateTable(object);
				tableDataSource.close();
	        }
			@Override public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			@Override public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
	    });
		tableCount.addTextChangedListener(new TextWatcher(){
	        public void afterTextChanged(Editable s) {
				if(settingEvents)
					return;
	        	long ID = (Long)details.getTag(R.string.table_id);
				Table object = objects.get(ID);
				object.setCount(Integer.parseInt(tableCount.getText().toString()));
				tableDataSource.open();
				tableDataSource.updateTable(object);
				tableDataSource.close();
	        }
			@Override public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			@Override public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
	    });
		pickColor.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(settingEvents)
					return;
				long ID = (Long)details.getTag(R.string.table_id);
				Table object = objects.get(ID);
				color = new ColorPickerDialog(context,
					new OnColorChangedListener(){
						@Override
						public void colorChanged(String key, int color) {
							long ID = (Long)details.getTag(R.string.table_id);
							Table object = objects.get(ID);
							object.setColor(color);
							tableDataSource.open();
							tableDataSource.updateTable(object);
							tableDataSource.close();
						}			
				}, "picker", object.getColor(), Color.BLUE);
				color.show();
			}
		});
		return view;
	}

	public void setEditorSettings(View view, Table table, final Context context, final OnColorChangedListener listener){
		final long ID = table.getID();
		view.setTag(R.string.table_id, ID);
		
		Spinner spinnerType = (Spinner) view.findViewById(R.id.spinnerType);
		Spinner spinnerShape = (Spinner) view.findViewById(R.id.spinnerShape);
		EditText tableNumber = (EditText) view.findViewById(R.id.tableNumber2);
		EditText tableName = (EditText) view.findViewById(R.id.tableName);
		EditText tableDescription = (EditText) view.findViewById(R.id.tableDescription);
		SeekBar sizeA = (SeekBar) view.findViewById(R.id.sizeA);
		SeekBar sizeB = (SeekBar) view.findViewById(R.id.sizeB);
		EditText tableCount = (EditText) view.findViewById(R.id.tableCount);
		
		settingEvents=true;
		spinnerType.setSelection(table.getTypeInt());
		spinnerShape.setSelection(table.getShapeInt());
		tableNumber.setText(String.valueOf(table.getNumber()));
		tableName.setText(table.getTableName());
		tableDescription.setText(table.getDescription());
		sizeA.setProgress(table.getAsize());
		sizeB.setProgress(table.getBsize());
		tableCount.setText(String.valueOf(table.getCount()));
		settingEvents=false;
	}
	
}
