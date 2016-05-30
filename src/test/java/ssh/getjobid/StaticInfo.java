package ssh.getjobid;

import java.util.Date;

import org.apache.hadoop.mapreduce.Job;

public class StaticInfo {

	private static Job job = null;

	public static Job getApplicationId() {
		return job;
	}

	public static synchronized void setApplicationId(Job applicationId) {
		StaticInfo.job = applicationId;
	}

	public static String getJobId() {
		long start = System.currentTimeMillis();
		while (noJobId()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(new Date() + ":getting job id ...");
		}
		long end = System.currentTimeMillis();

		System.out.println("获取jobId，耗时：" + (end - start) * 1.0 / 1000 + "s");
		return job.getJobID().getJtIdentifier() + job.getJobID().getId() + "-"
				+ job.getJobID().toString();
	}

	private static boolean noJobId() {
		if (job == null || job.getJobID() == null)
			return true;

		return false;
	}

}
