package com.banda.bringme;

import java.io.Serializable;

import android.graphics.Color;

public class Table implements Serializable{
	
	public enum Shape {
	    RECTANGLE("Rectangle"),
	    CIRCLE("Circle");
	    private String friendlyName;
	    private Shape(String friendlyName){
	        this.friendlyName = friendlyName;
	    }
	    @Override
	    public String toString(){
	        return friendlyName;
	    }
	}
	
	public enum Type {
	    TABLE("Table"),
	    OBJECT("Object");
	    private String friendlyName;
	    private Type(String friendlyName){
	        this.friendlyName = friendlyName;
	    }
	    @Override
	    public String toString(){
	        return friendlyName;
	    }
	}
	
	public static final int STATUS_NEW = 0;
	public static final int STATUS_ACKNOWLEDGED = 1;	
	private static final long serialVersionUID = 1L;
	
	private long ID;
	private String tableName;
	private String description;
	private Type type;
	private int count;
	private Shape shape;
	private int xposition;
	private int yposition;
	private int asize;
	private int bsize;
	private int number;
	private int color;
	
	public Table() {
		xposition = 50;
		yposition = 50;
		asize = 10;
		bsize = 10;
		color = Color.BLUE;
		type = Type.TABLE;
		shape = Shape.CIRCLE;
		number = 1;
		tableName = "New table";
		description = "Add description";
		count = 1;
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Type getType() {
		return type;
	}
	
	public int getTypeInt() {
		return type.ordinal();
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public void setType(int type) {
		this.type = Type.values()[type];
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Shape getShape() {
		return shape;
	}

	public int getShapeInt() {
		return shape.ordinal();
	}
	
	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public void setShape(int shape) {
		this.shape = Shape.values()[shape];
	}
	
	public int getXposition() {
		return xposition;
	}

	public void setXposition(int xposition) {
		this.xposition = xposition;
	}

	public int getYposition() {
		return yposition;
	}

	public void setYposition(int yposition) {
		this.yposition = yposition;
	}

	public int getAsize() {
		return asize;
	}

	public void setAsize(int asize) {
		this.asize = asize;
	}

	public int getBsize() {
		return bsize;
	}

	public void setBsize(int bsize) {
		this.bsize = bsize;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getColor() {
		return color;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
}