<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>Hadoop Ecosystem</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="themes/icon.css">
<link rel="stylesheet" type="text/css" href="css/demo.css">
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/utils.js"></script>
</head>

<body class="easyui-layout" style="width:100%;height:100%;">
	<%@ include file="login.jsp" %>
	<!-- 登录界面 -->
	<!-- <div id="win" class="easyui-window" title="登录"
		style="width:300px;height:200px;text-align: center;padding: 15px"
		data-options="modal:true,closable:false,collapsible:false,minimizable:false,maximizable:false,draggable:false,resizable:false">

		<form id="login_ff" method="post">
			<table>
			<tr>
			<div>
				<td><label for="email">邮箱:</label></td><td> <input class="easyui-validatebox"
					type="text" name="email" data-options="validType:'email',required:true" /></td>
			</div>
			</tr>
			<tr>
			<div>
				<td><label for="password">密码:</label> </td> <td><input class="easyui-validatebox"
					type="password" name="password" data-options="required:true" /></td>
			</div>
			</tr>
			<tr>
				<td>
				<a id="login_btn" href="#" class="easyui-linkbutton" data-options="">登录</a> 
				</td>
				
				<td></td>
			</tr>
			</table>
		</form>

	</div> -->

	<!-- 主体界面 -->
	<div data-options="region:'north'" style="height:50px">
		<h2 align="center">Hadoop Ecosystem</h2>
	</div>
	<div data-options="region:'south',split:true,collapsed:true"
		style="height:50px;"></div>
	<div data-options="region:'east',split:true" title="East"
		style="width:180px;">
		<ul class="easyui-tree" id="eastTree"
			data-options="url:'json/hdfsManager.json',method:'get',animate:true,dnd:true"></ul>
	</div>
	<div data-options="region:'west',split:true,collapsed:true" title="项目"
		style="width:100px;">
		<div class="easyui-accordion" data-options="fit:true,border:false">
			<div title="HDFS" style="padding:10px;">
				<a onclick="update_project('HDFS文件管理','json/hdfsManager.json')"
					class="easyui-linkbutton">HDFS文件管理系统</a>
			</div>
			<div title="博客推荐系统" data-options="selected:true"
				style="padding:10px;">
				<div title="HDFS" style="padding:10px;">
					<a onclick="update_project('博客推荐系统：未完成','json/tree_data1.json')"
						class="easyui-linkbutton">博客推荐系统</a>
				</div>
			</div>
			<div title="待续" style="padding:10px">待续</div>
		</div>
	</div>
	<div id="centerTitleId"
		data-options="region:'center',title:'HDFS文件管理',iconCls:'icon-ok'">
		<div id="centerTab" class="easyui-tabs"
			data-options="fit:true,border:false,plain:true">
			<div title="HE简介" data-options="href:'about.jsp'"
				style="padding:10px"></div>
		</div>
	</div>
</body>
</html>
