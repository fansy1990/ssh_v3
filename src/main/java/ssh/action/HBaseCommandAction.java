package ssh.action;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ssh.model.HBaseTable;
import ssh.model.HBaseTableData;
import ssh.model.TextValue;
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
	private Logger logger = LoggerFactory.getLogger(HBaseCommandAction.class);
	private HBaseCommandService hbaseCommandService;
	private int rows;
	private int page;

	private String tableName;
	private String cfs;
	private String startRowKey;
	
	private String timestamp;
	
	private int limit;

	private int versions;
	
	private String column;
	private String value;
	private String rowkey;

	// private Admin admin = null; // 应该放在公共的地方

	public HBaseCommandAction() {
		// 每次请求都是一个实例
		// logger.info("HBaseCommandAction==================");
	}

	// @PostConstruct
	// public void init() {
	// try {
	// if (admin == null) {
	// admin = HadoopUtils.getHBaseConnection().getAdmin();
	// }
	// logger.info("HBase admin 被初始化!");
	// } catch (IOException e) {
	// logger.info("初始化HBase admin异常!");
	// e.printStackTrace();
	// admin = null;
	// }
	// }
	//
	// @PreDestroy
	// public void destory() {
	// try {
	// admin.close();
	// } catch (IOException e) {
	// logger.info("关闭 HBase admin异常！");
	// if (admin != null)
	// admin = null;
	// }
	// }
	public void addTableData(){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		boolean flag =false;
		try {
			flag = this.hbaseCommandService.saveTableData(tableName,cfs,rowkey,column,value);
		} catch (IOException e) {//
			e.printStackTrace();
			logger.info("新增HBase 表数据异常!");
			jsonMap.put("flag", flag?"true":"false");
			jsonMap.put("msg", "请联系管理员");
			Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
			return;
		}
		jsonMap.put("flag", flag?"true":"false");
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}
	public void getTables() {
		List<HBaseTable> tables = new ArrayList<>();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			tables = this.hbaseCommandService.getTables();
		} catch (IOException e) {// 
			e.printStackTrace();
			logger.info("获取HBase 表异常!");
			jsonMap.put("total", 0);
			jsonMap.put("rows", tables);
			Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
			return;
		}
		jsonMap.put("total", tables.size());
		jsonMap.put("rows", Utils.getProperFiles(tables, page, rows));
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}

	public void getTableStartRowKey() {
		String rowkey = null;
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			rowkey = this.hbaseCommandService.getTableRowKey(tableName);
		} catch (IOException e) {// @TODO 前台如何处理
			e.printStackTrace();
			logger.info("获取HBase 表rowkey异常!");
			jsonMap.put("flag", "false");
			jsonMap.put("data", "-1");
			Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
			return;
		}
		jsonMap.put("flag", "true");
		jsonMap.put("data", rowkey);
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}

	public void getTablesJson() {
		List<TextValue> tables = new ArrayList<>();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			tables = this.hbaseCommandService.getTablesString();
		} catch (IOException e) {// @TODO 前台如何处理
			e.printStackTrace();
			logger.info("获取HBase 表异常!");
			jsonMap.put("flag", "false");
			jsonMap.put("data", null);
			Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
			return;
		}
		jsonMap.put("flag", "true");
		jsonMap.put("data", tables);
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
	}

	public void getTableData() {
		List<HBaseTableData> tableDatas = new ArrayList<>();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if (tableName == null || cfs == null || "".equals(tableName)
				|| "".equals(cfs)) {
			logger.info("HBase 表名或列簇没有设置，获取数据异常!");
			jsonMap.put("total", 0);
			jsonMap.put("rows", tableDatas);
			Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
			return;
		}
		try {
			tableDatas = this.hbaseCommandService.getTableData(tableName, cfs,
					startRowKey, limit, versions);
		} catch (IOException e) {// @TODO 前台如何处理
			e.printStackTrace();
			logger.info("获取HBase 表数据异常!");
			jsonMap.put("total", 0);
			jsonMap.put("rows", null);
			Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
			return;
		}
		jsonMap.put("total", tableDatas.size());
		jsonMap.put("rows", Utils.getProperFiles(tableDatas, page, rows));
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}

	public void getTableColumnFamilyJson() {
		List<TextValue> tables = new ArrayList<>();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			tables = this.hbaseCommandService.getTablesColumnFamily(tableName);
		} catch (IOException e) {// @TODO 前台如何处理
			e.printStackTrace();
			logger.info("获取HBase 表异常!");
			jsonMap.put("flag", "false");
			jsonMap.put("data", null);
			Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
			return;
		}
		jsonMap.put("flag", "true");
		jsonMap.put("data", tables);
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
	}

	public void getTableDetails() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		String details;
		try {
			details = this.hbaseCommandService.getTableDetails(tableName);
			jsonMap.put("flag", "true");
			jsonMap.put("content", details);
		} catch (IOException e) {// @TODO 前台如何处理
			e.printStackTrace();
			logger.info("获取HBase 表详细异常!");
			jsonMap.put("flag", "false");
		}
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}

	public void deleteTable() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			boolean flag = this.hbaseCommandService.deleteTable(tableName);
			jsonMap.put("flag", String.valueOf(flag));
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("删除HBase 表异常!");
			jsonMap.put("flag", "false");
		}
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}
	
	public void deleteTableData() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			String[] cfsArr = cfs.split(Utils.COLON, -1);
			boolean flag = this.hbaseCommandService.deleteTableData(tableName,
					cfsArr[0],cfsArr[1],rowkey,value,Utils.dateStringtoLong(timestamp));
			jsonMap.put("flag", String.valueOf(flag));
		} catch (IOException|ParseException e) {
			e.printStackTrace();
			logger.info("删除HBase 表数据异常!");
			jsonMap.put("flag", "false");
		} 
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}

	public void checkTableExists() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			boolean flag = this.hbaseCommandService.checkTableExists(tableName);
			jsonMap.put("flag", String.valueOf(flag));
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("检测HBase 表是否存在异常!");
			jsonMap.put("flag", "false");
		}
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}

	public void saveTable() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			boolean flag = this.hbaseCommandService.saveTable(tableName, cfs);
			jsonMap.put("flag", String.valueOf(flag));
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("新建HBase 表异常!");
			jsonMap.put("flag", "false");
		}
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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCfs() {
		return cfs;
	}

	public void setCfs(String cfs) {
		this.cfs = cfs;
	}

	public String getStartRowKey() {
		return startRowKey;
	}

	public void setStartRowKey(String startRowKey) {
		this.startRowKey = startRowKey;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getVersions() {
		return versions;
	}

	public void setVersions(int versions) {
		this.versions = versions;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRowkey() {
		return rowkey;
	}

	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
