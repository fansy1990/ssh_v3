<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'upload.jsp' starting page</title>

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
<body>
	<div style="padding: 15px">
		<div style="margin:20px 0;">
			<span>目录：</span><input class="easyui-validatebox" type="text"
				id="hdfsManager_search_folder" data-options="required:true"
				style="width:300px" value="/" /> <br>
			<p>请输入检索条件，进行检索:</p>
			<div >
				文件名：<input class="easyui-validatebox" type="text"
					id="hdfsManager_search_folder_name" style="width:60px;" value="" /> <select
					id="hdfsManager_search_folder_name_op" class="easyui-combobox"
					style="width:85px;">
					<option value="no">No</option>
					<option value="contains">Contains</option>
					<option value="equals">Equals</option>
					<option value="noequal">NoEqual</option>
				</select> 所有者：<input class="easyui-validatebox" type="text"
					id="hdfsManager_search_owner" style="width:60px;" value="" /> <select
					id="hdfsManager_search_owner_op" class="easyui-combobox"
					style="width:85px;">
					<option value="no">No</option>
					<option value="contains">Contains</option>
					<option value="equals">Equals</option>
					<option value="noequal">NoEqual</option>
				</select>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-filter" onclick="search_data()">确定</a>
				
			</div>
		</div>

		<table id="dg_hdfsManager_search" class="easyui-datagrid"></table>
	</div>
	<script type="text/javascript" src="js/hdfsManager/folder.js"></script>
</body>
</body>
</html>
