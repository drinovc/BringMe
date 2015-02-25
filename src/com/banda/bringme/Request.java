package com.banda.bringme;

import java.io.Serializable;

public class Request implements Serializable{
	
	public static final int STATUS_NEW = 0;
	public static final int STATUS_ACKNOWLEDGED = 1;
	
	private static final long serialVersionUID = 1L;
	
	public int ID;
	public String table;
	public String type;
	public String comment;
	public String created;
	public long status;
	public String ipAddr;
	public int count;
	
	public Request() {
		this.status = STATUS_NEW;
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment != null ? comment : "";
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public long getStatus() {
		return status;
	}
	public void setStatus(long status) {
		this.status = status;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr != null ? ipAddr : "";
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}	
}