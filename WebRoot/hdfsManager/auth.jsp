<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
   <div style="padding: 35px">
		<div style="margin:20px 0;">
			<h3>权限管理：</h3>
			通过设置当前账户绑定的HDFS账号及密码来控制读取及写入的权限；（由于系统默认使用的是启动Tomcat的账号来操作HDFS，
			所以设置此权限管理的目的就是改变默认账号）
			<p>
				验证： 通过HDFS用户名和密码，查看是否HDFS所在Linux机器有此用户，且密码正确；<br>
				更新：验证通过的用户名和密码更新到数据库中；
			</p>
			<hr>
			当前绑定HDFS用户：
			<s:if test='#session.hUser==null'>
    				 null 
			</s:if>
			<s:else>
				<s:property value="#session.hUser"/> 
			</s:else>
		</div>
	</div>

	<div style="padding:10px 60px 20px 60px; ">
	<form id="auth_ff" class="easyui-form" method="post"
				data-options="novalidate:true" >
		<table style="font-size: 12px;text-align: left;">
				<tr>
					<td>HDFS用户名：</td>
					<td><input class="easyui-textbox" type="text" id="hdfsUser" name="hadoopUserName"
							style="height:30px;width: 180px;" 
							data-options="required:true" /></td>
				</tr>
				<tr>
					<td>HDFS密码：</td>
					<td><input class="easyui-textbox" type="password" id="hdfsPassword" name="hadoopPassword"
							style="height:30px;width: 180px;" 
							data-options="required:true"></input></td>
				</tr>
				<tr>
					<td>
						<a  class="easyui-linkbutton" id="authCheckId"
					 iconCls="icon-page_white_magnify">验证</a>
					<a  class="easyui-linkbutton" id="authUpdateId"
					 iconCls="icon-ok">更新</a>
					</td>
					<td>
					
					</td>
				</tr>
		</table>
		</form>
	</div>
	<script type="text/javascript"
		src="js/hdfsManager/hdfsManager_utils.js"></script>
  </body>
</html>
