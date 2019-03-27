package com.tydic.pntstar.service.impl.system;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.util.SpringBeanUtil;

import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

@Service("exchangeFtpJobSerivceImpl")
public class ExchangeFtpJobSerivceImpl {
    private Logger logger = LoggerFactory.getLogger(ExchangeFtpJobSerivceImpl.class);
    private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
    
    //ftp信息
    public static String EXCHANGEFTP_IP = "exchangeftp.ip";
    public static String EXCHANGEFTP_USER = "exchangeftp.user";
    public static String EXCHANGEFTP_PWD = "exchangeftp.pwd";
    //定时器默认地址
    public static String EXCHANGEFTP_ADJUST_POINT_IP = "exchangeftp.adjust_point_ip";
    //ftp上传路径
    public static String EXCHANGEFTP_PATH = "exchangeftp.path";
    //ETE_[容灾标识（默认为0）]_[业务系统编码（文档附1）]_[稽核点编码（CEI+指标顺序<01文档集团规范>）]_[信息点类型（默认为0）]_[传输频度（文档2.4）]_
    public static String EXCHANGEFTP_FILENAMEPRE = "exchangeftp.filenamepre";
    //_[区域编码（61）]_[重传标志（默认为0）]_
    public static String EXCHANGEFTP_FILENAMESUF = "exchangeftp.filenamesuf";
    //一次上传的最大数据量
    public static String EXCHANGEFTP_QUANTITY = "exchangeftptp.quantity";
    

	public void exchangeFtpAutoInit() throws IOException, FtpProtocolException{
		String ip = EXCHANGEFTP_IP;
		//TODO,此处需要控制只在一台机子上执行
		//logger.info("获取本机IP >........"+ip);
		
		//1.取日志表中type=0的数据，生成json文件，并ftp上传至端到端系统；
		List<Map<String,Object>> datas = dao.query("QueryExchFtpJob", null);
	    //TODO 获取数据，
	

		//这里还需要获取latn_id 并且转换：area_code 对应关系：8610101-290,8610401-910,8610601-911,8610801-912,8610501-913,8611001-914,8610901-915,8610701-916,
		//8610301-917,8610201-919
		
		
		//2.登录ftp
		FtpClient ftpClient = ftpClientLog();
		
		
		int quantity = Integer.valueOf(EXCHANGEFTP_QUANTITY); 
		if(datas.size()>quantity){//以38000条为一个单位进行传输
		List<List<Map>> wrapList = new ArrayList<List<Map>>();
        int count = 0;
        while (count < datas.size()) {//以38000条分组
            wrapList.add(new ArrayList(datas.subList(count, (count + quantity) > datas.size() ? datas.size() : count + quantity)));
            count += quantity;
        }
        //按个数循环上传
		for(int i=0;i<wrapList.size();i++){
			List<Map> data =  wrapList.get(i);
			JSONArray json = new JSONArray(); 
			for(Map m : data){
	             JSONObject jo = new JSONObject();
	             jo.put("cust_id", m.get("cust_id"));
	             jo.put("list_id", m.get("list_id"));
	             jo.put("points_value", m.get("points_value"));
	             jo.put("list_type", m.get("list_type"));
	             jo.put("create_date", m.get("create_date"));
	             jo.put("accout", m.get("accout"));
	             jo.put("area_code", m.get("area_code"));
	             jo.put("item_name", m.get("item_name"));
	             jo.put("item_code", m.get("item_code"));
	             jo.put("istransfered", m.get("istransfered"));
	             json.add(jo);
	         }
			  ftpUpload(ftpClient, json, i+1, json.size());//登录ftp上传文件
		}
		}else{
			JSONArray json = new JSONArray(); 
			for(Map m : datas){
	             JSONObject jo = new JSONObject();
	             jo.put("cust_id", m.get("cust_id"));
	             jo.put("list_id", m.get("list_id"));
	             jo.put("points_value", m.get("points_value"));
	             jo.put("list_type", m.get("list_type"));
	             jo.put("create_date", m.get("create_date"));
	             jo.put("accout", m.get("accout"));
	             jo.put("area_code", m.get("area_code"));
	             jo.put("item_name", m.get("item_name"));
	             jo.put("item_code", m.get("item_code"));
	             jo.put("istransfered", m.get("istransfered"));
	             json.add(jo);
	         }
			 ftpUpload(ftpClient, json, 1, json.size());//登录ftp上传文件
		}
		 //3.关闭ftp
		 ftpClient.close();
	     logger.info("----------ftp 服务器断开");
	      }
	
	 public FtpClient ftpClientLog() throws IOException{
		    String ip=EXCHANGEFTP_IP ,user=EXCHANGEFTP_USER,password=EXCHANGEFTP_PWD,path=EXCHANGEFTP_PATH;
	        FtpClient ftpClient = null;
	        try{
				ftpClient = FtpClient.create(ip);
				ftpClient.login(user, password.toCharArray());
				// 设置成2进制传输
				ftpClient.setBinaryType();
				System.out.println("已登录服务器");
				logger.info("----------ftp 登录成功!");
				if (path.length() != 0) {
				// 把远程系统上的目录切换到参数path所指定的目录
				ftpClient.changeDirectory(path);
				}
				logger.info("----------ftp 目录切换成功!目录："+path);
			}catch(IOException  | FtpProtocolException a){
				 a.printStackTrace();
				 logger.error("连接ftp至目录失败，检查ip，账户配置以及路径等");  	
				 logger.error(a.getMessage(),a);  	
			}
	        return ftpClient;
		   
	   }


		public void ftpUpload(FtpClient ftpClient, JSONArray json, int i, int count) throws IOException, FtpProtocolException{
			String FileNamePre = EXCHANGEFTP_FILENAMEPRE;
			String FileNameSuf = EXCHANGEFTP_FILENAMESUF;
		    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
	        SimpleDateFormat df2 = new SimpleDateFormat("HH");//设置小时格式
			String nowDate =  df.format(new Date());
			TelnetOutputStream os = null;
			InputStream is = null;
		    String str = "";
		    for(int l =0; l<json.size(); l++){
		    	str = str+json.get(l)+"\n";
		    }
		    logger.info("----------开始上传json文件"+i);
		    logger.info("----------上传数据为："+json.size()+"条");
		    String random = "0000"+df2.format(new Date());//当前小时
		    String fileName = FileNamePre+nowDate+"_"+random+FileNameSuf+"00"+i+".json";
		    try{
		    	str = new String(str.getBytes("UTF-8"),"UTF-8");
				is = new ByteArrayInputStream(str.getBytes());//本地文件输入流
				 // 将远程文件加入输出流中  
		    	 os = (TelnetOutputStream) ftpClient.putFileStream(fileName, false);
		    	uploadFile(ftpClient, os, is);
		    	//插入上传日志表 FTP_POINT_LOG 
		    	insertFtpLog(fileName,"0",count);
			}catch(IOException e){
				 logger.info(e.getMessage(),e);  
				 logger.info("----------第一次上传失败："+fileName+",正在重传。。。");
				 try {
					uploadFile(ftpClient, os, is);
				 } catch (IOException e1) {
					logger.info(e1.getMessage(),e1);  
					logger.info("----------第二次上传失败："+fileName+",正在重传。。。");
					try {
						uploadFile(ftpClient, os, is);
					} catch (IOException e2) {
						e2.printStackTrace();
						logger.info("----------三次重传失败，请看报错日志："+fileName);
						logger.error("----------三次重传失败："+fileName);
						logger.error(e2.getMessage(),e2);  
						//插入上传日志表 FTP_POINT_LOG 
						insertFtpLog(fileName,"1",count);
					}
				}
			} finally {  // 关闭连接
			     if (is != null) {  
	                is.close();  
	            }  
	             if (os != null) {  
	                os.close();  
	            }  
	            }  
		}
		
		

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private void insertFtpLog(String fileName, String status, int i) {
			Map param = new HashMap();
			param.put("fileName", fileName);
			param.put("status", status);
			param.put("count", i);
			dao.insert("", param);
			//TODO 创建log表，并且进行如下配置
			// insert into FTP_POINT_LOG(file_name,status,ACCOUT,create_date)
		    // values(#{fileName},#{status},#{count},sysdate)
		}


		/**
		 * 文件上传
		 * @throws IOException
		 */
		private void uploadFile(FtpClient ftpClient, TelnetOutputStream os, InputStream is) throws IOException {
			 //FileInputStream is = new FileInputStream(new File("C:/offline_FtnInfo.txt"));
			 byte[] b  = new byte[4096];
			 int c;
			 while((c=is.read(b))!=-1){
				 os.write(b, 0, c);
			 }
		}
	
	
}
