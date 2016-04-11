package demo.dao.impl;

import org.springframework.stereotype.Component;

import demo.dao.AbstractDao;
import demo.dao.UserDao;
import demo.model.User;

@Component
public class UserDaoImpl extends AbstractDao implements UserDao {

	public int save(User u) {
		return (Integer) this.getHibernateTemplate().save(u);
	}

	@Override
	public void delete(int id) {
		this.getHibernateTemplate().delete(this.load(id));
	}

	@Override
	public void update(User u) {
		this.getHibernateTemplate().update(u);
	}

	@Override
	public User load(int id) {
		return this.getHibernateTemplate().load(User.class, id);
	}

}
