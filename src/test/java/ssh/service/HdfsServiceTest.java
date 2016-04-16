package ssh.service;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ssh.model.HdfsResponseProperties;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class HdfsServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	HdfsService hdfsService;

	/**
	 * make sure in hdfs there are files or folders in '/' folder (root folder)
	 * 
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testListFolder() throws FileNotFoundException,
			IllegalArgumentException, IOException {
		String folder = "/";
		List<HdfsResponseProperties> files = hdfsService.listFolder(folder);

		assertTrue(files.size() > 0);
	}

}
