package ssh.model;

public class HBaseTable {
	private String nameSpace;
	private String tableName;
	private int onlineRegions;
	private int offlineRegions;
	private int failedRegions;
	private int splitRegions;
	private int otherRegions;
	private String description;

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getOnlineRegions() {
		return onlineRegions;
	}

	public void setOnlineRegions(int onlineRegions) {
		this.onlineRegions = onlineRegions;
	}

	public int getOfflineRegions() {
		return offlineRegions;
	}

	public void setOfflineRegions(int offlineRegions) {
		this.offlineRegions = offlineRegions;
	}

	public int getFailedRegions() {
		return failedRegions;
	}

	public void setFailedRegions(int failedRegions) {
		this.failedRegions = failedRegions;
	}

	public int getSplitRegions() {
		return splitRegions;
	}

	public void setSplitRegions(int splitRegions) {
		this.splitRegions = splitRegions;
	}

	public int getOtherRegions() {
		return otherRegions;
	}

	public void setOtherRegions(int otherRegions) {
		this.otherRegions = otherRegions;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
