package demo.service;

import demo.model.User;

public interface UserService {
	public int save(User u);

	public void delete(int id);

	public void update(User u);

	public User load(int id);
}
