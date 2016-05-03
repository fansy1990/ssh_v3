package ssh.service;

import javax.annotation.Resource;
//import javax.transaction.Transactional;



import org.springframework.stereotype.Service;

import ssh.dao.HdfsUserDao;
import ssh.model.HdfsUser;


@Service
public class HdfsUserService  {
	private HdfsUserDao hdfsUserDao;

	public HdfsUserDao getHdfsUserDao() {
		return hdfsUserDao;
	}
	@Resource
	public void setHdfsUserDao(HdfsUserDao hdfsUserDao) {
		this.hdfsUserDao = hdfsUserDao;
	}

	public boolean updateByHdfsUserName(String email,String hdfsUserName,String hdfsPassword){
		HdfsUser hdfsUser = hdfsUserDao.loadByEmail(email);
		hdfsUser.setHdfsUserName(hdfsUserName);
		hdfsUser.setHdfsPassword(hdfsPassword);
		try{
			hdfsUserDao.update(hdfsUser);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public HdfsUser getByEmail(String email){
		
		return hdfsUserDao.loadByEmail(email);
	}

}
