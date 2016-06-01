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

<title>My JSP 'check.jsp' starting page</title>

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
				<tr>
					<td>冠字号：</td>
					<td><input id="check_input" class="easyui-textbox"
						value="" data-options="required:true"
						style="width:350px"></td>
				</tr>
				
				<tr>
					<td>个数：</td>
					<td><select id="check_num" class="easyui-combobox"
						style="width:180px;">
							<option value="1" selected="selected">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
							<option value="7">7</option>
							<option value="8">8</option>
							<option value="9">9</option>
							<option value="10">10</option>
					</select></td>
				</tr>
				
				<tr>
					<td><a id="check_random_btn" onclick="generateRandomStumber()"
						class="easyui-linkbutton" data-options="iconCls:'icon-shading'">随机生成</a>
					</td>
					<td><a id="check_check_btn"
						onclick="check_stumbers()" class="easyui-linkbutton"
						data-options="iconCls:'icon-page_white_magnify'">查询</a></td>
				</tr> 
			</table>
			<br>
			<hr>
			<div id="check_show_or_not_id" style="display: none;">
				<div id="check_result_id" ></div>
				<br>
				版本数： <select id="check_versions_num" class="easyui-combobox"
						style="width:180px;">
							<option value="1" selected="selected">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
							<option value="7">7</option>
							<option value="8">8</option>
							<option value="9">9</option>
							<option value="10">10</option>
							<option value="20">20</option>
							<option value="100">100</option>
							<option value="1000">1000</option>
					</select>
				<a id="check_detail_btn"
						onclick="check_detail()" class="easyui-linkbutton"
						data-options="iconCls:'icon-page_white_magnify'">详细查询</a>
				<br>
				<div id="check_detail_dg"></div>
			</div>
		</div>
	</div>
	
	 <script type="text/javascript" src="js/identifyRMB/check.js"></script> 
	<script type="text/javascript" src="js/utils.js"></script>
</body>
</html>
