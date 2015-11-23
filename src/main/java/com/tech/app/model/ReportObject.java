package com.tech.app.model;

public class ReportObject {
	String hostName;
	String icmpPingResult;
	String tcpPingResult;
	String tracertResult;
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getIcmpPingResult() {
		return icmpPingResult;
	}
	public void setIcmpPingResult(String icmpPingResult) {
		this.icmpPingResult = icmpPingResult;
	}
	public String getTcpPingResult() {
		return tcpPingResult;
	}
	public void setTcpPingResult(String tcpPingResult) {
		this.tcpPingResult = tcpPingResult;
	}
	public String getTracertResult() {
		return tracertResult;
	}
	public void setTracertResult(String tracertResult) {
		this.tracertResult = tracertResult;
	}
	@Override
	public String toString() {
		return "ReportObject [hostName=" + hostName + ", icmpPingResult="
				+ icmpPingResult + ", tcpPingResult=" + tcpPingResult
				+ ", tracertResult=" + tracertResult + "]";
	}
	
	
	
}


