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
			<span>HBase表如下所示:</span> <a class="easyui-linkbutton"
				iconCls="icon-reload" onclick="list()">刷新</a> <a
				class="easyui-linkbutton" iconCls="icon-application_view_detail"
				onclick="table_detail()">表详细</a> <a class="easyui-linkbutton"
				iconCls="icon-add" onclick="table_add()">表新增</a> <a
				class="easyui-linkbutton" iconCls="icon-remove"
				onclick="table_delete()">表删除</a>
		</div>

		<table id="dg_hbaseCommand_list" ></table>
		<div id="win_table_detail"></div>

		<div id="win_table_add" class="easyui-window" title="新增表"
			style="width:400px;"
			data-options="closable:true,closed:true,collapsible:false,minimizable:false,maximizable:false,draggable:false,resizable:false,modal:false">
			<div style="padding:10px 60px 20px 60px;">
				<form id="table_add_ff" class="easyui-form" method="post">
					<table cellpadding="5" align="center">
						<tr>
							<td>表名:</td>
							<td><input class="easyui-textbox" type="text" name="tableName" id="tableName_id" 
								style="height:30px;width: 180px;" value="t1"
								data-options="validType:['length[0,20]'] ,required:true,prompt:'t1...'"></input></td>
						</tr>
						<tr>
							<td>列簇名:</td>
							<td><input class="easyui-textbox" type="text" name="cfs"
								style="height:30px;width: 180px;"
								value="cf1,cf2"
								data-options="required:true,prompt:'cf1,cf2,...'"></input></td>
						</tr>
		
					</table>
				</form>
				<div style="text-align:center;padding:5px">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						id="table_add_btn" iconCls="icon-ok">新增</a>
					<a href="javascript:void(0)" id="table_add_cancel_btn"
						class="easyui-linkbutton" iconCls="icon-cancel">清空</a>
				</div>
			</div>

		</div>



	</div>
	<script type="text/javascript" src="js/hbaseCommand/hbaseCommand.js"></script>
</body>
</html>
