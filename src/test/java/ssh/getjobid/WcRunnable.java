package ssh.getjobid;

import org.apache.hadoop.util.ToolRunner;

import ssh.WordCount;

public class WcRunnable implements Runnable {
	private String input;
	private String output;

	public WcRunnable(String in, String out) {
		input = in;
		output = out;
	}

	@Override
	public void run() {
		String[] args = new String[] { input, output };
		try {
			ToolRunner.run(TestGetJobID.getConf(), new WordCount(), args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
