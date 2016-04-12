package ssh.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;

public class HadoopUtils {
	private static Configuration conf;
	private static Map<String,String> confMap= new HashMap<>();
	public static final String hadoop_properties="hadoop.properties";
	private static void init(){
		Properties props = new Properties();
		InputStream in=null;
        try {
         in = HadoopUtils.class.getClassLoader().getResourceAsStream(hadoop_properties);
         props.load(in);
            Enumeration<?> en = props.propertyNames();
             while (en.hasMoreElements()) {
            	 String key = (String) en.nextElement();
//            	 String Property = props.getProperty (key);
            	 confMap.put(key, props.getProperty (key));
             }
        } catch (Exception e) {
        	e.printStackTrace();
        }finally{
        	try {
        		if(in!=null){
        			in.close();
        		}
			} catch (IOException e) {
				e.printStackTrace();
			}
        
        }
	}
	
	public static Configuration getConf() {
		if (conf == null) {
			init();
			conf = new Configuration();
			conf.setBoolean("mapreduce.app-submission.cross-platform", 
					Boolean.parseBoolean(confMap.get("mapreduce.app-submission.cross-platform")));// 配置使用跨平台提交任务
			conf.set("fs.defaultFS",confMap.get("fs.defaultFS") );// 指定namenode
			conf.set("mapreduce.framework.name", 
					confMap.get("mapreduce.framework.name")); // 指定使用yarn框架
			conf.set("yarn.resourcemanager.address",
					confMap.get("yarn.resourcemanager.address") ); // 指定resourcemanager
			conf.set("yarn.resourcemanager.scheduler.address", 
					confMap.get("yarn.resourcemanager.scheduler.address"));// 指定资源分配器
			conf.set("mapreduce.jobhistory.address", 
					confMap.get("mapreduce.jobhistory.address"));// 指定historyserver
		}
		return conf;
	}
}
