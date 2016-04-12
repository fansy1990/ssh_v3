package ssh.util;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Test;

public class HadoopUtilsTest {
	
	/**
	 * org.apache.hadoop.security.AccessControlException: Permission denied:
	 * this error is ok, just use : hadoop fs -chmod -R 777 /
	 * 
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	@Test
	public void testGetConf() throws FileNotFoundException, IllegalArgumentException, IOException{
		Configuration conf = HadoopUtils.getConf();
		
		if(conf!=null){
			FileSystem fs = FileSystem.get(conf);
			
			RemoteIterator<LocatedFileStatus> files =fs.listFiles(new Path("/"), true);
			
			while(files.hasNext()){
				System.out.println(files.next().getPath());
			}
		}
	}
}
