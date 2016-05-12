package ssh.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.ShortWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HadoopUtils {
	public static final Logger log = LoggerFactory.getLogger(HadoopUtils.class);

	private static Configuration conf;
	private static Map<String, String> confMap = new HashMap<>();
	public static final String hadoop_properties = "hadoop.properties";
	private static FileSystem fs;

	private static String hadoopUserName = "root";// 初始值，root字符串
	private static String hadoopPassword = "null";

	/**
	 * 更新 绑定的hadoop用户密码
	 * 
	 * @param username
	 * @param password
	 */
	public static void updateHadoopUserNamePassword(String username,
			String password) {
		if (username != null) {
			hadoopUserName = username;
		} else {
			hadoopUserName = "null";
		}
		if (password != null) {
			setHadoopPassword(password);
		} else {
			setHadoopPassword("null");
		}
		initFs();// 重新初始化
	}

	/**
	 * 获取hadoop.properties中的值
	 * 
	 * @param property
	 * @return
	 */
	public static String getPropertyValue(String property) {
		if (confMap == null)
			return null;
		return confMap.get(property);
	}

	/**
	 * 在更新 绑定的HDFS用户时，需要重新初始化FS
	 */
	public static void initFs() {
		try {
			conf = null;
			fs = FileSystem.get(getConf());
			log.info("Hadoop文件系统FS被初始化！");
		} catch (IOException e) {
			e.printStackTrace();
			log.info("获取Hadoop文件系统异常!");
			fs = null;
		}
	}

	private static void init() {
		System.setProperty("HADOOP_USER_NAME", getHadoopUserName());
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

	public static String getHadoopUserName() {
		return hadoopUserName;
	}

	private static Configuration hbaseConfiguration = null;
	private static Connection hbaseConnection = null;

	public static Connection getHBaseConnection() {
		if (hbaseConnection == null) {
			hbaseConfiguration = HBaseConfiguration.create();
			hbaseConfiguration.set("hbase.master", confMap.get("hbase.master"));
			hbaseConfiguration.set("hbase.rootdir",
					confMap.get("hbase.rootdir"));
			hbaseConfiguration.set("hbase.zookeeper.quorum",
					confMap.get("hbase.zookeeper.quorum"));
			hbaseConfiguration.set("hbase.zookeeper.property.clientPort",
					confMap.get("hbase.zookeeper.property.clientPort"));
			try {
				hbaseConnection = ConnectionFactory
						.createConnection(hbaseConfiguration);
			} catch (IOException e) {
				log.error("获取hbase 连接异常！");
				hbaseConnection = null;
			}
		}
		return hbaseConnection;
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
		if (fileStatus.getOwner().equals(getHadoopUserName()))
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

	/**
	 * 读取文本文件
	 * 
	 * @param fileName
	 * @param records
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public static String readText(String fileName, int records)
			throws IllegalArgumentException, IOException {
		FileSystem fs = getFs();
		FSDataInputStream inStream = fs.open(new Path(fileName));
		BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
		StringBuffer buffer = new StringBuffer();
		try {
			String line;
			line = br.readLine();
			while (line != null && records-- > 0) {
				// System.out.println(line);
				buffer.append(line).append("<br>");
				// be sure to read the next line otherwise you'll get an
				// infinite loop
				line = br.readLine();
			}
		} finally {
			// you should close out the BufferedReader
			br.close();
			inStream.close();
		}
		return buffer.toString();
	}

	/**
	 * 读取序列文件
	 * 
	 * @param fileName
	 * @param records
	 * @return
	 * @throws IOException
	 */
	public static String readSeq(String fileName, int records)
			throws IOException {
		Configuration conf = getConf();
		SequenceFile.Reader reader = null;
		StringBuffer buffer = new StringBuffer();
		try {
			reader = new SequenceFile.Reader(conf, Reader.file(new Path(
					fileName)), Reader.bufferSize(4096), Reader.start(0));
			Writable dkey = (Writable) ReflectionUtils.newInstance(
					reader.getKeyClass(), conf);
			Writable dvalue = (Writable) ReflectionUtils.newInstance(
					reader.getValueClass(), conf);
			while (reader.next(dkey, dvalue) && records-- > 0) {// 循环读取文件
				buffer.append(getValue(dkey)).append("\t")
						.append(getValue(dvalue)).append("<br>");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			IOUtils.closeStream(reader);
		}
		return buffer.toString();
	}

	/**
	 * 获得writable 的实际值
	 * 
	 * @param writable
	 * @return
	 */
	private static String getValue(Writable writable) {
		if (writable instanceof Text) {
			return writable.toString();
		}
		if (writable instanceof LongWritable) {
			return String.valueOf(((LongWritable) writable).get());
		}
		if (writable instanceof IntWritable) {
			return String.valueOf(((IntWritable) writable).get());
		}
		if (writable instanceof ShortWritable) {
			return String.valueOf(((ShortWritable) writable).get());
		}

		if (writable instanceof DoubleWritable) {
			return String.valueOf(((DoubleWritable) writable).get());
		}
		if (writable instanceof BooleanWritable) {
			return String.valueOf(((BooleanWritable) writable).get());
		}
		return null;
	}

	public static String getHadoopPassword() {
		return hadoopPassword;
	}

	public static void setHadoopPassword(String hadoopPassword) {
		HadoopUtils.hadoopPassword = hadoopPassword;
	}

	public static void setHadoopUserName(String hadoopUserName) {
		HadoopUtils.hadoopUserName = hadoopUserName;
	}
}
