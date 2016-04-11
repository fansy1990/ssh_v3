package demo.action;

import org.apache.struts2.StrutsSpringTestCase;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;

//@RunWith(SpringJUnit4ClassRunner.class)// 不要加此Annotation，否则NullPointerException
@ContextConfiguration("classpath:applicationContext.xml")
@WebAppConfiguration
@Transactional(transactionManager = "txManager")
// maven test 使用@Transactional注解或extends
// AbstractTransactionalJUnit4SpringContextTests 任一即可完成事务
public class UserActionTest extends StrutsSpringTestCase {

	public void testGetActionMapping() {
		ActionMapping mapping = getActionMapping("/test/user.action");
		assertNotNull(mapping);
		assertEquals("/test", mapping.getNamespace());
		assertEquals("user", mapping.getName());
	}

	public void testGetActionProxy() throws Exception {
		// set parameters before calling getActionProxy
		request.setParameter("name", "FD");
		request.setParameter("age", "11");

		ActionProxy proxy = getActionProxy("/test/user.action");
		assertNotNull(proxy);

		UserAction action = (UserAction) proxy.getAction();
		assertNotNull(action);

		String result = proxy.execute();
		// String result = action.save();
		assertEquals(Action.SUCCESS, result);
		assertEquals("FD", action.getModel().getName());
	}

}
