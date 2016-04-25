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
		<div style="margin:20px 0;">
			请输入要读取的文件路径： <input id="dg_hdfsManager_file_read_file"
				class="easyui-textbox" value="/"
				data-options="required:true"
				style="width:300px"> 
				<br><br>
			请输入要读取的最大文件记录数： 
			<select id="dg_hdfsManager_file_read_records" 
				class="easyui-combobox"
				 style="width:80px;">
				<option value="1" >1</option>
				<option value="5" selected="selected">5</option>
				<option value="10" >10</option>
				<option value="50" >50</option>
				<option value="100" >100</option>
				<option value="500" >500</option>
				</select>
				文本/序列文件： 
			<select id="dg_hdfsManager_file_read_text_seq" 
				class="easyui-combobox"
				 style="width:80px;">
				<option value="seq" >序列化</option>
				<option value="text" selected="selected">文本</option>
				</select>
				<br>
				 <a id="dg_hdfsManager_file_read_btn" href="#"
				class="easyui-linkbutton" data-options="iconCls:'icon-save'">读取</a>
		</div>
	</div>
	<script type="text/javascript" src="js/hdfsManager/file.js"></script>
	<script type="text/javascript"
		src="js/hdfsManager/hdfsManager_utils.js"></script>
  </body>
</html>
