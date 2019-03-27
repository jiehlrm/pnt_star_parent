package com.tydic.pntstar.service.impl.demo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.frame.MybatisInitFactory;
import com.tydic.pntstar.service.demo.TestService;
import com.tydic.pntstar.service.impl.openapi.PointAdjustServiceImpl;
import com.tydic.pntstar.service.utils.DefaultBusinessService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;
@Service("testServiceImpl")
public class TestServiceImpl implements TestService {

	
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
	
	private static final Logger logger = LoggerFactory.getLogger(PointAdjustServiceImpl.class);

	
	@Autowired
	private DefaultBusinessService defaultBusinessService;
	
	@Autowired
	private MybatisInitFactory mybatisInitFactory;
	
	public void sayHello(String saystr,String str) {
		System.out.println(saystr);
		System.out.println(str);
	}

	@Override
	public void sayWhat(String jsonStr) {
		System.out.println(jsonStr);
		
	}

	/* 
	 * 测试sql的正确性
	 */
	@Override
	public String testsql(String JsonStr) {
		// TODO Auto-generated method stub
		Map<String,Object> param = null;//请求参数
		try {
			 //1: 解析获取参数
            param=defaultBusinessService.getParam(JsonStr);
			List<Map<String, Object>> list=dao.query(param.get("sqlName").toString(),param);
			return Tools.buildResponseData(Tools.SUCCESS,"sql查询成功",list);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseData(Tools.FAILED,"sql查询失败",e.getMessage());
		}
		
	}

	/* 
	 * 测试服务
	 */
	@Override
	public String testService(String JsonStr) {
		Map<String,Object> param=defaultBusinessService.getParam(JsonStr);
		String  result =null;
		String methodName=param.get("methodName").toString();
		Class className=SpringBeanUtil.getBean(param.get("className").toString()).getClass();//获取执行类
		try {
			Method m =null;
		    if(Tools.isNull(param.get("methodParam"))) {
		    	m=className.getDeclaredMethod(methodName);
		    }else {
		    	m=className.getDeclaredMethod(methodName,String.class);
		    }
			try {
				 if(Tools.isNull(param.get("methodParam"))) {
				    	m.invoke(SpringBeanUtil.getBean(param.get("className").toString()),null);
				    }else {
				    	result=(String) m.invoke(SpringBeanUtil.getBean(param.get("className").toString()),JsonStr);
				    }
				return result;
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.tydic.pntstar.service.demo.TestService#refresh()
	 */
	@Override
	public String refresh() {
		// TODO Auto-generated method stub
		try {
			mybatisInitFactory.initMybatis();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "刷新成功";
		
		
	}
	

}
