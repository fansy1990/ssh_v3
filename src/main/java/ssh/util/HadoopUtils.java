package ssh.util;

import java.io.BufferedReader;
import java.io.File;
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

import ssh.eum.MRInfo;
import ssh.eum.MRLock;

public class HadoopUtils {
	public static final Logger log = LoggerFactory.getLogger(HadoopUtils.class);

	private static Configuration conf;
	private static Map<String, String> confMap = new HashMap<>();
	public static final String hadoop_properties = "hadoop.properties";
	private static FileSystem fs;

	private static String hadoopUserName = "root";// 初始值，root字符串
	private static String hadoopPassword = "null";

	private static MRLock mrLock = MRLock.NOTLOCKED;
	private static Map<MRInfo, String> mrInfo = new HashMap<>();

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

	// private static Admin admin = null;
	private static Configuration hbaseConfiguration = null;
	private static Connection hbaseConnection = null;

	/**
	 * Pooling or caching of the returned Admin is not recommended. 不使用
	 * 
	 * @return
	 */
	// public static Admin getHBaseAdmin() {
	// try {
	// if (admin == null) {
	// admin = HadoopUtils.getHBaseConnection().getAdmin();
	// }
	// log.info("HBase admin 被初始化!");
	// } catch (IOException e) {
	// log.info("初始化HBase admin异常!");
	// e.printStackTrace();
	// admin = null;
	// }
	// return admin;
	// }

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
			conf.set("mapreduce.job.jar", JarUtil.jar(JarUtil.class));// 设置jar包路径
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

	public void cleanUp() {
		try {
			new File(JarUtil.getJarFilePath()).delete();
		} catch (Exception e) {
			log.info("删除" + JarUtil.getJarFilePath() + "异常!");
			e.printStackTrace();
			return;
		}
		log.info("jar 文件" + JarUtil.getJarFilePath() + "被删除！");
	}

	public static MRLock getMrLock() {
		return mrLock;
	}

	/**
	 * 设置同步方法
	 * 
	 * @param mrLock
	 */
	public static synchronized void setMrLock(MRLock mrLock) {
		HadoopUtils.mrLock = mrLock;
	}

	private static String jobId;

	public static void getAndSetJobId() {
		// 通过获取hadoop集群最近jobId，并生成新jobId;

		String lastJobId = "job_1464309897118_0001";

		setJobId(lastJobId);// TODO 这里还需要经过处理
	}

	public static String getJobId() {
		return jobId;
	}

	public static void setJobId(String jobId) {
		HadoopUtils.jobId = jobId;
	}

	public static Map<MRInfo, String> getMrInfo() {
		return mrInfo;
	}

	/**
	 * 在修改的时候加上同步
	 * 
	 * @param mrError
	 */
	public static synchronized void addMrError(MRInfo key, String mrError) {
		HadoopUtils.mrInfo.put(key, mrError);
	}

	/**
	 * 清空
	 */
	public static synchronized void clearMrInfo() {
		HadoopUtils.mrInfo.clear();
	}

	/**
	 * MR任务缓存包括 jobId 和 mrInfo
	 */
	public static void initMRCache() {
		HadoopUtils.jobId = null;
		clearMrInfo();
	}

	/**
	 * 根据JobID 获得进度
	 * 
	 * @param jobId
	 * @return
	 */
	public static String getMRProgress(String jobId) {
		return null;
	}
}
