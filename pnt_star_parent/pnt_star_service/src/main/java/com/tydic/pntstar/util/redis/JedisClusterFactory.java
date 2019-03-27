package com.tydic.pntstar.util.redis;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.core.io.Resource;

import com.tydic.pntstar.util.StringCommon;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/****
 * dca的client包中jedis版本比较低，不支持密码，如果用jedis请把dca的包移除掉
 * 并且如果需要支撑密码的话，还需要把代码中的这行注释去掉
 * //jedisCluster = new JedisCluster(haps, timeout, soTimeout,maxRedirections,password,genericObjectPoolConfig);  
 * @author Administrator
 *
 */
public class JedisClusterFactory  {  

    private Resource addressConfig;  
    private String addressKeyPrefix ;  

    private JedisCluster jedisCluster;  
    private int timeout;  
    private int maxRedirections;  
    private GenericObjectPoolConfig genericObjectPoolConfig;  
    private String  password;
    private int soTimeout;
    
    
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSoTimeout() {
		return soTimeout;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public JedisCluster getJedisCluster() {
		return jedisCluster;
	}

	public void setJedisCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}

	private Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");  
    private Set<HostAndPort> parseHostAndPort() throws Exception {  
        try {  
            Properties prop = new Properties();  
            prop.load(this.addressConfig.getInputStream());  

            Set<HostAndPort> haps = new HashSet<HostAndPort>();  
            for (Object key : prop.keySet()) {  

                if (!((String) key).startsWith(addressKeyPrefix)) {  
                    continue;  
                }  

                String val = (String) prop.get(key);  

                boolean isIpPort = p.matcher(val).matches();  

                if (!isIpPort) {  
                    throw new IllegalArgumentException("ip 或 port 不合法");  
                }  
                String[] ipAndPort = val.split(":");  

                HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));  
                haps.add(hap);  
            }  

            return haps;  
        } catch (IllegalArgumentException ex) {  
            throw ex;  
        } catch (Exception ex) {  
            throw new Exception("解析 jedis 配置文件失败", ex);  
        }  
    }  

    public void afterPropertiesSet() throws Exception {  
        Set<HostAndPort> haps = this.parseHostAndPort();  
        if(StringCommon.isNull(password)) {
            jedisCluster = new JedisCluster(haps, timeout,maxRedirections,genericObjectPoolConfig);  
        }else {
        	//TODO 注意需要把DCA客户端jar包去掉，并且把下面这行代码去掉
            //jedisCluster = new JedisCluster(haps, timeout, soTimeout,maxRedirections,password,genericObjectPoolConfig);  
        }

    }  
    public void setAddressConfig(Resource addressConfig) {  
        this.addressConfig = addressConfig;  
    }  

    public void setTimeout(int timeout) {  
        this.timeout = timeout;  
    }  

    public void setMaxRedirections(int maxRedirections) {  
        this.maxRedirections = maxRedirections;  
    }  

    public void setAddressKeyPrefix(String addressKeyPrefix) {  
        this.addressKeyPrefix = addressKeyPrefix;  
    }  

    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {  
        this.genericObjectPoolConfig = genericObjectPoolConfig;  
    }  

}  