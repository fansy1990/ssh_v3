package demo.service.impl;

import javax.annotation.Resource;
//import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import demo.dao.UserDao;
import demo.model.User;
import demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	@Resource
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public int save(User u) {

		return userDao.save(u);

	}

	@Override
	public void delete(int id) {
		this.userDao.delete(id);
	}

	@Override
	public void update(User u) {
		this.userDao.update(u);
	}

	@Override
	public User load(int id) {
		return this.userDao.load(id);
	}

}
