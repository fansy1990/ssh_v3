package ssh.interceptor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ssh.util.HadoopUtils;
import ssh.util.Utils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class HdfsAuthInterceptor extends MethodFilterInterceptor {
	private static final Logger log = LoggerFactory
			.getLogger(HdfsAuthInterceptor.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// @Override
	// public String intercept(ActionInvocation invocation) throws Exception {
	// // 取得请求相关的ActionContext实例
	// ActionContext ctx = invocation.getInvocationContext();
	// Map<String, Object> parametersMap = ctx.getParameters();
	// System.out.println(parametersMap.get("folder"));
	// invocation.invoke();
	// return null;
	// }

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		ActionContext ctx = invocation.getInvocationContext();
		Map<String, Object> parametersMap = ctx.getParameters();

		String folder = ((String[]) parametersMap.get("folder"))[0];
		log.info("hdfs folder:{}", folder);
		if (HadoopUtils.checkHdfsAuth(folder, "r")) {
			invocation.invoke();
		} else {
			Utils.write2PrintWriter("nodata");
			log.info("用户对目录{}没有权限!", folder);
		}
		return null;
	}

}
