package ssh.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueExcludeFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ssh.model.HBaseTable;
import ssh.model.HBaseTableData;
import ssh.model.TextValue;
import ssh.util.HadoopUtils;
import ssh.util.Utils;

@Service
public class HBaseCommandService {

	private Logger log = LoggerFactory.getLogger(HBaseCommandService.class);

	/**
	 * 检查给定的冠字号是否存在疑似伪钞冠字号
	 * 
	 * @param stumbers
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public Map<String, String> checkStumbersExist(String stumbers)
			throws IllegalArgumentException, IOException {
		String[] stumbersArr = StringUtils.split(stumbers, Utils.COMMA);
		Connection connection = HadoopUtils.getHBaseConnection();
		Table table = connection.getTable(TableName
				.valueOf(Utils.IDENTIFY_RMB_RECORDS));
		Map<String, String> map = new HashMap<>();
		Get get = null;
		try {
			List<Get> gets = new ArrayList<>();
			for (String stumber : stumbersArr) {
				get = new Get(stumber.trim().getBytes());
				gets.add(get);
			}
			Result[] results = table.get(gets);
			String exist;
			StringBuffer existStr = new StringBuffer();
			StringBuffer notExistStr = new StringBuffer();
			for (int i = 0; i < results.length; i++) {
				exist = new String(results[i].getValue(Utils.FAMILY,
						Utils.COL_EXIST));
				if ("1".equals(exist)) {
					existStr.append(stumbersArr[i]).append(Utils.COMMA);
				} else if ("0".equals(exist)) {
					notExistStr.append(stumbersArr[i]).append(Utils.COMMA);
				} else {
					log.info("冠字号：" + stumbersArr[i] + "值 exist字段值异常！");
				}
			}
			if (existStr.length() > 0) {
				map.put("exist", existStr.substring(0, existStr.length() - 1));
			} else {
				map.put("exist", "nodata");
			}
			if (notExistStr.length() > 0) {
				map.put("notExist",
						notExistStr.substring(0, notExistStr.length() - 1));
			} else {
				map.put("notExist", "nodata");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public boolean checkTableExists(String tableName) throws IOException {
		Admin admin = HadoopUtils.getHBaseConnection().getAdmin();
		TableName[] tables = admin.listTableNames();
		for (TableName t : tables) {
			if (t.getNameAsString().equals(tableName)) {
				return true;
			}
		}
		return false;
	}

	public Map<String, Object> checkTableExistsAndFamily(String tableName,
			String colDescription) throws IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Admin admin = HadoopUtils.getHBaseConnection().getAdmin();
		TableName[] tables = admin.listTableNames();
		boolean flag = false;
		for (TableName t : tables) {
			if (t.getNameAsString().equals(tableName)) {
				flag = true;
			}
		}
		if (!flag) {// 表不存在
			jsonMap.put("flag", "false");
			jsonMap.put("msg", "表不存在，请重新输入!");
			return jsonMap;
		}
		// 检查列描述
		HTableDescriptor tableDescriptor = admin
				.getTableDescriptor(getTableName(tableName));
		HColumnDescriptor[] columnDescriptors = tableDescriptor
				.getColumnFamilies();
		List<String> familyList = new ArrayList<>();
		for (HColumnDescriptor t : columnDescriptors) {
			familyList.add(t.getNameAsString());
		}
		String[] cfs = colDescription.split(Utils.COMMA, -1);
		// flag = true ;
		String family = null;
		for (String cf : cfs) {
			if (cf.contains(Utils.COLON)) {// 包含：，则说明非rk或ts，则是列簇，需要判断
				family = cf.split(Utils.COLON, -1)[0];
				if (!familyList.contains(family)) {// 不包含，则改列簇描述有问题
					jsonMap.put("msg", "列簇描述：" + family + "在表中没有此列簇！");
					jsonMap.put("flag", "flag");
					return jsonMap;
				}
			}
		}
		jsonMap.put("flag", "true");
		return jsonMap;
	}

	public boolean deleteTable(String tableName) throws IOException {
		Admin admin = HadoopUtils.getHBaseConnection().getAdmin();
		admin.disableTable(getTableName(tableName));
		admin.deleteTable(getTableName(tableName));
		return true;
	}

	public boolean deleteTableData(String tableName, String family,
			String qualifier, String rowkey, String value, long timestamp)
			throws IOException {
		Table table = HadoopUtils.getHBaseConnection().getTable(
				getTableName(tableName));
		Delete delete = new Delete(Bytes.toBytes(rowkey));
		delete.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier),
				timestamp);
		boolean flag = table.checkAndDelete(Bytes.toBytes(rowkey),
				Bytes.toBytes(family), Bytes.toBytes(qualifier),
				Bytes.toBytes(value), delete);

		table.close();

		return flag;
	}

	/**
	 * 根据row获取特定put对象
	 * 
	 * @param row
	 * @return
	 */
	private Put generatePutFromRow(byte[] row, String exist,String uId,String bank) {
		Put put = null;
		try {
			put = new Put(row);
			put.addColumn(Utils.FAMILY, Utils.COL_EXIST, Bytes.toBytes(exist));
			put.addColumn(Utils.FAMILY, Utils.COL_UID, Bytes.toBytes(uId));
			put.addColumn(Utils.FAMILY, Utils.COL_BANK, Bytes.toBytes(bank));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return put;
	}
	
	private Put generatePutFromRow(byte[] row, String exist) {
		Put put = null;
		try {
			put = new Put(row);
			put.addColumn(Utils.FAMILY, Utils.COL_EXIST, Bytes.toBytes(exist));
//			put.addColumn(Utils.FAMILY, Utils.COL_UID, Bytes.toBytes(uId));
//			put.addColumn(Utils.FAMILY, Utils.COL_BANK, Bytes.toBytes(bank));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return put;
	}

	private List<HBaseTableData> getFromCells(Cell[] rawCells) {
		List<HBaseTableData> list = new ArrayList<>();
		for (Cell cell : rawCells) {
			list.add(new HBaseTableData(cell));
		}
		return list;
	}

	private List<HBaseTableData> getHBaseTableDataListFromCells(Cell[] cells) {
		List<HBaseTableData> list = new ArrayList<>();
		for (Cell cell : cells) {
			list.add(new HBaseTableData(cell));
		}
		return list;
	}

	public List<HBaseTableData> getTableData(String tableName, String cfs,
			String startRowKey, int limit, int versions) throws IOException {
		List<HBaseTableData> datas = new ArrayList<>();
		Table table = HadoopUtils.getHBaseConnection().getTable(
				getTableName(tableName));
		Scan scan = new Scan();
		scan.setMaxVersions(versions);
		if (startRowKey != "-1") {
			scan.setStartRow(startRowKey.getBytes());
		}
		String[] cfsArr = cfs.split(Utils.COMMA, -1);
		for (String cf : cfsArr) {
			scan.addFamily(cf.getBytes());
		}

		ResultScanner scanner = table.getScanner(scan);

		Result[] rows = scanner.next(limit);

		for (Result row : rows) {
			// Cell[] cells = row.rawCells();

			datas.addAll(getFromCells(row.rawCells()));
		}

		scanner.close();
		table.close();
		return datas;
	}

	/**
	 * 获取指定表详细信息
	 * 
	 * @param tableName
	 * @return
	 * @throws IOException
	 */
	public String getTableDetails(String tableName) throws IOException {
		Admin admin = HadoopUtils.getHBaseConnection().getAdmin();
		HTableDescriptor tableDescriptors = admin
				.getTableDescriptor(getTableName(tableName));
		log.info(tableDescriptors.toStringCustomizedValues());
		log.info(tableDescriptors.toString());
		return admin.getTableDescriptor(getTableName(tableName)).toString();
	}

	private TableName getTableName(String tableName) {

		return TableName.valueOf(tableName);
	}

	public String getTableRowKey(String tableName) throws IOException {
		Table table = HadoopUtils.getHBaseConnection().getTable(
				getTableName(tableName));
		Scan scan = new Scan();
		ResultScanner scanner = table.getScanner(scan);
		Result firstRow = scanner.next();
		scanner.close();
		table.close();
		if (firstRow == null)
			return "-1";

		return new String(firstRow.getRow());
	}

	/**
	 * 获取所有表
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<HBaseTable> getTables() throws IOException {
		List<HBaseTable> list = new ArrayList<>();
		Admin admin = HadoopUtils.getHBaseConnection().getAdmin();
		TableName[] tables = admin.listTableNames();
		HBaseTable hTable = null;

		for (TableName t : tables) {
			hTable = new HBaseTable();
			hTable.setNameSpace(t.getNamespaceAsString());
			hTable.setTableName(t.getNameAsString());
			// HTableDescriptor htableDes = admin.getTableDescriptor(t);
			// log.info(htableDes.toString());
			// log.info(htableDes.toStringTableAttributes());
			// log.info(htableDes.getFamilies().toString());
			// log.info(htableDes.toStringCustomizedValues());
			hTable.setDescription(admin.getTableDescriptor(t)
					.toStringCustomizedValues());
			setRegions(hTable, admin.getTableRegions(t));
			list.add(hTable);
		}

		return list;
	}

	public List<TextValue> getTablesColumnFamily(String tableName)
			throws IOException {
		List<TextValue> list = new ArrayList<>();
		Admin admin = HadoopUtils.getHBaseConnection().getAdmin();
		HTableDescriptor tableDescriptor = admin
				.getTableDescriptor(getTableName(tableName));
		HColumnDescriptor[] columnDescriptors = tableDescriptor
				.getColumnFamilies();
		for (HColumnDescriptor t : columnDescriptors) {
			list.add(new TextValue(t.getNameAsString()));
		}
		return list;
	}

	public List<TextValue> getTablesString() throws IOException {
		List<TextValue> list = new ArrayList<>();
		Admin admin = HadoopUtils.getHBaseConnection().getAdmin();
		TableName[] tables = admin.listTableNames();
		for (TableName t : tables) {
			list.add(new TextValue(t.getNameAsString()));
		}
		return list;
	}

	/**
	 * 根据rowkey和版本个数查询数据
	 * 
	 * @param stumbers
	 * @param num
	 * @return
	 * @throws IOException
	 */
	public List<HBaseTableData> read(String stumbers, int num)
			throws IOException {
		String[] stumbersArr = StringUtils.split(stumbers, Utils.COMMA);
		Connection connection = HadoopUtils.getHBaseConnection();
		Table table = connection.getTable(TableName
				.valueOf(Utils.IDENTIFY_RMB_RECORDS));
		List<HBaseTableData> list = new ArrayList<>();
		Get get = null;
		try {
			List<Get> gets = new ArrayList<>();
			for (String stumber : stumbersArr) {
				get = new Get(stumber.trim().getBytes());
				get.setMaxVersions(num);
				gets.add(get);
			}
			Result[] results = table.get(gets);
			Cell[] cells;
			for (int i = 0; i < results.length; i++) {
				cells = results[i].rawCells();

				list.addAll(getHBaseTableDataListFromCells(cells));

			}

			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 取钱，随机输出冠字号，并更新冠字号对应的exist字段值； 只有在exist为true时，才进行上面的操作
	 * 
	 * @param numStr
	 * @throws IOException
	 */
	public List<String> retrieve(String numStr,String uId,String bank) throws IOException {
		Connection connection = HadoopUtils.getHBaseConnection();
		Table table = connection.getTable(TableName
				.valueOf(Utils.IDENTIFY_RMB_RECORDS));
		List<String> list = new ArrayList<>();
		int num = Integer.parseInt(numStr);
		try {

			Scan scan = new Scan();
			scan.setStartRow(Utils.getRandomRecordsRowKey().getBytes());
			// 设置只查询exist为1的数据（不使用SingleColumnValueFilter,为什么？）
			Filter filter = new SingleColumnValueExcludeFilter(Utils.FAMILY,
					Utils.COL_EXIST, CompareOp.EQUAL, Bytes.toBytes("1"));
			scan.setFilter(filter);

			ResultScanner resultScanner = table.getScanner(scan);
			Result[] results = resultScanner.next(num * 3);// 取出的记录数是num的3倍(效率高)，因为数据可能被其他值更新
			Put put = null;
			for (int i = 0; i < results.length; i++) {
				put = generatePutFromRow(results[i].getRow(), "0",uId,bank);
				if (table.checkAndPut(results[i].getRow(), Utils.FAMILY,
						Utils.COL_EXIST, Bytes.toBytes("1"), put)) {
					list.add(new String(results[i].getRow()));
				}
				if (list.size() >= num) {// 如果已经找到所有数据，则返回
					break;
				}
			}
			byte[] row;
			while (list.size() < num) {// 没有没有找到所有数据，则接着直接查找
				row = resultScanner.next().getRow();
				put = generatePutFromRow(row, "0",uId,bank);
				if (table.checkAndPut(row, Utils.FAMILY, Utils.COL_EXIST,
						Bytes.toBytes("1"), put)) {
					list.add(new String(row));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	/**
	 * 在exist为false（也就是0）时，才进行下面的操作 更新op_www:exist为true（也即是1），即进行存储操作；
	 * 否则，存储失败，并返回疑似伪钞冠字号
	 * 
	 * @param stumbers
	 * @return
	 * @throws IOException
	 */
	public Map<String, String> save(String stumbers,String uId,String bank) throws IOException {
		String[] stumbersArr = StringUtils.split(stumbers, Utils.COMMA);
		Connection connection = HadoopUtils.getHBaseConnection();
		Table table = connection.getTable(TableName
				.valueOf(Utils.IDENTIFY_RMB_RECORDS));
		Map<String, String> map = new HashMap<>();
		StringBuffer saved = new StringBuffer();
		StringBuffer notSaved = new StringBuffer();
		try {
			Put put = null;
			for (int i = 0; i < stumbersArr.length; i++) {
				put = generatePutFromRow(stumbersArr[i].trim().getBytes(), 
						"1",uId,bank);
				if (table.checkAndPut(stumbersArr[i].trim().getBytes(),
						Utils.FAMILY, Utils.COL_EXIST, Bytes.toBytes("0"), put)) {
					saved.append((stumbersArr[i].trim())).append(",");
				} else {// 数据库中已存在冠字号，且op_www:exist为0，所以插入会有问题（伪钞）
					notSaved.append((stumbersArr[i].trim())).append(",");
				}
			}
			if (saved.length() > 0) {
				map.put("saved", saved.substring(0, saved.length() - 1));
			} else {
				map.put("saved", "nodata");
			}
			if (notSaved.length() > 0) {
				map.put("notSaved",
						notSaved.substring(0, notSaved.length() - 1));
			} else {
				map.put("notSaved", "nodata");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public boolean saveTable(String tableName, String cfs) throws IOException {
		Admin admin = HadoopUtils.getHBaseConnection().getAdmin();
		HTableDescriptor hTableDescriptor = new HTableDescriptor(
				getTableName(tableName));
		String[] cfsArr = StringUtils.split(cfs, Utils.COMMA);
		for (String cf : cfsArr) {
			hTableDescriptor.addFamily(new HColumnDescriptor(cf));
		}
		admin.createTable(hTableDescriptor);
		return true;
	}

	public boolean saveTableData(String tableName, String cfs, String rowkey,
			String column, String value) throws IOException {
		Table table = HadoopUtils.getHBaseConnection().getTable(
				getTableName(tableName));
		Put put = new Put(Bytes.toBytes(rowkey));
		put.addColumn(Bytes.toBytes(cfs), Bytes.toBytes(column),
				Bytes.toBytes(value));

		table.put(put);
		table.close();
		return true;
	}

	private void setRegions(HBaseTable hTable, List<HRegionInfo> tableRegions) {
		int online = 0;
		int offline = 0;
		int split = 0;
		for (HRegionInfo hRegionInfo : tableRegions) {
			if (hRegionInfo.isOffline()) {
				offline++;
			} else {
				online++;
			}
			if (hRegionInfo.isSplit())
				split++;
		}
		hTable.setOfflineRegions(offline);
		hTable.setOnlineRegions(online);
		hTable.setSplitRegions(split);
	}

	public boolean updateTableData(String tableName, String cfs, String rowkey,
			String column, String value, long timestamp, String oldValue)
			throws IOException {
		Table table = HadoopUtils.getHBaseConnection().getTable(
				getTableName(tableName));
		Put put = new Put(Bytes.toBytes(rowkey));
		put.addColumn(Bytes.toBytes(cfs), Bytes.toBytes(column), timestamp,
				Bytes.toBytes(value));

		table.checkAndPut(Bytes.toBytes(rowkey), Bytes.toBytes(cfs),
				Bytes.toBytes(column), Bytes.toBytes(oldValue), put);
		table.close();
		return true;
	}
}
