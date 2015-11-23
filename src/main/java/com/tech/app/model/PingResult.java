package com.tech.app.model;

import java.util.Date;

public class PingResult {
	String pingType;
	String result;
	Date date;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public PingResult(String pingType, String result, Date date) {
		
		this.result = result;
		this.date = date;
		this.pingType = pingType;
	}
	
	public String getPingType() {
		return pingType;
	}
	public void setPingType(String pingType) {
		this.pingType = pingType;
	}
	
	
}
