package ssh.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JarUtil {
	public static final Logger log = LoggerFactory.getLogger(JarUtil.class);
	private static String jarFilePath = null;

	private static void jar(String inputFileName, String outputFileName) {
		JarOutputStream out = null;
		try {
			out = new JarOutputStream(new FileOutputStream(outputFileName));
			File f = new File(inputFileName);
			jar(out, f, "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 把classes目录下面的文件打jar包，并返回jar包路径
	 * 
	 * @param cls
	 * @return
	 */
	public static String jar(Class<?> cls) {
		String outputJar = cls.getClassLoader().getResource("/").getFile()
				.replace("WEB-INF/classes/", "")
				+ "hdfs2hbase.jar";
		jar(cls.getClassLoader().getResource("").getFile(), outputJar);
		jarFilePath = outputJar;
		log.info("MR jar:" + outputJar);
		return outputJar;
	}

	private static void jar(JarOutputStream out, File f, String base)
			throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			base = base.length() == 0 ? "" : base + "/"; // 注意，这里用左斜杠
			for (int i = 0; i < fl.length; i++) {
				jar(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new JarEntry(base));
			FileInputStream in = new FileInputStream(f);
			byte[] buffer = new byte[1024];
			int n = in.read(buffer);
			while (n != -1) {
				out.write(buffer, 0, n);
				n = in.read(buffer);
			}
			in.close();
		}
	}

	public static String getJarFilePath() {
		return jarFilePath;
	}

	public static void setJarFilePath(String jarFilePath) {
		JarUtil.jarFilePath = jarFilePath;
	}

}
