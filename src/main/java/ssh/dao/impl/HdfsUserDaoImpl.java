package ssh.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import demo.dao.AbstractDao;
import ssh.dao.HdfsUserDao;
import ssh.model.HdfsUser;
@Component
public class HdfsUserDaoImpl extends AbstractDao implements HdfsUserDao {

	private Logger log = LoggerFactory.getLogger(HdfsUserDaoImpl.class);
	@Override
	public int save(HdfsUser hdfsUser) {
		if(checkExist(hdfsUser.getEmail())) return -1;
		return (Integer) this.getHibernateTemplate().save(hdfsUser);
	}

	@Override
	public void update(HdfsUser hdfsUser) {
		this.getHibernateTemplate().update(hdfsUser);
	}
	@Override
	public HdfsUser loadByEmail(String email){
		String hql = "from HdfsUser where email = ?";
		List list = this.getHibernateTemplate().find(hql, email);
		if(list==null || list.size()<1) return null;
		if(list.size()>1){
			log.info("多行email！email:{}"+email);
		}
		
		return (HdfsUser) list.get(0);
	}
	
	public boolean checkExist(String email){
		HdfsUser hdfsUser= loadByEmail(email);
		return hdfsUser==null ? false: true;
	}

}
