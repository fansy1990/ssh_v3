package ssh.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.hadoop.ipc.RemoteException;
import org.apache.hadoop.security.AccessControlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ssh.model.HdfsRequestProperties;
import ssh.model.HdfsResponseProperties;
import ssh.service.HdfsService;
import ssh.util.HadoopUtils;
import ssh.util.Utils;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * HDFS 文件管理系统Action 每个方法前加入权限判断
 * 
 * @author fansy
 * 
 */
@Resource(name = "hdfsManagerAction")
public class HdfsManagerAction extends ActionSupport implements
		ModelDriven<HdfsRequestProperties> {
	private static final Logger log = LoggerFactory
			.getLogger(HdfsManagerAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HdfsRequestProperties hdfsFile = new HdfsRequestProperties();
	private HdfsService hdfsService;
	private int rows;
	private int page;

	private File file;
	private String fileFileName;

	private String fileContentType;

	@Override
	public HdfsRequestProperties getModel() {
		return hdfsFile;
	}

	/**
	 * 读取文件夹下面的文件和文件夹
	 * 
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 */
	public void listFolder() throws FileNotFoundException,
			IllegalArgumentException, IOException {
		List<HdfsResponseProperties> files = this.hdfsService
				.listFolder(hdfsFile.getFolder());
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("total", files.size());
		jsonMap.put("rows", files);
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}

	/**
	 * 检查目录权限或是否存在 权限由外部设定
	 * 
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public void checkExistAndAuth() throws IllegalArgumentException,
			IOException {
		Map<String, Object> map = new HashMap<>();
		boolean exist = this.hdfsService.checkExist(this.hdfsFile.getFolder());
		if (!exist) {
			map.put("flag", "nodir");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
		}
		// 读取并且执行权限
		boolean hasAuth = true;
		if (this.hdfsFile.getAuth() == null
				|| this.hdfsFile.getAuth().length() < 1
				|| this.hdfsFile.getAuth().length() > 3) {
			log.info("权限设置异常！authString:{}", this.hdfsFile.getAuth());
			map.put("flag", "false");
			map.put("msg", "后台错误，请联系管理员！");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
		}
		for (char a : this.hdfsFile.getAuth().toCharArray()) {
			hasAuth = hasAuth
					&& HadoopUtils.checkHdfsAuth(this.hdfsFile.getFolder(),
							String.valueOf(a));
		}
		if (!hasAuth) {
			map.put("flag", "false");
			map.put("msg", "目录操作没有权限！");
		}
		if (map.get("flag") == null) {
			map.put("flag", "true");
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}

	/**
	 * 移除文件夹
	 * 
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void deleteFolder() throws IllegalArgumentException, IOException {
		boolean flag = false;
		Map<String, Object> map = new HashMap<>();
		boolean exist = this.hdfsService.checkExist(this.hdfsFile.getFolder());
		if (!exist) {
			map.put("flag", "false");
			map.put("msg", "目录不存在！");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
		}
		// 读取并且执行权限
		boolean auth = HadoopUtils
				.checkHdfsAuth(this.hdfsFile.getFolder(), "r")
				&& HadoopUtils.checkHdfsAuth(this.hdfsFile.getFolder(), "x");
		if (!auth) {
			map.put("msg", "没有权限!");
			map.put("flag", "false");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
		}

		try {
			flag = this.hdfsService.deleteFolder(this.hdfsFile.getFolder(),
					hdfsFile.isRecursive());
		} catch (RemoteException e) {
			if (e.getClassName().equals(
					"org.apache.hadoop.fs.PathIsNotEmptyDirectoryException")) {
				map.put("msg", "目录下有子目录!");
			}
		}
		if (flag) {// 目录删除成功
			map.put("flag", "true");
		} else {// 目录删除失败
			map.put("flag", "false");
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}

	/**
	 * 检索文件夹 先检查权限
	 * 
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 */
	public void searchFolder() throws FileNotFoundException,
			IllegalArgumentException, IOException {

		List<HdfsResponseProperties> files = this.hdfsService.searchFolder(
				hdfsFile.getFolder(), hdfsFile.getName(), hdfsFile.getNameOp(),
				hdfsFile.getOwner(), hdfsFile.getOwnerOp());
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("total", files.size());
		jsonMap.put("rows", files);
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}

	/**
	 * 新建文件夹
	 * 
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void createFolder() throws IllegalArgumentException, IOException {
		Map<String, Object> map = new HashMap<>();
		boolean exist = this.hdfsService.checkExist(this.hdfsFile.getFolder());
		if (exist) {
			map.put("flag", "hasdir");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
		}
		boolean flag = false;
		try {
			flag = this.hdfsService.createFolder(this.hdfsFile.getFolder(),
					hdfsFile.isRecursive());
		} catch (AccessControlException e) {
			map.put("msg", "没有权限！");
		} catch (Exception e) {
			map.put("msg", "创建目录异常，请联系管理员！");
		}
		if (flag) {// 目录删除成功
			map.put("flag", "true");
		} else {// 目录删除失败
			map.put("flag", "false");
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}

	public void upload() {
		Map<String, Object> map = new HashMap<>();
	
		boolean flag = false;
		
		flag = this.hdfsService.upload(file.getAbsolutePath(),hdfsFile.getFolder()+"/"+fileFileName);
		
		if (flag) {// 上传成功
			map.put("flag", "true");
		} else {// 失败
			map.put("flag", "false");
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}

	public HdfsService getHdfsService() {
		return hdfsService;
	}

	@Resource
	public void setHdfsService(HdfsService hdfsService) {
		this.hdfsService = hdfsService;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
}
