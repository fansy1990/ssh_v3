package ssh.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.springframework.stereotype.Service;

import ssh.model.HdfsResponseProperties;
import ssh.util.HadoopUtils;

@Service(value = "hdfsService")
public class HdfsService {

	public String listFolder(String folder) throws FileNotFoundException,
			IllegalArgumentException, IOException {
		List<HdfsResponseProperties> files = new ArrayList<>();
		FileSystem fs = HadoopUtils.getFs();
		RemoteIterator<LocatedFileStatus> children = fs.listFiles(new Path(
				folder), false);
		// 需返回文件的属性？
		while (children.hasNext()) {
			// files.add(children.)
		}
		return null;
	}

	public String removeFolder() {

		return null;
	}

	public String searchFolder() {

		return null;
	}

	public String createFolder() {

		return null;
	}
}
