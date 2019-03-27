package com.tydic.pntstar.constant;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.pntstar.dao.CommonDBDao;

/**
 * 全局变量用来创建序列的值
 * @author root
 *
 */

public class Global {
	//序列值
	private static int key=0;
	
	public static int  getPK(){
		return ++key;
	}
	
	public static void setPk(int num){
		if(key==0){
			key=num;
		}
	}
}
