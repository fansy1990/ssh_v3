package ssh.util;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FileStatus;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ssh.model.HdfsResponseProperties;
import ch.ethz.ssh2.Connection;

public class Utils {
	// public static int justfortest = 0;
	private static final Logger log = LoggerFactory.getLogger(Utils.class);
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String COMMA = ",";
	public static final String UNDERLINE = "_";
	private static PrintWriter writer = null;

	public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss SSS");
	public static final DecimalFormat twoPlaceDecimal = new DecimalFormat(
			"#0.00");
	public static final String COLON = ":";
	public static final byte[] IDENTIFY_RMB_RECORDS = "records".getBytes();
	public static final byte[] FAMILY = "info".getBytes();
	public static final byte[] COL_EXIST = "exist".getBytes();
	public static final byte[] COL_UID = "uid".getBytes();
	public static final byte[] COL_BANK = "bank".getBytes();

	public static String dateLongtoString(long time) {
		return SIMPLE_DATE_FORMAT.format(new Date(time));
	}

	public static long dateStringtoLong(String dateString)
			throws ParseException {
		return SIMPLE_DATE_FORMAT.parse(dateString).getTime();
	}

	/**
	 * HDFS文件格式转换为目标格式
	 * 
	 * @param file
	 * @return
	 */
	public static HdfsResponseProperties getDataFromLocatedFileStatus(
			FileStatus file) {
		HdfsResponseProperties properties = new HdfsResponseProperties();
		properties.setName(file.getPath().getName());
		properties.setBlockSize(convertFileSize(file.getBlockSize()));
		properties.setGroup(file.getGroup());
		properties.setModificationTime(convert2String(
				file.getModificationTime(), null));
		properties.setOwner(file.getOwner());
		properties.setPermission(file.getPermission().toString());
		properties.setReplication(file.getReplication() > 0 ? String
				.valueOf(file.getReplication()) : "");
		properties.setSize(convertFileSize(file.getLen()));
		properties.setType(file.isDirectory() ? "dir" : "file");
		return properties;

	}

	public static <T> List<T> getProperFiles(List<T> files, int page, int rows) {

		return files.subList((page - 1) * rows,
				page * rows > files.size() ? files.size() : page * rows);
	}

	/**
	 * 把byte转换为字符串，包含Mb，GB等
	 * 
	 * @param size
	 * @return
	 */
	private static String convertFileSize(long size) {
		if (size == 0)
			return "";
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;

		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else
			return String.format("%d B", size);
	}

	/**
	 * 向PrintWriter中输入数据
	 * 
	 * @param info
	 */
	public static void write2PrintWriter(String info) {
		log.info("json:{}", info);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			writer = ServletActionContext.getResponse().getWriter();

			writer.write(info);// 响应输出
			// 释放资源，关闭流
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * HTML5 支持 服务器发送事件
	 * 
	 * @param info
	 */
	public static void write2PrintWriter5(String info) {
		log.info("data:{}", info);
		OutputStream outputStream = null;
		try {

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/event-stream");
			outputStream = new BufferedOutputStream(response.getOutputStream());

			outputStream.write(("data:" + info + "\n\n").getBytes());// 响应输出
			// 释放资源，关闭流
			outputStream.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将长整型数字转换为日期格式的字符串
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static String convert2String(long time, String format) {
		if (time > 0l) {
			if (StringUtils.isBlank(format))
				format = TIME_FORMAT;
			SimpleDateFormat sf = new SimpleDateFormat(format);
			Date date = new Date(time);
			return sf.format(date);
		}
		return "";
	}

	/**
	 * ssh远程是否可登录
	 * 
	 * @param hdfsUserName
	 * @param hdfsPassword
	 * @return
	 */
	public static boolean canLogin(String hdfsUserName, String hdfsPassword) {
		// hdfsPassword 解密
		// TODO 未完成
		// ssh远程检查用户名和密码

		String hostname = HadoopUtils.getPropertyValue("hdfs.ssh.client.host");
		int port = Integer.parseInt(HadoopUtils
				.getPropertyValue("hdfs.ssh.client.port"));
		try {
			Connection conn = new Connection(hostname, port);
			conn.connect();

			boolean isAuthenticated = conn.authenticateWithPassword(
					hdfsUserName, hdfsPassword);
			return isAuthenticated;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	private static Random random = new Random();

	public static String getRandomRecordsRowKey() {
		int rp = random.nextInt(crownSizePrefixes.length);
		int rs = random.nextInt(9999);
		return crownSizePrefixes[rp] + formatCrownSizeSuffix(rs);
	}

	public static String[] crownSizePrefixes = null;

	static {
		crownSizePrefixes = new String[26 * 2];
		for (int i = 0; i < crownSizePrefixes.length / 2; i++) {
			crownSizePrefixes[i] = "AAA" + (char) (65 + i);
			crownSizePrefixes[i + 26] = "AAB" + (char) (65 + i);
		}
	}
	private static DecimalFormat df = new DecimalFormat("0000");

	public static String formatCrownSizeSuffix(int num) {
		return df.format(num);
	}

}
