package ssh.service;

import javax.annotation.Resource;
//import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ssh.dao.HdfsUserDao;
import ssh.model.HdfsUser;

@Service
public class HdfsUserService {
	private HdfsUserDao hdfsUserDao;

	public HdfsUserDao getHdfsUserDao() {
		return hdfsUserDao;
	}

	@Resource
	public void setHdfsUserDao(HdfsUserDao hdfsUserDao) {
		this.hdfsUserDao = hdfsUserDao;
	}

	public HdfsUser getByEmail(String email) {

		return hdfsUserDao.loadByEmail(email);
	}

	public Integer save(HdfsUser hdfsUser) {
		return hdfsUserDao.save(hdfsUser);
	}

}
