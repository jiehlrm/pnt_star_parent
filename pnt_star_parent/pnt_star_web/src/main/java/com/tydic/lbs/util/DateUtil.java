package com.tydic.lbs.util;

import java.util.Date;
import java.text.SimpleDateFormat;

public class DateUtil {
	public static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
	public static SimpleDateFormat dfm = new SimpleDateFormat("yyyyMMddHHmmssSSS");//设置日期格式

	public static String getNowTime() { 
		return df.format(new Date());// new Date()为获取当前系统时间
	}
	public static String getNowTimeMs() {
		return dfm.format(new Date());// new Date()为获取当前系统时间
	}
}