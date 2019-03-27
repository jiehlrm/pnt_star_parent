package com.tydic.pntstar.frame;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.pntstar.util.SysdataLoad;

public class Start {
	 public static void main(String[] args) throws Exception {
	        //Prevent to get IPV6 address,this way only work in debug mode
	        //But you can pass use -Djava.net.preferIPv4Stack=true,then it work well whether in debug mode or not
	        System.setProperty("java.net.preferIPv4Stack", "true");
		 
	        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring/applicationContext.xml"});
	        context.start();
	       // SysdataLoad.init();
	        System.out.println("provider start done ..........");
        	synchronized (Start.class) {
                while (true) {
                    try {
                    	Start.class.wait();
                    } catch (InterruptedException e) {
            	        System.out.println("errror ..........");
                    }
                }
            }
	    
	    }
}

