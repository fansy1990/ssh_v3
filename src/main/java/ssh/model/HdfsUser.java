package ssh.model;
/**
 * HDFS模块 用户登录model 
 * email 确认两个用户是否是同一个用户
 */
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class HdfsUser {
	private int id;
	private String name;
	private String email;
	private String password; // 加密存储 md5
	
	private String hdfsUserName; // 权限模块
	private String hdfsPassword;// 权限模块，加密存储md5

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHdfsUserName() {
		return hdfsUserName;
	}

	public void setHdfsUserName(String hdfsUserName) {
		this.hdfsUserName = hdfsUserName;
	}

	public String getHdfsPassword() {
		return hdfsPassword;
	}

	public void setHdfsPassword(String hdfsPassword) {
		this.hdfsPassword = hdfsPassword;
	}

}
