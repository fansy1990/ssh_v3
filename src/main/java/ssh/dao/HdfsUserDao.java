package ssh.dao;

import ssh.model.HdfsUser;

public interface HdfsUserDao {
	public int save(HdfsUser hdfsUser);
	
	public void update(HdfsUser hdfsUser);
	
	public HdfsUser loadByEmail(String email);
}
