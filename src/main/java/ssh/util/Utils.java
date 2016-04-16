package ssh.util;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FileStatus;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ssh.model.HdfsResponseProperties;

public class Utils {
	private static final Logger log = LoggerFactory.getLogger(Utils.class);
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static PrintWriter writer = null;

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
		properties.setReplication(file.getReplication());
		properties.setSize(convertFileSize(file.getLen()));
		properties.setType(file.isDirectory() ? "dir" : "file");
		return properties;

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
}
