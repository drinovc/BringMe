package com.banda.bringme;

import java.io.Serializable;

public class Request implements Serializable{
	private static final long serialVersionUID = 1L;
	public int ID;
	public String table;
	public String type;
}