package com.banda.bringme;

import java.util.Hashtable;

import DataSources.Table;
import DataSources.Table.Shape;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class DrawView extends View{

	Hashtable<Long,Table> objects;
	Hashtable<Long,Region> regions;
    
	Point size = new Point();
	Paint paint = new Paint();
	
	Context context;
	Tables tables;
	
	public DrawView(Context context, Tables tables) {
		super(context);
		WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE); 
	    Display display = window.getDefaultDisplay();
	    display.getSize(size);
	    this.context = context;
	    this.tables = tables;
		this.setOnTouchListener(drawViewTouch);
	}
	
	public void setObjects(Hashtable<Long,Table> objects){
		this.objects = objects;
		this.regions = new Hashtable<Long,Region>();
		for(long objectId : objects.keySet()){
			updateOrAddRegion(objects.get(objectId));
		}
		invalidate();
	}
	
    @Override
    public void onDraw(Canvas canvas) {
    	for(long objectId : objects.keySet()){
    		Table object = objects.get(objectId);
    		int x = object.getXposition() * size.x / 100;
    		int y = object.getYposition() * size.y / 100;
    		int a = object.getAsize() * size.x / 100;
    		int b = object.getBsize() * size.y / 100;
    		paint.setColor(object.getColor());
    		if(object.getShape() == Table.Shape.CIRCLE){
    			canvas.drawCircle(x,y,a,paint);
    		}
    		if(object.getShape() == Table.Shape.RECTANGLE){
    			canvas.drawRect(x, y, x+a, y+b, paint);
    		}
    	}
    }
	
    class CanvasTouch{
    	long objectId;
    	long time;
    	int deltaX,x;
    	int deltaY,y;
    	boolean dragged = false;
    	public CanvasTouch(long objectId, long time, int x, int y, int deltaX, int deltaY){
    		this.objectId = objectId;
    		this.time = time;
    		this.deltaX = deltaX;
    		this.deltaY = deltaY;
    		this.x = x;
    		this.y = y;
    	}
    }
    
    CanvasTouch lastTouch;
    
    OnTouchListener drawViewTouch = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			System.out.println(String.valueOf(event.getAction()));
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				boolean foundRegion = false;
				for(long objectId : regions.keySet()){
					Region region = regions.get(objectId);
					if(region.contains((int)event.getX(), (int)event.getY())){
						Table object = (Table)objects.get(objectId);
						CanvasTouch touch = new CanvasTouch(objectId,event.getEventTime(),(int)event.getX(),(int)event.getY(),
								(int)(event.getX() - object.getXposition() * size.x / 100), (int)(event.getY() - object.getYposition() * size.y / 100));
						foundRegion = true;
						if(lastTouch != null && lastTouch.time + 500 > touch.time){
							System.out.println("Touched object " + object.getTableName());
						}
						lastTouch = touch;
						break;
					}
				}
				if(!foundRegion){
					lastTouch = null;
					return false;
				}
			}
			if(event.getAction() == MotionEvent.ACTION_MOVE){
				if(lastTouch == null)
					return false;
				Table object = (Table) objects.get(lastTouch.objectId);
				Region region = (Region) regions.get(lastTouch.objectId);
				if(lastTouch.dragged || Math.sqrt(Math.pow(event.getX()-lastTouch.x,2)+Math.pow(event.getY()-lastTouch.y,2)) > 20){
					lastTouch.dragged = true;
					object.setXposition(((int)(event.getX() - lastTouch.deltaX))*100/size.x);
					object.setYposition(((int)(event.getY() - lastTouch.deltaY))*100/size.y);
					
					invalidate();
				}
			}
			if(event.getAction() == MotionEvent.ACTION_UP){
				if(lastTouch == null)
					return false;
				Table object = (Table) objects.get(lastTouch.objectId);
				updateOrAddRegion(object);
				tables.tableDataSource.open();
				tables.tableDataSource.updateTable(object);
				tables.tableDataSource.close();
				lastTouch = null;
			}
			return true;
		}
    };
    
    private void updateOrAddRegion(Table object){
    	Region region = regions.get(object.getID());
    	if(region == null)
    		region = new Region();
		int x=object.getXposition()*size.x / 100,
			y=object.getYposition()*size.y / 100;
		int a=object.getAsize()*size.x / 100,
			b=object.getBsize()*size.y / 100;
		if(object.getShape() == Shape.CIRCLE){
			Path path = new Path();
			path.addCircle(x, y, a, Path.Direction.CW);
			region.setPath(path,new Region(new Rect(x-a,y-a,x+a,y+a)));
		}else{
			region.set(new Rect(x,y,a,b));
		}
		regions.put(object.getID(), region);
    }
    
}
