package demo.service.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import demo.model.User;
import demo.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
// @Transactional(transactionManager = "txManager")
// maven test 使用@Transactional注解或extends
// AbstractTransactionalJUnit4SpringContextTests 任一即可完成事务
public class UserServiceImplTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	UserService userService;

	@Test
	public void testSave() {
		User user = new User();
		user.setName("张三");
		user.setAge(32);
		int id = userService.save(user);

		assertTrue(id > 0);
	}

	@Test
	public void testLoad() {
		User user = new User();
		user.setName("张三");
		user.setAge(32);
		int id = userService.save(user);

		User user2 = userService.load(id);

		assertTrue(user.getAge() == user2.getAge()
				&& user.getName().equals(user2.getName()));
	}

}
