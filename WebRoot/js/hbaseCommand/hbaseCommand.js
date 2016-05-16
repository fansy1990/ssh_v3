$(function(){
	
	// 获取hbase表
//	var tablesString = callByAJax('hbase/hbaseCommand_getTablesJson.action', {});
////	console.info(JSON.parse(tablesString));
//	console.info(":"+tablesString.data);
	// 绑定获取hbase表；
	$('#cc_data_retrieve_tableName').combobox({    
	    required:true,    
	    multiple:false  ,
	    valueField:'value',
	    textField:'text',
//	    url: 'hbase/hbaseCommand_getTablesJson.action'
//	    data: tablesString.data
	    data: callByAJax('hbase/hbaseCommand_getTablesJson.action', {}).data,
//	    data:[{value:9,text:"addf"},{value:5,text:"sdfsdf"}, {value:3,text:"hjhfjhj"},{value:4,text:"fghgfh"}]
	    onSelect: function (record) { 
	    	console.info("table:"+record.value);
	    	
	    	//设置表对应的column family
	    	$('#cc_data_retrieve_column_family').combobox({    
	    	    required:true,    
	    	    multiple:true ,
	    	    valueField:'value',
	    	    textField:'text',
	    	    data:callByAJax('hbase/hbaseCommand_getTableColumnFamilyJson.action',
	    	    		{tableName:record.value}).data,
	    	});
	    }
	});
	
	// hbase 表 list
	$('#dg_hbaseCommand_list').datagrid({
		border : false,
		fitColumns : false,
		singleSelect : true,
		width : 1100,
		height : 420,
		nowrap : false,
		fit : false,
		pagination : true,// 分页控件
		pageSize : 4, // 每页记录数，需要和pageList保持倍数关系
		pageList : [ 4,8 ,12],
		rownumbers : true,// 行号
		pagePosition : 'top',
		url : 'hbase/hbaseCommand_getTables.action',
//		queryParams: {
//			folder: folder_
//		},
		onLoadError:function(){
			console.info("list load error!");
			 $.messager.alert('信息','加载错误，请联系管理员!','info');
		},
		onBeforeLoad:function(param){
//			return checkExistAndAuth(param.folder,'rx');
			console.info("a");
			return true;
		},
		idField:'id',
		columns :[[
				{
					field : 'nameSpace',
					title : '数据库',
					width : '120'
				},{
					field : 'tableName',
					title : '表名',
					width : '100'
				},{
					field : 'onlineRegions',
					title : 'Online Regions',
					width : '100'
				},{
					field : 'offlineRegions',
					title : 'Offline Regions',
					width : '100',
				},{
					field : 'failedRegions',
					title : 'Failed Regions',
					width : '100'
				},{
					field : 'splitRegions',
					title : 'Split Regions',
					width : '100'
				},{
					field : 'otherRegions',
					title : 'Other Regions',
					width : '100'
				},{
					field : 'description',
					title : '表描述',
					width : '300'
				}
				 ]]
		    }); 
	
	
	//表清空
	$('#table_add_cancel_btn').bind('click', function(){
		$('#table_add_ff').form('clear');
		
	});
	
	//表新增
	$('#table_add_btn').bind('click', function(){
		$('#table_add_ff').submit(); 
	});
	
	// 绑定表新增form表单
	$('#table_add_ff').form({    
	    url:'hbase/hbaseCommand_saveTable.action',
	    onSubmit: function(){    
	    	var tableName_ = $('#tableName_id').val();
	    	var result = callByAJax('hbase/hbaseCommand_checkTableExists.action',{tableName:tableName_});
	    	if(result.flag == "true"){
	    		$.messager.alert('提示','表已存在!','warning');
	    		$('#table_add_ff').form('clear');
	    		return false;
	    	}else{
	    		return true;
	    	}
	         
	    },
	    success:function(data){    
	    	console.info(data);
	    	var jsonData = JSON.parse(data);
	    	if("true" == jsonData.flag){
	    		$('#win_table_add').window('close'); 
	    		$.messager.alert('提示','表新增成功！','info');
	    		$('#table_add_ff').form('clear');
	    	}else{
	    		$.messager.alert('提示','表新增异常！','info');
	    		$('#table_add_ff').form('clear');
	    	}
	    }    
	});  
});

function list(){
	console.info("abc");
	$('#dg_hbaseCommand_list').datagrid('load',{});
}

function table_detail(){
	var slRow = $('#dg_hbaseCommand_list').datagrid('getSelected');
	if(slRow ==null || slRow == 'null'){
		$.messager.alert('信息','请选择一个表!','info');
		return ;
	}
	console.info('table:'+slRow.tableName);
	var ret = callByAJax('hbase/hbaseCommand_getTableDetails.action',{tableName:slRow.tableName});
	if(ret.flag=='false'){
		$.messager.alert('信息','表详细获取错误，请联系管理员!','info');
		return ;
	}
	$('#win_table_detail').window({    
	    width:450,    
	    height:200,    
	    modal:true,
	    left:400,
	    top:200,
	    title:'表详细',
	    collapsible:false,
	    minimizable:false,
	    maximizable:false,
	    content: '<div style="padding:30px 20px 10px 20px;">' + ret.content +'</div>'
	});
	
}

function table_add(){
	
	$('#win_table_add').window('open'); 
}

function table_delete(){
	var slRow = $('#dg_hbaseCommand_list').datagrid('getSelected');
	if(slRow ==null || slRow == 'null'){
		$.messager.alert('信息','请选择一个表!','info');
		return ;
	}
	$.messager.confirm('确认','您确认想要删除表'+slRow.tableName+'吗？',function(r){    
	    if (r){    
	    	var ret = callByAJax('hbase/hbaseCommand_deleteTable.action',{tableName:slRow.tableName});    
	    	if(ret.flag=='false'){
	    		$.messager.alert('信息','表删除错误，请联系管理员!','info');
	    	}else{
	    		$.messager.alert('信息','表:'+slRow.tableName+'被删除!','info');
	    		$('#dg_hbaseCommand_list').datagrid('load',{});
	    	}
	    }    
	});
}