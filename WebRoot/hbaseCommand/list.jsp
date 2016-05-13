<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'list.jsp' starting page</title>

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
	<div style="padding: 15px">
		<div style="margin:20px 0; ">
			<span>HBase表如下所示:</span> 
			<a  class="easyui-linkbutton" iconCls="icon-reload" onclick="list()" >刷新</a>
			<a  class="easyui-linkbutton" iconCls="icon-application_view_detail"   onclick="table_detail()">表详细</a>
			<a  class="easyui-linkbutton" iconCls="icon-add"   onclick="table_add()">表新增</a>
			<a  class="easyui-linkbutton" iconCls="icon-remove"  onclick="table_delete()">表删除</a>
		</div>

		<table id="dg_hbaseCommand_list" class="easyui-datagrid"></table>
	</div>
	<script type="text/javascript" src="js/hbaseCommand/hbaseCommand.js"></script>
</body>
</html>
