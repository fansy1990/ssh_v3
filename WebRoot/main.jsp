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
		<div data-options="region:'north'" style="height:50px">
			<h2 align="center">Hadoop Ecosystem </h2>	
		</div>
		<div data-options="region:'south',split:true" style="height:50px;"></div>
		<div data-options="region:'east',split:true" title="East"
			style="width:180px;">
			<ul class="easyui-tree" id="eastTree"
				data-options="url:'json/hdfsManager.json',method:'get',animate:true,dnd:true"></ul>
		</div>
		<div data-options="region:'west',split:true" title="项目"
			style="width:100px;">
			<div class="easyui-accordion" data-options="fit:true,border:false">
				<div title="HDFS" style="padding:10px;">
					<a onclick="update_project('HDFS文件管理','json/hdfsManager.json')" 
						class="easyui-linkbutton">HDFS文件管理系统</a>
				</div>
				<div title="Other" data-options="selected:true"
					style="padding:10px;">
					<div title="HDFS" style="padding:10px;">
					<a onclick="update_project('Other','json/tree_data1.json')" 
						class="easyui-linkbutton">Other</a>
				</div>
				</div>
				<div title="Title3" style="padding:10px">content3</div>
			</div>
		</div>
		<div id="centerId"
			data-options="region:'center',title:'HDFS文件管理',iconCls:'icon-ok'">
			<div id="centerTab" class="easyui-tabs" 
				data-options="fit:true,border:false,plain:true">
				<div title="HE简介" data-options="href:'about.jsp'"
					style="padding:10px"></div>
			</div>
		</div>
</body>
</html>
