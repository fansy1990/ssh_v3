package ssh.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ssh.service.HBaseCommandService;
import ssh.util.Utils;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionSupport;

@Resource(name = "identifyRMBAction")
public class IdentifyRMBAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String stumbers;
	private HBaseCommandService hbaseCommandService;
	private Logger log = LoggerFactory.getLogger(IdentifyRMBAction.class);

	public void checkStumberExistOrNot() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		try {
			Map<String, String> existMap = this.hbaseCommandService
					.checkStumbersExist(stumbers);
			jsonMap.put("exist", existMap.get("exist"));
			jsonMap.put("notExist", existMap.get("notExist"));
		} catch (IOException e) {//
			e.printStackTrace();
			log.info("查询 " + new String(Utils.IDENTIFY_RMB_RECORDS) + "表数据异常!");
			jsonMap.put("flag", "false");
			jsonMap.put("msg", "请联系管理员");
			Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
			return;
		}
		jsonMap.put("flag", "true");
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}

	public String getStumbers() {
		return stumbers;
	}

	public void setStumbers(String stumbers) {
		this.stumbers = stumbers;
	}

	public HBaseCommandService getHbaseCommandService() {
		return hbaseCommandService;
	}

	@Resource
	public void setHbaseCommandService(HBaseCommandService hbaseCommandService) {
		this.hbaseCommandService = hbaseCommandService;
	}
}
