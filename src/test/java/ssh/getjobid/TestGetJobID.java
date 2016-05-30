package ssh.getjobid;

import org.apache.hadoop.conf.Configuration;

public class TestGetJobID {

	public static void main(String[] args) throws Exception {

		// YarnClient client = new YarnClientImpl();
		// client.init(getConf());
		//
		// ApplicationId applicationId = client.createApplication()
		// .getApplicationSubmissionContext().getApplicationId();
		//
		// System.out.println(applicationId.appIdStrPrefix + ","
		// + applicationId.getId());

		String in = "wc_00";
		String out = "wc_02";
		new Thread(new WcRunnable(in, out)).start();

		System.out.println(StaticInfo.getJobId());
	}

	public static Configuration getConf() {
		Configuration conf = new Configuration();
		conf.setBoolean("mapreduce.app-submission.cross-platform", true);// 配置使用跨平台提交任务
		conf.set("fs.defaultFS", "hdfs://192.168.0.80:8020");// 指定namenode
		conf.set("mapreduce.framework.name", "yarn"); // 指定使用yarn框架
		conf.set("yarn.resourcemanager.address", "192.168.0.80:8032"); // 指定resourcemanager
		conf.set("yarn.resourcemanager.scheduler.address", "192.168.0.80:8030");// 指定资源分配器
		conf.set("mapreduce.jobhistory.address", "192.168.0.80:10020");// 指定historyserver

		return conf;
	}
}
