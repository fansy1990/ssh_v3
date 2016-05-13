package ssh.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.springframework.stereotype.Service;

import ssh.model.HBaseTable;
import ssh.util.HadoopUtils;

@Service
public class HBaseCommandService {

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

}
