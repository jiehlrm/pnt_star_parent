package com.tydic.pntstar.entity;

/***
 * 流程与原子服务关系
 * @author Administrator
 *
 */
public class FlowServRelEntity {
	private String servEditId;//主键ID
	private String servId;//流程ID
	private String servInfId;//原子服务ID
	private String step;//原子服务执行步骤
	private String outPutFlag;//环节能力是否输出处理结果
	
	public String getServEditId() {
		return servEditId;
	}
	public void setServEditId(String servEditId) {
		this.servEditId = servEditId;
	}
	public String getServId() {
		return servId;
	}
	public void setServId(String servId) {
		this.servId = servId;
	}
	public String getServInfId() {
		return servInfId;
	}
	public void setServInfId(String servInfId) {
		this.servInfId = servInfId;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getOutPutFlag() {
		return outPutFlag;
	}
	public void setOutPutFlag(String outPutFlag) {
		this.outPutFlag = outPutFlag;
	}
	
	
 }
