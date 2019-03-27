package com.tydic.pntstar.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
	public static SimpleDateFormat dfm = new SimpleDateFormat("yyyyMMddHHmmssSSS");//设置日期格式
	public static SimpleDateFormat df3 = new SimpleDateFormat("yyyyMM");//设置日期格式
	public static SimpleDateFormat df4 = new SimpleDateFormat("MMddHHmmss");//设置日期格式
	

	public static String getNowTime() { 
		return df.format(new Date());// new Date()为获取当前系统时间
	}
	public static String getNowTimeMs() {
		return dfm.format(new Date());// new Date()为获取当前系统时间
	}
	public static String getNowTimeYM() {
		return df3.format(new Date());// new Date()为获取当前系统时间
	}
}