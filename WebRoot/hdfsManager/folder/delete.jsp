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
  <div style="padding: 15px">
		<div style="margin:20px 0;">请输入要删除的目录路径：
    <input  id="dg_hdfsManager_folder_delete" class="easyui-textbox" data-options="iconCls:'icon-folder_delete'" style="width:300px">
    <a id="dg_hdfsManager_folder_delete_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">确定</a>
    </div>
    </div>
  </body>
</html>
