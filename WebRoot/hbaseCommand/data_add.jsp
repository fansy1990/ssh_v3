<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
	<div style="padding:10px 60px 20px 60px;">
		<form id="data_add_ff" class="easyui-form" method="post">
			<table cellpadding="5" align="center">
				<tr>
					<td>表名:</td>
					<td><input class="easyui-textbox" type="text" name="tableName" id="data_add_ff_tableName"
						style="height:30px;width: 180px;" ></input></td>
				</tr>
				<tr>
					<td>列簇名:</td>
					<td><input class="easyui-textbox" type="text" name="cfs"
						id="data_add_ff_family" style="height:30px;width: 180px;"></input></td>
				</tr>
				<tr>
					<td>列名:</td>
					<td><input class="easyui-textbox" type="text" name="column"
						id="data_add_ff_column" style="height:30px;width: 180px;" 
						data-options="validType:['length[1,20]'],required:true,prompt:'col1'"></input></td>
				</tr>
				<tr>
					<td>值:</td>
					<td><input class="easyui-textbox" type="text" name="value"
						id="data_add_ff_value" style="height:30px;width: 180px;" value=""
						data-options="validType:['length[1,20]'],required:true,prompt:'value'"></input></td>
				</tr>
			</table>
		</form>
		<div style="text-align:center;padding:5px">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="data_add_add()" iconCls="icon-add">添加</a> <a
				href="javascript:void(0)" iconCls="icon-clear"
				class="easyui-linkbutton" onclick="data_add_cancle()">取消</a>
		</div>
	</div>
<script type="text/javascript" src="js/hbaseCommand/hbaseCommand.js"></script>