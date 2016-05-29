package ssh.action;

import java.io.IOException;

import javax.annotation.Resource;

import ssh.util.Utils;

import com.opensymphony.xwork2.ActionSupport;

@Resource(name = "hadoopAction")
public class HadoopAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String jobId;

	public void getMRProgress() throws IOException, InterruptedException {
		System.out.println("jobId:" + jobId);
		Utils.justfortest++;
		Utils.write2PrintWriter5(String.valueOf(Utils.justfortest * 10));

		if (Utils.justfortest >= 10) {
			Utils.justfortest = 0;
		}

	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

}
