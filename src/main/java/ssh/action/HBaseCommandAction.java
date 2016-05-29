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
	private String cfs;
	private String column;
	private HBaseCommandService hbaseCommandService;
	private int limit;

	private Logger logger = LoggerFactory.getLogger(HBaseCommandAction.class);
	private int page;
	private String rowkey;

	private int rows;

	private String startRowKey;

	private String tableName;

	private String timestamp;
	private String value;
	private int versions;
	private String oldValue;

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
	public void addTableData() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		boolean flag = false;
		try {
			flag = this.hbaseCommandService.saveTableData(tableName, cfs,
					rowkey, column, value);
		} catch (IOException e) {//
			e.printStackTrace();
			logger.info("新增HBase 表数据异常!");
			jsonMap.put("flag", flag ? "true" : "false");
			jsonMap.put("msg", "请联系管理员");
			Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
			return;
		}
		jsonMap.put("flag", flag ? "true" : "false");
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}

	public void checkExistAndFamily() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			jsonMap = this.hbaseCommandService.checkTableExistsAndFamily(
					tableName, cfs);
		} catch (Exception e) {
			logger.info("检查HBase 表存在以及表与输入的列描述 异常!");
			jsonMap.put("flag", "false");
			jsonMap.put("msg", "请联系管理员");
		}
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}

	public void updateTableData() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		boolean flag = false;
		try {
			flag = this.hbaseCommandService.updateTableData(tableName, cfs,
					rowkey, column, value, Utils.dateStringtoLong(timestamp),
					oldValue);
		} catch (IOException | ParseException e) {//
			e.printStackTrace();
			logger.info("修改HBase 表数据异常!");
			jsonMap.put("flag", flag ? "true" : "false");
			jsonMap.put("msg", "请联系管理员");
			Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
			return;
		}
		jsonMap.put("flag", flag ? "true" : "false");
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
					cfsArr[0], cfsArr[1], rowkey, value,
					Utils.dateStringtoLong(timestamp));
			jsonMap.put("flag", String.valueOf(flag));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			logger.info("删除HBase 表数据异常!");
			jsonMap.put("flag", "false");
		}
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}

	public String getCfs() {
		return cfs;
	}

	public String getColumn() {
		return column;
	}

	public HBaseCommandService getHbaseCommandService() {
		return hbaseCommandService;
	}

	public int getLimit() {
		return limit;
	}

	public int getPage() {
		return page;
	}

	public String getRowkey() {
		return rowkey;
	}

	public int getRows() {
		return rows;
	}

	public String getStartRowKey() {
		return startRowKey;
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

	public String getTableName() {
		return tableName;
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

	public String getTimestamp() {
		return timestamp;
	}

	public String getValue() {
		return value;
	}

	public int getVersions() {
		return versions;
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

	public void setCfs(String cfs) {
		this.cfs = cfs;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	@Resource
	public void setHbaseCommandService(HBaseCommandService hbaseCommandService) {
		this.hbaseCommandService = hbaseCommandService;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setStartRowKey(String startRowKey) {
		this.startRowKey = startRowKey;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setVersions(int versions) {
		this.versions = versions;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
}
