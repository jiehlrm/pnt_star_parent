package com.tydic.lbs.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.entity.Constants;
import com.tydic.lbs.frame.BaseService;
import com.tydic.lbs.frame.LoadSysData;
import com.tydic.lbs.util.DateUtil;
import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.lbs.util.StringCommon;
import com.tydic.pntstar.service.system.CommonService;
import com.tydic.pntstar.service.system.LoadSysDataService;

/***
 * 常用的公共方法
 * @author Administrator
 *
 */
@Service("commonServlet")
public class CommonServlet extends BaseService {
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(CommonServlet.class);
    
    @Autowired
    private CommonService commonService;
    @Autowired
    private LoadSysDataService  loadSysDataService;
    
    public static final String CHAR_ENCODING = "UTF-8";
    public static final String CONTENT_TYPE = "text/html; charset=utf-8";

    /****
     * 获取表头信息
     * @param request
     * @param response
     * @param rtnMap
     */
    public void getTableHead(HttpServletRequest request,
                          HttpServletResponse response, Map<String, Object> rtnMap) {

        try {

            Map params = this.dataToMap_new(request);
            // 获取查询参数
            String serviceName = params.get("service_name") + "";
            // 获取记录
            List<Map<String, Object>> dataList = Constants.getTableHead(params);
            rtnMap.put("success", "success");
            rtnMap.put("data", dataList);
            rtnMap.put("list_flag", "list");

        } catch (Exception e) {
            e.printStackTrace();
            rtnMap.put("success", "fail");
            rtnMap.put("errormsg", e.getMessage());
        }
    }
    
    
    /**
     * 分页查询和不分页
     * 如果是分页则传递到后台的start_num>0，end_num>0
     *
     * @throws IOException
     */
    public void gridValue_rows(HttpServletRequest request,
                               HttpServletResponse response, Map<String, Object> rtnMap) {
        try {
            // 获取流程名
            String flowCode = request.getParameter("flowCode") + "";
            
        	//拼接报文头TcpCont
            JSONObject TcpCont=new JSONObject();
            //端到端调用标识,端到端监控埋点，全局跟踪ID，全局唯一 
            String nowStr=DateUtil.getNowTimeMs();
            TcpCont.put("traceId", Constants.spanId+"_"+nowStr+"_"+RandomStringUtils.randomAlphanumeric(3));
            //端到端监控埋点，调用端ID
            TcpCont.put("spanId", Constants.spanId);
            //父调用端标识
            TcpCont.put("parentSpanId", "0");
            //采样标志,该字段是用于控制采样率，由调用端传递给服务端，也就是说采样率由调用端决定。
            TcpCont.put("sampledFlag", "0");
            TcpCont.put("debugFlag", "0");
            TcpCont.put("flowCode", flowCode);
            TcpCont.put("reqTime", nowStr);
            //拼接业务参数SvcCont
            String SvcContReqStr = request.getParameter("SvcCont") + "";
            JSONObject  SvcCont=JSONObject.parseObject(SvcContReqStr);
            //获取操作员信息
            JSONObject operAttrStruct=super.getOperAttrStruct(request,flowCode);
            SvcCont.putAll(operAttrStruct);
            
            //获取分页数据
           String  pageIndex=request.getParameter("pageIndex");
           String  pageSize=request.getParameter("pageSize");
            int startNum = pageIndex == null?-1:
    			Integer.parseInt(pageIndex+"")*
    			(pageSize == null?-1:Integer.parseInt(pageSize+""))+1;
    		int endNum = (startNum-1)+(pageSize == null?-1:Integer.parseInt(pageSize+""));
    		if(startNum > 0){
    			SvcCont.put("start_num", startNum);
    		}
    		if(endNum > 0){
    			SvcCont.put("end_num", endNum);
    		}
            
            //发起请求
            JSONObject jsonRest=new JSONObject();
            jsonRest.put("TcpCont", TcpCont);
            jsonRest.put("SvcCont", SvcCont);

            // 调用dubbo服务
            String jsonRestStr=jsonRest.toJSONString();
            logger.debug("send------->"+jsonRestStr);
        	String jsonResultStr=commonService.invokeFlow(jsonRestStr);
        	 logger.debug("receive<-------"+jsonResultStr);
        	JSONObject jsonResult=JSONObject.parseObject(jsonResultStr).getJSONObject("SvcCont");
        	//响应码
        	String resultCode=jsonResult.getString("resultCode");
        	String resultMsg=jsonResult.getString("resultMsg");
        	String totalRecord=jsonResult.getString("totalRecord");
        	
        	//如果响应成功则放入数据
        	if (Constants.resultCodeSuccess.equals(resultCode)) {
                // 获取记录数
                long count = 0;
                if (!StringCommon.isNull(totalRecord)) {
                	count=Long.parseLong(totalRecord);
    			}
                JSONArray  dataList=null;
                //如果结果>0则获取数据集合
                if(count>0) {
                	//获取数据清单
                    dataList=jsonResult.getJSONArray("dataList");
                    if (dataList==null||dataList.size()<1) {
    					//如果没找到则遍历所有key获取
                    	Set<String> keySet=jsonResult.keySet();
                    	for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
    						String keyStr = (String) iterator.next();
    						Object value=jsonResult.get(keyStr);
    						if (value instanceof JSONArray) {
    							dataList = (JSONArray) value;
    							break;
    						}
    					}
    				}
                }
                
                rtnMap.put("success", "success");
                rtnMap.put("total", count);
                rtnMap.put("data", dataList);
			}else {
				 rtnMap.put("success", "fail");
		         rtnMap.put("errormsg", resultMsg);
			}
        } catch (Exception e) {
            e.printStackTrace();
            rtnMap.put("success", "fail");
            rtnMap.put("errormsg", e.getMessage());
        }
    }
    
    /**
     * 初始化表头信息对应的表为TB_PTY_CODE表
     * @throws IOException
     */
    public void initComboxInfo(HttpServletRequest request,
                          HttpServletResponse response, Map<String, Object> rtnMap) {

        try {
        	 Map params = this.dataToMap_new(request);
            // 获取记录
        	JSONObject result=JSONObject.parseObject(loadSysDataService.loadCombox(JSONObject.toJSONString(params)));
            rtnMap.put("success", result.getString("success"));
            rtnMap.put("data", result.get("data"));
            rtnMap.put("list_flag", "list");

        } catch (Exception e) {

            e.printStackTrace();
            rtnMap.put("success", "fail");
            rtnMap.put("errormsg", e.getMessage());
        }
    }
    
    
    /***
     * 刷新表头
     *  http://localhost:8080/pnt_star_web/ControlServlet.do?serviceName=commonServlet&methodName=freshTableHead
     * @param request
     * @param response
     * @param rtnMap
     */
    public void freshTableHead(HttpServletRequest request,
            HttpServletResponse response, Map<String, Object> rtnMap) {
    	LoadSysData sysData=(LoadSysData)SpringBeanUtil.getBean("loadSysData");
    	try {
			sysData.handFreshTableHead();
			rtnMap.put("success", "success");
            rtnMap.put("data", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			rtnMap.put("success", "fail");
            rtnMap.put("errormsg", e.getMessage());
		}
    }
    
    /***
     * 刷新流程信息url,method
     * http://localhost:8080/pnt_star_web/ControlServlet.do?serviceName=commonServlet&methodName=freshFlowInfo
     * @param request
     * @param response
     * @param rtnMap
     */
    public void freshFlowInfo(HttpServletRequest request,
            HttpServletResponse response, Map<String, Object> rtnMap) {
    	LoadSysData sysData=(LoadSysData)SpringBeanUtil.getBean("loadSysData");
    	try {
			sysData.handFreshFlowInfo();
			rtnMap.put("success", "success");
            rtnMap.put("data", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			rtnMap.put("success", "fail");
            rtnMap.put("errormsg", e.getMessage());
		}
    }
    
    
    
}
    
    



