package com.tydic.pntstar.util.redis;

public class DCA {
	private String dcaIp;
	private int dcaPort;
	private String dcaUUID;
	private String dcaAcctid;
	private String dcaUsername;
	private String dcaPasswd;
	private String dcaProcessname;
	private int dcaPid;
	private String dcaFlag;
	private int reconnectCount;
	private int reconnectInterval;
	private int timeout;
	
	private int maxFreeConnections;
	private int maxActiveConnections;
	
	public int getMaxFreeConnections() {
		return maxFreeConnections;
	}
	public void setMaxFreeConnections(int maxFreeConnections) {
		this.maxFreeConnections = maxFreeConnections;
	}
	public int getMaxActiveConnections() {
		return maxActiveConnections;
	}
	public void setMaxActiveConnections(int maxActiveConnections) {
		this.maxActiveConnections = maxActiveConnections;
	}
	public String getDcaIp() {
		return dcaIp;
	}
	public void setDcaIp(String dcaIp) {
		this.dcaIp = dcaIp;
	}
	public int getDcaPort() {
		return dcaPort;
	}
	public void setDcaPort(int dcaPort) {
		this.dcaPort = dcaPort;
	}
	public String getDcaUUID() {
		return dcaUUID;
	}
	public void setDcaUUID(String dcaUUID) {
		this.dcaUUID = dcaUUID;
	}
	public String getDcaAcctid() {
		return dcaAcctid;
	}
	public void setDcaAcctid(String dcaAcctid) {
		this.dcaAcctid = dcaAcctid;
	}
	public String getDcaUsername() {
		return dcaUsername;
	}
	public void setDcaUsername(String dcaUsername) {
		this.dcaUsername = dcaUsername;
	} 
	public String getDcaPasswd() {
		return dcaPasswd;
	}
	public void setDcaPasswd(String dcaPasswd) {
		this.dcaPasswd = dcaPasswd;
	}
	public String getDcaProcessname() {
		return dcaProcessname;
	}
	public void setDcaProcessname(String dcaProcessname) {
		this.dcaProcessname = dcaProcessname;
	}
	public int getDcaPid() {
		return dcaPid;
	}
	public void setDcaPid(int dcaPid) {
		this.dcaPid = dcaPid;
	}
	public String getDcaFlag() {
		return dcaFlag;
	}
	public void setDcaFlag(String dcaFlag) {
		this.dcaFlag = dcaFlag;
	}
	public int getReconnectCount() {
		return reconnectCount;
	}
	public void setReconnectCount(int reconnectCount) {
		this.reconnectCount = reconnectCount;
	}
	public int getReconnectInterval() {
		return reconnectInterval;
	}
	public void setReconnectInterval(int reconnectInterval) {
		this.reconnectInterval = reconnectInterval;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	 
	
	
}
