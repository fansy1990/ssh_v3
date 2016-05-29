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
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import ssh.model.HBaseTable;
import ssh.model.HBaseTableData;
import ssh.model.TextValue;
import ssh.util.HadoopUtils;
import ssh.util.Utils;

@Service
public class HBaseCommandService {

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
			// System.out.println(htableDes.toString());
			// System.out.println(htableDes.toStringTableAttributes());
			// System.out.println(htableDes.getFamilies().toString());
			// System.out.println(htableDes.toStringCustomizedValues());
			hTable.setDescription(admin.getTableDescriptor(t)
					.toStringCustomizedValues());
			setRegions(hTable, admin.getTableRegions(t));
			list.add(hTable);
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
		System.out.println(tableDescriptors.toStringCustomizedValues());
		System.out.println(tableDescriptors.toString());
		return admin.getTableDescriptor(getTableName(tableName)).toString();
	}

	private TableName getTableName(String tableName) {

		return TableName.valueOf(tableName);
	}

	public boolean deleteTable(String tableName) throws IOException {
		Admin admin = HadoopUtils.getHBaseConnection().getAdmin();
		admin.disableTable(getTableName(tableName));
		admin.deleteTable(getTableName(tableName));
		return true;
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

	private List<HBaseTableData> getFromCells(Cell[] rawCells) {
		List<HBaseTableData> list = new ArrayList<>();
		for (Cell cell : rawCells) {
			list.add(new HBaseTableData(cell));
		}
		return list;
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
}
