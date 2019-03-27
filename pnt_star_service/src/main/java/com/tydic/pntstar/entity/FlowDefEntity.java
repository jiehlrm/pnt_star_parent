package com.tydic.pntstar.entity;
/**
 * 流程定义
 * @author lenovo
 *
 */
public class FlowDefEntity {
	private String servId;//流程ID
	private String servName;//流程编码
	private String iStructId;//流程入参（配置在出入参表中）
	private String oStructId;//流程出参（配置在出入参表中）
	private String senceId;//场景ID，-1表示所有业务场景通用
	
	public String getServId() {
		return servId;
	}
	public void setServId(String servId) {
		this.servId = servId;
	}
	public String getServName() {
		return servName;
	}
	public void setServName(String servName) {
		this.servName = servName;
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
	public String getSenceId() {
		return senceId;
	}
	public void setSenceId(String senceId) {
		this.senceId = senceId;
	}
	
	 
	
}
