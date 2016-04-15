package ssh.action;

import javax.annotation.Resource;

import ssh.model.HdfsRequestProperties;

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

	@Override
	public HdfsRequestProperties getModel() {
		return hdfsFile;
	}

	/**
	 * 读取文件夹下面的文件和文件夹
	 * 
	 * @return
	 */
	public String listFolder() {

		return null;
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
}
