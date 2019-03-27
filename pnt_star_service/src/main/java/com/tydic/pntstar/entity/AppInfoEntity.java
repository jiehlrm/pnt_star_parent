package com.tydic.pntstar.entity;
/**
 * 应用信息
 * @author lenovo
 *
 */
public class AppInfoEntity {
	private String app_id;
	private String app_secret;
	private String app_name;
	private String if_crypt;
	private String ip_start;
	private String ip_end;
	private String is_white;
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getApp_secret() {
		return app_secret;
	}
	public void setApp_secret(String app_secret) {
		this.app_secret = app_secret;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getIf_crypt() {
		return if_crypt;
	}
	public void setIf_crypt(String if_crypt) {
		this.if_crypt = if_crypt;
	}
	public String getIp_start() {
		return ip_start;
	}
	public void setIp_start(String ip_start) {
		this.ip_start = ip_start;
	}
	public String getIp_end() {
		return ip_end;
	}
	public void setIp_end(String ip_end) {
		this.ip_end = ip_end;
	}
	public String getIs_white() {
		return is_white;
	}
	public void setIs_white(String is_white) {
		this.is_white = is_white;
	}
	
}
