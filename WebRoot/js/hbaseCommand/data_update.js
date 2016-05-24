
$.extend($.fn.textbox.methods, {
	show: function(jq){
		return jq.each(function(){
			$(this).next().show();
		})
	},
	hide: function(jq){
		return jq.each(function(){
			$(this).next().hide();
		})
	}
})
$(function(){
	$('#data_update_ff_oldValue').textbox('hide');
});
/**
 * 页面弹出数据修改的修改按钮
 */
function data_update_update(){
	if(checkTextBoxEmpty('data_update_ff_value','值不能为空，请重新输入!')) return ;
	popupProgressbar('数据修改','正在修改数据...',1000);
	var ret = callByAJax('hbase/hbaseCommand_updateTableData.action',
			{tableName:$('#data_update_ff_tableName').val(),
			cfs:$('#data_update_ff_family').val(),
			column:$('#data_update_ff_column').val(),
			value:$('#data_update_ff_value').val(),
			oldValue:$('#data_update_ff_oldValue').val(),
			rowkey:$('#data_update_ff_rowkey').val(),
			timestamp:$('#data_update_ff_timestamp').val()});
	closeProgressbar();
	
	if(ret.flag=="true"){
		$.messager.alert('信息',"数据修改成功！",'info',function(){
			parent.$('#win_table_update_data').dialog('close');
		});
	}else{
		$.messager.alert('警告',"数据修改失败！"+ret.msg,'warning');
	}
	
	
}

/**
 * 页面弹出数据新增的取消按钮
 */
function data_add_cancel(){
	parent.$('#win_table_update_data').dialog('close');
}