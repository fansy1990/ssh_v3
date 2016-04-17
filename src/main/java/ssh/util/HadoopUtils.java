package ssh.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HadoopUtils {
	public static final Logger log = LoggerFactory.getLogger(HadoopUtils.class);

	private static Configuration conf;
	private static Map<String, String> confMap = new HashMap<>();
	public static final String hadoop_properties = "hadoop.properties";
	private static FileSystem fs;

	public void initFs() {
		try {

			fs = FileSystem.get(getConf());
			log.info("Hadoop文件系统FS被初始化！");
		} catch (IOException e) {
			e.printStackTrace();
			log.info("获取Hadoop文件系统异常!");
			fs = null;
		}
	}

	private static void init() {
		Properties props = new Properties();
		InputStream in = null;
		try {
			in = HadoopUtils.class.getClassLoader().getResourceAsStream(
					hadoop_properties);
			props.load(in);
			Enumeration<?> en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				// String Property = props.getProperty (key);
				confMap.put(key, props.getProperty(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				log.info("获取Hadoop配置文件失败！");
			}

		}
	}

	public static Configuration getConf() {
		if (conf == null) {
			init();
			conf = new Configuration();
			conf.setBoolean("mapreduce.app-submission.cross-platform", Boolean
					.parseBoolean(confMap
							.get("mapreduce.app-submission.cross-platform")));// 配置使用跨平台提交任务
			conf.set("fs.defaultFS", confMap.get("fs.defaultFS"));// 指定namenode
			conf.set("mapreduce.framework.name",
					confMap.get("mapreduce.framework.name")); // 指定使用yarn框架
			conf.set("yarn.resourcemanager.address",
					confMap.get("yarn.resourcemanager.address")); // 指定resourcemanager
			conf.set("yarn.resourcemanager.scheduler.address",
					confMap.get("yarn.resourcemanager.scheduler.address"));// 指定资源分配器
			conf.set("mapreduce.jobhistory.address",
					confMap.get("mapreduce.jobhistory.address"));// 指定historyserver
		}
		return conf;
	}

	public static FileSystem getFs() {

		return fs;
	}

	/**
	 * 检查用户是否对hdfs 目录folder 有读权限 规则： 判断是否和档案拥有者同名，同名直接返回true； 不同名，直接归为其他用户
	 * 
	 * @param folder
	 * @param permission
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public static boolean checkHdfsAuth(String folder, String permission)
			throws IllegalArgumentException, IOException {
		FileStatus fileStatus = getFs().getFileStatus(new Path(folder));
		if (fileStatus.getOwner().equals(currentUser))
			return true;
		log.info("userAction:{},groupAction:{},otherAction:{}", new Object[] {
				fileStatus.getPermission().getUserAction().SYMBOL,
				fileStatus.getPermission().getGroupAction().SYMBOL,
				fileStatus.getPermission().getOtherAction().SYMBOL });
		if (fileStatus.getPermission().getOtherAction().SYMBOL
				.contains(permission))
			return true;
		return false;
	}

	private static String currentUser = System.getProperty("user.name");
}
