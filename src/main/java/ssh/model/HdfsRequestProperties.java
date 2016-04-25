package ssh.model;

/**
 * HDFS文件相关用到的属性集合
 * 
 * @author fansy
 * 
 */
public class HdfsRequestProperties {

	private String folder;
	private String name;
	private String nameOp;
	private String owner;
	private String ownerOp;
	private boolean recursive;

	private String fileName;
	private String localFile;

	private String auth;

	private int records;
	private String textSeq;

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameOp() {
		return nameOp;
	}

	public void setNameOp(String nameOp) {
		this.nameOp = nameOp;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerOp() {
		return ownerOp;
	}

	public void setOwnerOp(String ownerOp) {
		this.ownerOp = ownerOp;
	}

	public boolean isRecursive() {
		return recursive;
	}

	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLocalFile() {
		return localFile;
	}

	public void setLocalFile(String localFile) {
		this.localFile = localFile;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public String getTextSeq() {
		return textSeq;
	}

	public void setTextSeq(String textSeq) {
		this.textSeq = textSeq;
	}

}
