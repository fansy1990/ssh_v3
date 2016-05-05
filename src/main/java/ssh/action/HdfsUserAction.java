package ssh.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;

import ssh.model.HdfsUser;
import ssh.service.HdfsUserService;
import ssh.util.HadoopUtils;
import ssh.util.Utils;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * Hdfs 模块用户登录
 * 
 * @author fansy
 * 
 */
@Resource(name = "hdfsUserAction")
public class HdfsUserAction extends ActionSupport implements
		ModelDriven<HdfsUser> {
	HdfsUser hdfsUser = new HdfsUser();
	private HdfsUserService hdfsUserService;
	
	private String hadoopUserName;
	private String hadoopPassword; 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public HdfsUser getModel() {
		return hdfsUser;
	}

	/**
	 * 登录
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void login() throws ServletException, IOException {
		Map<String, Object> map = new HashMap<>();
		HdfsUser hUser = hdfsUserService.getByEmail(hdfsUser.getEmail());
		if (hUser == null) {
			map.put("flag", "false");
			map.put("msg", "登录失败，用户名不存在!");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
		}
		if (!hdfsUser.getPassword().equals(hUser.getPassword())) {
			map.put("flag", "false");
			map.put("msg", "登录失败,用户密码不正确!");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;

		} else {
			map.put("flag", "true");
			map.put("msg", "登录成功!");
			ActionContext context = ActionContext.getContext();
			Map session = context.getSession();
			session.put("user", hUser.getName());
			session.put("email", hUser.getEmail());// 用于更新
			session.put("hUser", HadoopUtils.getHadoopUserName());// 用于更新
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;

	}

	/**
	 * 权限验证
	 */
	public void authCheck(){
		Map<String, Object> map = new HashMap<>();
		// 进行ssh权限验证
		boolean hasHdfsLoginAuth = Utils.canLogin(hadoopUserName,hadoopPassword);
		ActionContext context = ActionContext.getContext();
		Map session = context.getSession();
		if (!hasHdfsLoginAuth) {
			map.put("flag", "false");
			map.put("msg", "HDFS用户名或密码错误！");
			session.put("authCheck", "false");// 用于验证 ,更新数据库时
			
		}else{
			map.put("flag", "true");
			session.put("authCheck", "true");
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}
	
	/**
	 * 更新hdfsuser表数据
	 */
	public void authUpdate() {
		Map<String, Object> map = new HashMap<>();

		// 获得session中的email
		ActionContext context = ActionContext.getContext();
		Map session = context.getSession();
		if(session.get("authCheck")==null || !"true".equals(session.get("authCheck"))){
			map.put("flag", "false");
			map.put("msg", "权限验证没用通过！");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
			
		}
		String email = (String) session.get("email");
		// 更新常量值
		HadoopUtils.updateHadoopUserNamePassword(hadoopUserName, hadoopPassword);
	
		map.put("flag", "true");
		map.put("msg", "更新成功!");
		session.put("hUser", hadoopUserName);// 用于更新
		
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;

	}

	public HdfsUserService getHdfsUserService() {
		return hdfsUserService;
	}

	@Resource
	public void setHdfsUserService(HdfsUserService hdfsUserService) {
		this.hdfsUserService = hdfsUserService;
	}

	public String getHadoopUserName() {
		return hadoopUserName;
	}

	public void setHadoopUserName(String hadoopUserName) {
		this.hadoopUserName = hadoopUserName;
	}

	public String getHadoopPassword() {
		return hadoopPassword;
	}

	public void setHadoopPassword(String hadoopPassword) {
		this.hadoopPassword = hadoopPassword;
	}

}
