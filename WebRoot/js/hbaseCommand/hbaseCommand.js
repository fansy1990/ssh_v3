$(function(){
	
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
		pageSize : 2, // 每页记录数，需要和pageList保持倍数关系
		pageList : [ 2,4,6, 8 ],
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
	
	
	//
});

function list(){
	console.info("abc");
	$('#dg_hbaseCommand_list').datagrid('load',{
//        folder: $('#hdfsManager_list_folder').val()
    });
}