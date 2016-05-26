package ssh.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
/**
 * Job Driver驱动类
 * @author fansy
 *
 */
public class ImportToHBase  extends Configured implements Tool {
	public static final String SPLITTER ="SPLITTER";
	public static final String  COLSFAMILY ="COLSFAMILY";
	public static final String DATEFORMAT="DATEFORMAT";
	@Override
	public int run(String[] args) throws Exception {
		if(args.length!=5){
			System.err.println("Usage:\n demo.job.ImportToHBase <input> <tableName> <splitter> <rk,ts,col1:q1,col2:q1,col2:q2> <date_format>");
			return -1;
		}
		if(args[3]==null || args[3].length()<1) {
			System.err.println("column family can't be null!");
			return -1;
		}
		Configuration conf = getConf();
		conf.set(SPLITTER, args[2]);
		conf.set(COLSFAMILY, args[3]);
		conf.set(DATEFORMAT, args[4]);
		TableName tableName = TableName.valueOf(args[1]);
        Path inputDir = new Path(args[0]);
        String jobName = "Import to "+ tableName.getNameAsString();
        Job job = Job.getInstance(conf, jobName);
        job.setJarByClass(ImportMapper.class);
        FileInputFormat.setInputPaths(job, inputDir);
        job.setInputFormatClass(TextInputFormat.class);
        job.setMapperClass(ImportMapper.class);
        
        TableMapReduceUtil.initTableReducerJob(tableName.getNameAsString(), null,job);
        job.setNumReduceTasks(0);
        return job.waitForCompletion(true) ? 0 : 1;
	}

}
