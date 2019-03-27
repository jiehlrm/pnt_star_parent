package com.tydic.pntstar.util;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;

import org.springframework.util.StringUtils;

public class IPUtil {
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
        if(!StringCommon.isNull(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(!StringCommon.isNull(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }
	
	/**  
     * 判断IP是否在指定范围；  
     */  
        
    public static boolean ipIsValid(String ipStart,String ipEnd, String ip) {   
        if (ipStart == null)   
            throw new NullPointerException("起始IP不能为空！");
        if (ipEnd == null)   
            throw new NullPointerException("结束IP不能为空！");  
        if (ip == null)   
            throw new NullPointerException("IP不能为空！");   
        ipStart = ipStart.trim();   
        ipEnd = ipEnd.trim();
        ip = ip.trim();   
        final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";   
        final String REGX_IPB = REGX_IP + "\\-" + REGX_IP;   
        if (!ipStart.matches(REGX_IP) || !ip.matches(REGX_IP) || !ipEnd.matches(REGX_IP))   
            return false;   
//        int idx = ipSection.indexOf('-');   
        String[] sips = ipStart.split("\\.");   
        String[] sipe = ipEnd.split("\\.");   
        String[] sipt = ip.split("\\.");   
        long ips = 0L, ipe = 0L, ipt = 0L;   
        for (int i = 0; i < 4; ++i) {   
            ips = ips << 8 | Integer.parseInt(sips[i]);   
            ipe = ipe << 8 | Integer.parseInt(sipe[i]);   
            ipt = ipt << 8 | Integer.parseInt(sipt[i]);   
        }   
        if (ips > ipe) {   
            long t = ips;   
            ips = ipe;   
            ipe = t;   
        }   
        return ips <= ipt && ipt <= ipe;   
    }   
    public static void main(String[] args) {   
        if (ipIsValid("192.168.1.1","192.169.1.10", "192.168.3.54")) {   
            System.out.println("ip属于该网段");   
        } else  
            System.out.println("ip不属于该网段");   
    } 
}
