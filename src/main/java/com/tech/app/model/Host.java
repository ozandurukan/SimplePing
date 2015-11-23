package com.tech.app.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Host {
	String hostName;
	ConcurrentMap<String, PingResult> pingResultMap;
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public ConcurrentMap<String, PingResult> getPingResultMap() {
		return pingResultMap;
	}
	public void setPingResultMap(ConcurrentMap<String, PingResult> pingResultMap) {
		this.pingResultMap = pingResultMap;
	}
	
	public Host(String hostName) {
		this.hostName = hostName;
		this.pingResultMap = new ConcurrentHashMap<String, PingResult>();
	}
	
}
