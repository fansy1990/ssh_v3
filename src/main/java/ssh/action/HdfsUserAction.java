package ssh.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import ssh.model.HdfsUser;
import ssh.service.HdfsUserService;
import ssh.util.Utils;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * Hdfs 模块用户登录
 * @author fansy
 *
 */
@Resource(name="hdfsUserAction")
public class HdfsUserAction extends ActionSupport implements
		ModelDriven<HdfsUser> {
	HdfsUser hdfsUser = new HdfsUser();
	private HdfsUserService hdfsUserService;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public HdfsUser getModel() {
		return hdfsUser;
	}
	
	public void updateHdfsUserName(){
		Map<String, Object> map = new HashMap<>();
		// 进行ssh权限验证
		boolean hasHdfsLoginAuth = Utils.canLogin(hdfsUser.getHdfsUserName(), hdfsUser.getHdfsPassword());
		
		if (!hasHdfsLoginAuth) {
			map.put("flag", "false");
			map.put("msg", "HDFS用户名密码错误！");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
		}
		
		// 获得session中的email
		ActionContext context = ActionContext.getContext(); 
		Map session = context.getSession();
		String email = (String) session.get("email");
		// 更新数据库
		boolean flag = hdfsUserService.updateByHdfsUserName(email, hdfsUser.getHdfsUserName(), hdfsUser.getHdfsPassword());
		if (!flag) {
			map.put("flag", "false");
			map.put("msg", "数据库更新失败！");
			
		}else{
			map.put("flag", "true");
			map.put("msg", "更新成功!");
		}
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

}
