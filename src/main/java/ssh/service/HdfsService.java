package ssh.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Service;

import ssh.model.HdfsResponseProperties;
import ssh.util.HadoopUtils;
import ssh.util.Utils;

@Service
public class HdfsService {

	public List<HdfsResponseProperties> listFolder(String folder)
			throws FileNotFoundException, IllegalArgumentException, IOException {
		List<HdfsResponseProperties> files = new ArrayList<>();
		FileSystem fs = HadoopUtils.getFs();
		FileStatus[] filesStatus = fs.listStatus(new Path(folder));
		// RemoteIterator<LocatedFileStatus> children = fs.listFiles(new Path(
		// folder), false);// 递归设置为flase，不需要对子文件夹再次递归
		// // 需返回文件的属性？
		// while (children.hasNext()) {
		// files.add(Utils.getDataFromLocatedFileStatus(children.next()));
		// }

		for (FileStatus file : filesStatus) {
			files.add(Utils.getDataFromLocatedFileStatus(file));
		}

		return files;
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
