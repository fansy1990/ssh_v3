package ssh.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HdfsRequestProperties hdfsFile = new HdfsRequestProperties();
	private HdfsService hdfsService;
	private int rows;
	private int page;

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
	 * 检查目录权限或是否存在
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
		boolean auth = HadoopUtils
				.checkHdfsAuth(this.hdfsFile.getFolder(), "r");
		if (!auth) {
			map.put("flag", "noauth");
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
	public void removeFolder() throws IllegalArgumentException, IOException {
		Map<String, Object> map = new HashMap<>();
		boolean flag = this.hdfsService.removeFolder(this.hdfsFile.getFolder(),
				hdfsFile.isRecursive());

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
		boolean flag = this.hdfsService.createFolder(this.hdfsFile.getFolder(),
				hdfsFile.isRecursive());

		if (flag) {// 目录删除成功
			map.put("flag", "true");
		} else {// 目录删除失败
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
}
