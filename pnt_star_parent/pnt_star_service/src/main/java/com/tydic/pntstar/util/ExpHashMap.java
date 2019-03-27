package com.tydic.pntstar.util;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * 扩展HashMap
 * @author xkarvy
 *
 */
@SuppressWarnings("unchecked")
public class ExpHashMap extends HashMap implements Serializable{

	private static final long serialVersionUID = 4518714865666285656L;

	public String getString(String key){
		return String.valueOf(this.get(key));
	}
	
	public String getStrNotNull(String key){
		if(Tools.isNull(this.get(key)))
			return "";
		return String.valueOf(this.get(key));
	}
	
	public int getInt(String key){
		return Integer.valueOf(this.getString(key)).intValue();
	}
	
	public double getDouble(String key){
		return Double.valueOf(this.getString(key)).doubleValue();
	}
	
	public float getFloat(String key){
		return Float.valueOf(this.getString(key)).floatValue();
	}
	
	public long getLong(String key){
		return Long.valueOf(this.getString(key)).longValue();
	}
	
	
	public Date getDate(String key){
		return (Date) this.get(key);
	}
	
	public Timestamp getTimestamp(String key){
		return (Timestamp) this.get(key);
	}
	
	
}
