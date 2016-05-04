package ssh.model;
/**
 * HDFS模块 用户登录model 
 * email 确认两个用户是否是同一个用户
 */
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class HdfsUser {
	private int id;
	private String name;
	private String email;
	private String password; // 加密存储 md5
	// 要设计成单例 才行
//	private String hdfsUserName; // 权限模块
//	private String hdfsPassword;// 权限模块，加密存储md5
	
	private String hadoopUserName;
	private String hadoopPassword; 
	private int authority; // 权限
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
	@Transient
	public String getHadoopUserName() {
		return hadoopUserName;
	}

	public void setHadoopUserName(String hadoopUserName) {
		this.hadoopUserName = hadoopUserName;
	}
	@Transient
	public String getHadoopPassword() {
		return hadoopPassword;
	}

	public void setHadoopPassword(String hadoopPassword) {
		this.hadoopPassword = hadoopPassword;
	}

	public int getAuthority() {
		return authority;
	}

	public void setAuthority(int authority) {
		this.authority = authority;
	}

}
