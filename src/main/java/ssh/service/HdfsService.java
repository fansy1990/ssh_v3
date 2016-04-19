package ssh.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathIsNotEmptyDirectoryException;
import org.apache.hadoop.ipc.RemoteException;
import org.apache.hadoop.security.AccessControlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ssh.model.HdfsResponseProperties;
import ssh.util.HadoopUtils;
import ssh.util.Utils;

@Service
public class HdfsService {
	private static final Logger log = LoggerFactory
			.getLogger(HdfsService.class);

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

	public boolean deleteFolder(String folder, boolean recursive)
			throws IllegalArgumentException, IOException {
		FileSystem fs = HadoopUtils.getFs();
		try{
			return fs.delete(new Path(folder), recursive);
		}catch(RemoteException e){
			throw e;
		}
	}

	public List<HdfsResponseProperties> searchFolder(String folder,
			String name, String nameOp, String owner, String ownerOp)
			throws FileNotFoundException, IllegalArgumentException, IOException {
		List<HdfsResponseProperties> files = new ArrayList<>();
		FileSystem fs = HadoopUtils.getFs();
		FileStatus[] filesStatus = fs.listStatus(new Path(folder));
		for (FileStatus file : filesStatus) {
			if (!checkName(file.getPath().getName(), name, nameOp))
				continue; // 名字没有检查通过，则下一个
			if (!checkName(file.getOwner(), owner, ownerOp))
				continue; // owner没有检查通过，则下一个
			files.add(Utils.getDataFromLocatedFileStatus(file));
		}

		return files;
	}

	/**
	 * 检查名字与给定名字是否符合op关系
	 * 
	 * @param fileName
	 * @param name
	 * @param op
	 * @return
	 */
	private boolean checkName(String fileName, String name, String op) {
		switch (op) {
		case "no":// 不用检查
			return true;
		case "contains":// 是否包含,包含返回true
			return fileName.contains(name) ? true : false;
		case "equals": // 是否相等,相等返回true
			return fileName.equals(name) ? true : false;
		case "noequal": // 是否不相等,不相等返回true
			return fileName.equals(name) ? false : true;

		default:
			log.info("wrong op:{}", op);
			break;
		}
		return false;
	}

	public boolean createFolder(String folder, boolean recursive)
			throws IllegalArgumentException, IOException,AccessControlException {
		FileSystem fs = HadoopUtils.getFs();
		try{
			return fs.mkdirs(new Path(folder));
		}catch(AccessControlException e){
			throw e;
		}
	}

	/**
	 * 检查目录是否存在
	 * 
	 * @param folder
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public boolean checkExist(String folder) throws IllegalArgumentException,
			IOException {
		boolean flag = HadoopUtils.getFs().exists(new Path(folder));
		if (!flag)
			log.info("文件或目录:{}不存在！", folder);
		return flag;
	}
}
