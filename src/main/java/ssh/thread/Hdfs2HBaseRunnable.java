package ssh.thread;

import org.apache.hadoop.util.ToolRunner;

import ssh.eum.MRInfo;
import ssh.mr.ImportToHBase;
import ssh.util.HadoopUtils;

/**
 * 提交MR任务线程
 * 
 * @author fansy
 * 
 */
public class Hdfs2HBaseRunnable implements Runnable {

	private String hdfsFile;
	private String tableName;
	private String colDescription;
	private String splitter;
	private String dateFormat;

	public Hdfs2HBaseRunnable(String hdfsFile, String tableName,
			String colDesc, String splitter, String dateFormat) {
		this.hdfsFile = hdfsFile;
		this.tableName = tableName;
		this.colDescription = colDesc;
		this.splitter = splitter;
		this.dateFormat = dateFormat;
	}

	@Override
	public void run() {

		// 提交MR任务
		String[] args = new String[] { hdfsFile,// "/user/root/user.txt",
				tableName,// "user",
				splitter,// ",", 分隔符是否要转义？
				colDescription,// "rk,info:name,info:birthday,info:gender,info:address,info:phone,info:bank",
				dateFormat,// "yyyy-MM-dd HH:mm"
		};
		int ret = -1;
		try {
			ret = ToolRunner.run(HadoopUtils.getConf(), new ImportToHBase(),
					args);
		} catch (Exception e) {
			e.printStackTrace();
			HadoopUtils.addMrError(MRInfo.ERROR, "提交任务异常!");
			return;
		}
		HadoopUtils.addMrError(MRInfo.JOBRETURNCODE, String.valueOf(ret));
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColDescription() {
		return colDescription;
	}

	public void setColDescription(String colDescription) {
		this.colDescription = colDescription;
	}

	public String getSplitter() {
		return splitter;
	}

	public void setSplitter(String splitter) {
		this.splitter = splitter;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getHdfsFile() {
		return hdfsFile;
	}

	public void setHdfsFile(String hdfsFile) {
		this.hdfsFile = hdfsFile;
	}

}
