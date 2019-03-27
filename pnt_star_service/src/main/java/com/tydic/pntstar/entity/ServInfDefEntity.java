package com.tydic.pntstar.entity;

/***
 * 原子服务的定义
 * @author Administrator
 *
 */
public class ServInfDefEntity {
	private String servInfId;
	//原子服务对应的spring注解中的beanId
	private String className;
	//入参ID
	private String iStructId;
	private String oStructId;//出参ID
	private String state;//	原子服务状态
	
	public String getServInfId() {
		return servInfId;
	}
	public void setServInfId(String servInfId) {
		this.servInfId = servInfId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getiStructId() {
		return iStructId;
	}
	public void setiStructId(String iStructId) {
		this.iStructId = iStructId;
	}
	public String getoStructId() {
		return oStructId;
	}
	public void setoStructId(String oStructId) {
		this.oStructId = oStructId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
 }
