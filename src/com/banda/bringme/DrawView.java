package com.banda.bringme;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class DrawView extends View{

	Hashtable<Long,Table> objects;
	Point size = new Point();
	Paint paint = new Paint();
	
	public DrawView(Context context) {
		super(context);
		WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE); 
	    Display display = window.getDefaultDisplay();
	    display.getSize(size);
	}
	
	public void setObjects(Hashtable<Long,Table> objects){
		this.objects = objects;
		invalidate();
	}
	
    @Override
    public void onDraw(Canvas canvas) {
    	for(long objectId : objects.keySet()){
    		Table object = objects.get(objectId);
    		int x = object.getXposition() * size.x;
    		int y = object.getYposition() * size.y;
    		int a = object.getAsize() * size.x;
    		int b = object.getBsize() * size.y;
    		paint.setColor(object.getColor());
    		if(object.getShape() == Table.Shape.CIRCLE){
    			canvas.drawCircle(x,y,a,paint);
    		}
    		if(object.getShape() == Table.Shape.RECTANGLE){
    			canvas.drawRect(x, y, x+a, y+b, paint);
    		}
    	}
    }
	
}
