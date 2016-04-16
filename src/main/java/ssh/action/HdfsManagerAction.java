package ssh.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mortbay.util.ajax.JSON;

import ssh.model.HdfsRequestProperties;
import ssh.model.HdfsResponseProperties;
import ssh.service.HdfsService;
import ssh.util.Utils;

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
		Utils.write2PrintWriter(JSON.toString(jsonMap));
		return;
	}

	/**
	 * 移除文件夹
	 * 
	 * @return
	 */
	public String removeFolder() {

		return null;
	}

	/**
	 * 检索文件夹
	 * 
	 * @return
	 */
	public String searchFolder() {

		return null;
	}

	/**
	 * 新建文件夹
	 * 
	 * @return
	 */
	public String createFolder() {

		return null;
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
