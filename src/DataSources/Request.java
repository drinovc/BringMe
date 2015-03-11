package DataSources;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Request implements Serializable{
	private static final long serialVersionUID = 1L;
	private String dateFormat = "yyyy.MM.dd hh:mm";
	private SimpleDateFormat ft;
	private long ID;
	private long tableID;
	private Type type;
	private Status status;
	private String comment;
	private String ipAddr;
	private Date created;
	
	public enum Type{
		CHEQUE("Cheque"),
		WAITER("Waiter");
	    private String friendlyName;
	    private Type(String friendlyName){
	        this.friendlyName = friendlyName;
	    }
	    @Override
	    public String toString(){
	        return friendlyName;
	    }
	}

	public enum Status{
		OPEN("Open"),
		CLOSED("Closed");
	    private String friendlyName;
	    private Status(String friendlyName){
	        this.friendlyName = friendlyName;
	    }
	    @Override
	    public String toString(){
	        return friendlyName;
	    }
	}
	
	public Request() {
		this.status = Status.OPEN;
		ft = new SimpleDateFormat (dateFormat);
	}
	
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getTableID() {
		return tableID;
	}
	public void setTableID(long tableID) {
		this.tableID = tableID;
	}
	public Type getType() {
		return type;
	}
	public int getTypeInt() {
		return type.ordinal();
	}
	public void setType(int type) {
		this.type = Type.values()[type];
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment != null ? comment : "";
	}
	public Date getCreated() {
		return created;
	}
	public String getCreatedString() {
		return ft.format(created);
	}
	public void setCreated(String created) {
		try {
			this.created = ft.parse(created);
		} catch (ParseException e) {}
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Status getStatus() {
		return status;
	}
	public int getStatusInt() {
		return status.ordinal();
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public void setStatus(int status) {
		this.status = Status.values()[status];
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr != null ? ipAddr : "";
	}
}