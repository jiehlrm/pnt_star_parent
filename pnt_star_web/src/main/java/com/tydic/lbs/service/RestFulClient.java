package com.tydic.lbs.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.entity.Constants;
import com.tydic.lbs.util.DateUtil;
import com.tydic.lbs.util.ServerConfig;
import com.tydic.lbs.util.StringCommon;

/***
 * 使用httpclient调用外部restful服务
 * 系统内部服务走dubbo调用
 * @author Administrator
 *
 */
public class RestFulClient {
	private static Logger logger = LoggerFactory.getLogger(RestFulClient.class);

	private static int SocketTimeout=6000;
	private static int ConnectTimeout=6000;
	private static int ConnectionRequestTimeout=6000;
	private static int HTTP_MAXTOTAL=200;
	private static int HTTP_MaxPerRoute=200;
	private static CloseableHttpClient client=null;
	static {
		try {
			ServerConfig serverConfig=new ServerConfig();
			SocketTimeout =Integer.parseInt(serverConfig.getValue("HTTP_SOCKETTIMEOUT"));
			ConnectTimeout=Integer.parseInt(serverConfig.getValue("HTTP_CONNECTTIMEOUT"));
			ConnectionRequestTimeout=Integer.parseInt(serverConfig.getValue("HTTP_CONNECTIONREQUESTTIMEOUT"));
			HTTP_MAXTOTAL=Integer.parseInt(serverConfig.getValue("HTTP_MAXTOTAL"));
			HTTP_MaxPerRoute=Integer.parseInt(serverConfig.getValue("HTTP_MaxPerRoute"));

			 PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
			    // 将最大连接数增加到200
			    cm.setMaxTotal(HTTP_MAXTOTAL);
			    // 将每个路由基础的连接增加到20
			    cm.setDefaultMaxPerRoute(HTTP_MaxPerRoute);

			    client = HttpClients.custom()
			            .setConnectionManager(cm)
			            .build();
			    
			    
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	/***
	 * 调用restful服务
	 * @param flowCode 流程编码
	 * @param Jsonparam  提交到后台的参数
	 * @param jsonPathParam  替换路径中的参数/accumulation/{accuId}例如accuId
	 * @return
	 * @throws Exception 
	 */
	public   static String invokeRestful(String flowCode,JSONObject Jsonparam,Map<String,String>  jsonPathParam) throws Exception {
				Map<String, String> urlMap=Constants.getUrlMap(flowCode);
				if (urlMap==null||!urlMap.containsKey("REST_METHOD")||!urlMap.containsKey("REST_URL")) {
					logger.error("can't find url info by flowCode["+flowCode+"]!");
					throw new Exception("can't find url info by flowCode["+flowCode+"]!");
				}
				String method=urlMap.get("REST_METHOD").toUpperCase();
				String url=urlMap.get("REST_URL");
				//替换path中的参数/accumulation/{accuId}例如accuId
				if(jsonPathParam!=null) {
					for (Map.Entry<String, String> m : jsonPathParam.entrySet()) {
						url.replace("{"+m.getKey()+"}", StringCommon.trimNull(m.getValue()));
						}
				}
				return invoke(url,Jsonparam,method);
	}
	
	public  static String invoke(String url,JSONObject param,String method) {
		logger.debug("invokeRestFul \r\n"+method+" "+url+"\r\nparam:\r\n"+param);
		String result="";
		CloseableHttpResponse response = null;

		try {
	        if("GET".equals(method)){
	        	//资源查询
	        	//构建查询参数
	        	URIBuilder builder = new URIBuilder(url);
	            Set<String> set = param.keySet();
	            for(String key: set){
	                builder.setParameter(key, param.get(key).toString());
	            }
	            HttpGet request = new HttpGet(builder.build());
	            //设置超时时间
	            RequestConfig requestConfig = RequestConfig.custom()
	                    .setSocketTimeout(SocketTimeout)
	                    .setConnectTimeout(ConnectTimeout)
	                    .setConnectionRequestTimeout(ConnectionRequestTimeout).build();
	            request.setConfig(requestConfig);
	            //设置头信息Content-Type和编码
	            request.setHeader(new BasicHeader("Content-Type", "application/json; charset=utf-8"));
	            
	            
	            //端到端调用标识,端到端监控埋点，全局跟踪ID，全局唯一 
	            String nowStr=DateUtil.getNowTimeMs();
	            request.setHeader(new BasicHeader("traceId", Constants.spanId+"_"+nowStr+"_"+RandomStringUtils.randomAlphanumeric(3)));
	            //端到端监控埋点，调用端ID
	            request.setHeader(new BasicHeader("spanId", Constants.spanId));
	            //父调用端标识
	            request.setHeader(new BasicHeader("parentSpanId", "0"));
	            //采样标志,该字段是用于控制采样率，由调用端传递给服务端，也就是说采样率由调用端决定。
	            request.setHeader(new BasicHeader("sampledFlag", "1"));
	            
	            //发起http请求
	            response = client.execute(request);
	            HttpEntity entity = response.getEntity();
	            //解析http请求
	            result= EntityUtils.toString(entity,"utf-8");
	            //释放连接
	            response.close();
	            response=null;
	        }else if("POST".equals(method)){
	        	//资源新建走POST，任务类也走POST,全部放入body中
	        	//post请求并且直接发送json字符串
	            HttpPost request2 = new HttpPost(url);
	            //设置超时时间
	            RequestConfig requestConfig = RequestConfig.custom()
	                    .setSocketTimeout(SocketTimeout)
	                    .setConnectTimeout(ConnectTimeout)
	                    .setConnectionRequestTimeout(ConnectionRequestTimeout).build();
	            request2.setConfig(requestConfig);
	            //设置头信息Content-Type和编码
	            request2.setHeader(new BasicHeader("Content-Type", "application/json; charset=utf-8"));
	            
	            //端到端调用标识,端到端监控埋点，全局跟踪ID，全局唯一 
	            String nowStr=DateUtil.getNowTimeMs();
	            request2.setHeader(new BasicHeader("traceId", Constants.spanId+"_"+nowStr+"_"+RandomStringUtils.randomAlphanumeric(3)));
	            //端到端监控埋点，调用端ID
	            request2.setHeader(new BasicHeader("spanId", Constants.spanId));
	            //父调用端标识
	            request2.setHeader(new BasicHeader("parentSpanId", "0"));
	            //采样标志,该字段是用于控制采样率，由调用端传递给服务端，也就是说采样率由调用端决定。
	            request2.setHeader(new BasicHeader("sampledFlag", "1"));
	            
	            //直接发送json字符串
	            StringEntity se = new StringEntity(param.toJSONString(),Charset.forName("UTF-8"));
                request2.setEntity(se);
                //发起http请求
                response = client.execute(request2);
	            HttpEntity entity = response.getEntity();
	            //解析http请求
	            result= EntityUtils.toString(entity,"utf-8");
	            //释放连接
	            response.close();
	            response=null;
	        }else if("DELETE".equals(method)){
	        	//暂时没有使用DELETE的场景
	        	throw new Exception("暂时不支持DELETE的场景");
	        }else if("PUT".equals(method)){
	        	//暂时没有使用PUT的场景
	        	throw new Exception("暂时不支持PUT的场景");
	        }else if("PATCH".equals(method)){
	        	//修改场景，参数也是直接放入body中
	        	//post请求并且直接发送json字符串
	        	HttpPatch request2 = new HttpPatch(url);
	            //设置超时时间
	            RequestConfig requestConfig = RequestConfig.custom()
	                    .setSocketTimeout(SocketTimeout)
	                    .setConnectTimeout(ConnectTimeout)
	                    .setConnectionRequestTimeout(ConnectionRequestTimeout).build();
	            request2.setConfig(requestConfig);
	            //直接发送json字符串
	            StringEntity se = new StringEntity(param.toJSONString(),Charset.forName("UTF-8"));
	            
	            //设置头信息
	            request2.setHeader(new BasicHeader("Content-Type", "application/json; charset=utf-8"));
                //端到端调用标识,端到端监控埋点，全局跟踪ID，全局唯一 
	            String nowStr=DateUtil.getNowTimeMs();
	            request2.setHeader(new BasicHeader("traceId", Constants.spanId+"_"+nowStr+"_"+RandomStringUtils.randomAlphanumeric(3)));
	            //端到端监控埋点，调用端ID
	            request2.setHeader(new BasicHeader("spanId", Constants.spanId));
	            //父调用端标识
	            request2.setHeader(new BasicHeader("parentSpanId", "0"));
	            //采样标志,该字段是用于控制采样率，由调用端传递给服务端，也就是说采样率由调用端决定。
	            request2.setHeader(new BasicHeader("sampledFlag", "1"));
	            
                request2.setEntity(se);
                //调用
                response = client.execute(request2);
	            HttpEntity entity = response.getEntity();
	            result=EntityUtils.toString(entity,"utf-8");
	            //释放连接
	            response.close();
	            response=null;
	        }
	    } catch (Exception e) {
	    	logger.error("invoke restful error:",e);
	    }finally {
			if(response!=null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		logger.debug("invokeRestFul result\r\n"+result);

		return result;
	}
	
	
    
}
