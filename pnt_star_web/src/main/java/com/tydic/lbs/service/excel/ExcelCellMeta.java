package com.tydic.lbs.service.excel;

public class ExcelCellMeta {
	private int colspan;
	private int rowspan;
	private String header;
	private boolean inMerge;
	private String coltype;
	private String dataIndex;
	private String field;
	private boolean hidden;
	
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	
	public int getRowspan() {
		return rowspan;
	}
	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}
	public boolean isInMerge() {
		return inMerge;
	}
	public void setInMerge(boolean inMerge) {
		this.inMerge = inMerge;
	}
 
	public int getColspan() {
		return colspan;
	}
	public void setColspan(int colspan) {
		this.colspan = colspan;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getColtype() {
		return coltype;
	}
	public void setColtype(String coltype) {
		this.coltype = coltype;
	}
	public String getDataIndex() {
		return dataIndex;
	}
	public void setDataIndex(String dataIndex) {
		this.dataIndex = dataIndex;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	
	
}
