<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<base href="<%=basePath%>">
<link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="themes/icon.css">
<link rel="stylesheet" type="text/css" href="css/demo.css">
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>

</head>
<body>
	<div style="padding:1px">
	<input class="easyui-textbox" type="text" name="oldValue" value='<%=request.getParameter("value")%>'
						id="data_update_ff_oldValue" style="height:30px;width: 180px;"></input>
		<form id="data_update_ff" class="easyui-form" method="post">
			<table cellpadding="5" align="center">
				<tr>
					<td>表名:</td>
					<td><input class="easyui-textbox" type="text" name="tableName" value='<%=request.getParameter("tableName")%>'
						id="data_update_ff_tableName" style="height:30px;width: 180px; " readonly="readonly"></input></td>
				</tr>
				<tr>

					<td>列簇名:</td>
					<td><input class="easyui-textbox" type="text" name="cfs" readonly="readonly"
						value='<%=request.getParameter("cf")%>' id="data_update_ff_family"
						style="height:30px;width: 180px;"></input></td>
				</tr>
				<tr>
					<td>Row Key:</td>
					<td><input class="easyui-textbox" type="text" name="rowkey" value='<%=request.getParameter("rowkey")%>'
						id="data_update_ff_rowkey" style="height:30px;width: 180px;" readonly="readonly"
						></input></td>
				</tr>
				<tr>
					<td>列名:</td>
					<td><input class="easyui-textbox" type="text" name="column" value='<%=request.getParameter("column")%>'
						id="data_update_ff_column" style="height:30px;width: 180px;" readonly="readonly"
						></input></td>
				</tr>
				<tr>
					<td>时间戳:</td>
					<td><input class="easyui-textbox" type="text" name="timestamp" value='<%=request.getParameter("timestamp")%>'
					readonly="readonly"	id="data_update_ff_timestamp" style="height:30px;width: 180px;"></input></td>
				</tr>
				
				<tr>
					<td>值:</td>
					<td><input class="easyui-textbox" type="text" name="value" value='<%=request.getParameter("value")%>'
						id="data_update_ff_value" style="height:30px;width: 180px;"
						data-options="validType:['length[1,20]'],required:true,prompt:'value'"></input></td>
				</tr>
			</table>
		</form>
		<div style="text-align:center;padding:5px">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="data_update_update()" iconCls="icon-ok">修改</a> <a
				href="javascript:void(0)" iconCls="icon-clear"
				class="easyui-linkbutton" onclick="data_update_cancel()">取消</a>
		</div>
	</div>
	<script type="text/javascript" src="js/hbaseCommand/data_update.js"></script>
	<script type="text/javascript" src="js/utils.js"></script>
</body>
</html>