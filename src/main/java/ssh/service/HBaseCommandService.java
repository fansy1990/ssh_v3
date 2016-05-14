package ssh.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.springframework.stereotype.Service;

import ssh.model.HBaseTable;
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

}
