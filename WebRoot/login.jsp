<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div style="margin: 0 auto;width: 400px;">
	<div class="easyui-window" title="用户登录" style="width:400px;"
		data-options="closable:false,collapsible:false,minimizable:false,maximizable:false,draggable:false,resizable:false,modal:false">
		<div style="padding:10px 60px 20px 60px;">
			<form id="ff" class="easyui-form" method="post"
				data-options="novalidate:true" action="do/doLogin.jsp">
				<table cellpadding="5" align="center" style="">
					<tr>
						<td>邮箱:</td>
						<td><input class="easyui-textbox" type="text" name="uname"
							style="height:30px;width: 180px;"
							data-options="validType:['email'],required:true,prompt:'email...'"></input></td>
					</tr>
					<tr>
						<td>密码:</td>
						<td><input class="easyui-textbox" type="password" name="upwd"
							style="height:30px;width: 180px;"
							data-options="required:true,prompt:'password...'"></input></td>
					</tr>
				</table>
			</form>
			<div style="text-align:center;padding:5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="uiEx.submitForm('#ff')" iconCls="icon-man">登录</a> <a
					href="javascript:void(0)" iconCls="icon-clear"
					class="easyui-linkbutton" onclick="uiEx.clearForm('#ff')">重置</a>
			</div>
		</div>

	</div>
</div>

