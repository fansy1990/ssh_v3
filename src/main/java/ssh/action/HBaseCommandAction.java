package ssh.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import ssh.model.HBaseTable;
import ssh.service.HBaseCommandService;
import ssh.util.Utils;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionSupport;

@Resource(name = "hbaseCommandAction")
public class HBaseCommandAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HBaseCommandService hbaseCommandService;
	private int rows;
	private int page;
	
	public void getTables(){
		List<HBaseTable> files=new ArrayList<>();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			files = this.hbaseCommandService.getTables();
		} catch (IOException e) {// @TODO 前台如何处理
			e.printStackTrace();
		}
		jsonMap.put("total", files.size());
		jsonMap.put("rows", Utils.getProperFiles(files,page,rows));
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}

	public HBaseCommandService getHbaseCommandService() {
		return hbaseCommandService;
	}

	@Resource
	public void setHbaseCommandService(HBaseCommandService hbaseCommandService) {
		this.hbaseCommandService = hbaseCommandService;
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
