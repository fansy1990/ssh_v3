package ssh.model;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;

import ssh.util.Utils;

public class HBaseTableData {

	private String rowKey;
	private String column;
	private String timestamp;
	private String value;

	public String getRowKey() {
		return rowKey;
	}

	public HBaseTableData() {
	}

	public HBaseTableData(Cell cell) {
		// this.rowKey = Bytes.toString(rowkey);
		// this.column = Bytes.toString(f) + ":" + Bytes.toString(q);
		// this.value = Bytes.toString(value);
		rowKey = new String(CellUtil.cloneRow(cell));
		column = new String(CellUtil.cloneFamily(cell)) + ":"
				+ new String(CellUtil.cloneQualifier(cell));

		value = new String(CellUtil.cloneValue(cell));
		this.setTimestamp(Utils.dateLongtoString(cell.getTimestamp()));
	}

	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
