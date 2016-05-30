<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'import2hbase.jsp' starting page</title>

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
	<div style="padding-left: 100px; padding-top: 15px">
		<div style="margin:20px 0;">
		<table style="font-size: 12px;text-align: left;">
		<tr><td>
			HDFS文件路径：
			</td>
			<td> <input id="import2hbase_input"
				class="easyui-textbox" value="/user/root/uid_details.txt" data-options="required:true"
				style="width:350px"> 
		</td></tr>
		<tr><td>
			HBase表名：</td><td> <input id="import2hbase_table"
				class="easyui-textbox" value="user" data-options="required:true"
				style="width:350px">
		</td></tr>
		<tr><td>
				列描述： </td><td><input id="import2hbase_columnDescription"
				class="easyui-textbox" value="rk,info:name,info:birthday,info:gender,info:address,info:phone,info:bank" data-options="required:true"
				style="width:350px"> 
		</td></tr>
		<tr><td>
			 字段分隔符：</td><td> <select
				id="import2hbase_splitter" class="easyui-combobox"
				style="width:180px;">
				<option value="," selected="selected">COMMA(,)</option>
				<option value=":" >COLON(:)</option>
				<option value="|">|</option>
				<option value="::">::</option>
				<option value="\t">Tab(\t)</option>
				<option value=";">semicolon(;)</option>
			</select> 
			</td></tr>
			<tr><td>
			时间格式：</td><td> <select id="import2hbase_dateFormat"
				class="easyui-combobox" style="width:280px;">
				<option value="yyyy-MM-dd HH:mm:ss SSS">yyyy-MM-dd HH:mm:ss SSS</option>
				<option value="yyyy-MM-dd HH:mm:ss" >yyyy-MM-dd HH:mm:ss</option>
				<option value="yyyy-MM-dd HH:mm" selected="selected">yyyy-MM-dd HH:mm</option>
				
			</select> 
			</td></tr>
			<tr><td>
			 <a id="import2hbase_ok_btn" onclick ="import2hbase()"
				class="easyui-linkbutton" data-options="iconCls:'icon-save'">导入</a>
				</td><td>
				<a id="import2hbase_cancel_btn" onclick="init_import2hbase()"
				class="easyui-linkbutton" data-options="iconCls:'icon-save'">清空</a>
			</td></tr>
				</table>
		</div>
	</div>
	<div id="progress_hdfs2hbase" style="width:400px;"></div>
	<script type="text/javascript" src="js/identifyRMB/import2hbase.js"></script>
	<script type="text/javascript" src="js/utils.js"></script>
</body>
</html>
