package com.tydic.pntstar.entity;

import java.util.Set;

/***
 * 出参入参
 * @author Administrator
 *
 */
public class StructFieldDef {
	private String structFieldId;//主键
	private String structId;//出入参ID（外键，与流程、原子服务关联）
	private String fieldName;//出入参对应KEY值
	private String fieldSeq;//出入参对应json报文的位置
	private String upFieldId;//上一级ID（标识该节点是否为根节点，如果是根节点填-1，如果不是根节点则填上一级节点ID）
	private String fieldLayer;//字段所在层数
	private String valueKey;//取值所需字段
	/**
	 * 字段类型
	 * 字段类型：
		1- 整型；
		2- 无符号整型；
		3- 长整形；
		4- 无符号长整形
		5- 单精度浮点；
		6- 双精度浮点；
		7- 布尔类型；
		8- 字符串；
		100- Group集合/矩阵
	 */
	private String fieldType;
	/**
	 * 取值多次出现：
		0- 否，仅一次；
		1- 是，有可能多个取值
	 */
	private String multiFlag;
	private int checkType;//参数校验类型, 0:不校验, 1:校验参数是否存在, 2:校验参数是否为空
	private String requestFieldName;//如果该字段配置了值则表示，如果入参JSON存在配置的节点，则返回需返回该记录
	private String defaultValue;//默认值
	
	private Set<StructFieldDef> childStructFieldDef=null;
	
	public String getStructFieldId() {
		return structFieldId;
	}
	public void setStructFieldId(String structFieldId) {
		this.structFieldId = structFieldId;
	}
	public String getStructId() {
		return structId;
	}
	public void setStructId(String structId) {
		this.structId = structId;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldSeq() {
		return fieldSeq;
	}
	public void setFieldSeq(String fieldSeq) {
		this.fieldSeq = fieldSeq;
	}
	public String getUpFieldId() {
		return upFieldId;
	}
	public void setUpFieldId(String upFieldId) {
		this.upFieldId = upFieldId;
	}
	public String getFieldLayer() {
		return fieldLayer;
	}
	public void setFieldLayer(String fieldLayer) {
		this.fieldLayer = fieldLayer;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getMultiFlag() {
		return multiFlag;
	}
	public void setMultiFlag(String multiFlag) {
		this.multiFlag = multiFlag;
	}
	public Set<StructFieldDef> getChildStructFieldDef() {
		return childStructFieldDef;
	}
	public void setChildStructFieldDef(Set<StructFieldDef> childStructFieldDef) {
		this.childStructFieldDef = childStructFieldDef;
	}
	public String getRequestFiledName() {
		return requestFieldName;
	}
	public void setRequestFiledName(String requestFieldName) {
		this.requestFieldName = requestFieldName;
	}
	public int getCheckType() {
		return checkType;
	}
	public void setCheckType(int checkType) {
		this.checkType = checkType;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getValueKey() {
		return valueKey;
	}
	public void setValueKey(String valueKey) {
		this.valueKey = valueKey;
	}
	public String getRequestFieldName() {
		return requestFieldName;
	}
	public void setRequestFieldName(String requestFieldName) {
		this.requestFieldName = requestFieldName;
	} 
 	
	
}
