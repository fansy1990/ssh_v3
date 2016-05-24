//$(function())

/**
 * 页面弹出数据新增的添加按钮
 */
function data_add_add(){
	if(checkTextBoxEmpty('data_add_ff_rowkey','RowKey不能为空，请重新输入!')) return ;
	if(checkTextBoxEmpty('data_add_ff_column','列名不能为空，请重新输入!')) return ;
	if(checkTextBoxEmpty('data_add_ff_value','值不能为空，请重新输入!')) return ;
	popupProgressbar('数据新增','正在新增数据...',1000);
	var ret = callByAJax('hbase/hbaseCommand_addTableData.action',
			{tableName:$('#data_add_ff_tableName').val(),
			cfs:$('#data_add_ff_family').val(),
			column:$('#data_add_ff_column').val(),
			value:$('#data_add_ff_value').val(),
			rowkey:$('#data_add_ff_rowkey').val()});
	closeProgressbar();
	
	if(ret.flag=="true"){
		$.messager.alert('信息',"数据新增成功！",'info',function(){
			parent.$('#win_table_add_data').dialog('close');
		});
	}else{
		$.messager.alert('警告',"数据新增失败！"+ret.msg,'warning');
	}
	
	
}

/**
 * 页面弹出数据新增的取消按钮
 */
function data_add_cancel(){
	parent.$('#win_table_add_data').dialog('close');
}