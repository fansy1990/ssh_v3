package demo.action;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import demo.model.User;
import demo.service.UserService;

@Resource(name = "userAction")
public class UserAction extends ActionSupport implements ModelDriven<User> {
	private User user = new User();

	private UserService userService;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public User getModel() {

		return this.user;
	}

	public String save() {
		this.userService.save(user);
		return SUCCESS;
	}

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
