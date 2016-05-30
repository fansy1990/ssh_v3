package ssh.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ssh.eum.MRInfo;
import ssh.eum.MRLock;
import ssh.thread.Hdfs2HBaseRunnable;
import ssh.util.HadoopUtils;
import ssh.util.Utils;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionSupport;

@Resource(name = "hadoopAction")
public class HadoopAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory
			.getLogger(HadoopAction.class);
	private String jobId;

	private String hdfsFile;
	private String tableName;
	private String colDescription;
	private String splitter;
	private String dateFormat;

	public void getMRProgress() throws IOException, InterruptedException {
		log.info("jobId:" + jobId);
		// Utils.justfortest++;
		// Utils.write2PrintWriter5(String.valueOf(Utils.justfortest * 10));
		//
		// if (Utils.justfortest >= 10) {
		// Utils.justfortest = 0;
		// }
		/**
		 * 在提交任务的时候报错
		 */
		if (HadoopUtils.getMrInfo().containsKey(MRInfo.ERROR)) {
			Utils.write2PrintWriter5("error:"
					+ HadoopUtils.getMrInfo().get(MRInfo.ERROR));
			return;
		}

		String mrProgress = HadoopUtils.getMRProgress(jobId);
		if ("success".equals(mrProgress)) {// 成功，则提示
			Utils.write2PrintWriter5("success");
			return;
		}
		// 正在运行，则返回进度即可
		Utils.write2PrintWriter5(mrProgress);
	}

	public void submitJob() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if (HadoopUtils.getMrLock().equals(MRLock.NOTLOCKED)) {// 没有锁，则可以提交代码
			// 先加锁
			HadoopUtils.setMrLock(MRLock.LOCKED);
			// 清空MR任务缓存
			HadoopUtils.initMRCache();
			// 获取JobId// 设置JobId
			HadoopUtils.getAndSetJobId();

			// 提交任务
			new Thread(new Hdfs2HBaseRunnable(hdfsFile, tableName,
					colDescription, splitter, dateFormat)).start();
			// boolean flag = true; // 在获取进度的时候检查即可
			// while (flag) {// 确认提交任务没有失败
			// try {
			// Thread.sleep(200);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// if (HadoopUtils.getMrInfo().containsKey("error")
			// || HadoopUtils.getMrInfo().containsKey("info")) {
			// flag = false;
			// }
			// }
			jsonMap.put("flag", "true");
			jsonMap.put("jobId", HadoopUtils.getJobId());
		} else {
			jsonMap.put("flag", "false");
			jsonMap.put("msg", "已经在运行MR程序，请确认！");
		}
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
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
