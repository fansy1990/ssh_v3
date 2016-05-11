$(function(){
	
	// hbase 表 list
	$('#dg_hbaseCommand_list').datagrid({
		border : false,
		fitColumns : false,
		singleSelect : true,
		width : 600,
		height : 250,
		nowrap : false,
		fit : true,
		pagination : true,// 分页控件
		pageSize : 4, // 每页记录数，需要和pageList保持倍数关系
		pageList : [ 4, 8, 12 ],
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
					width : '50'
				},{
					field : 'offlineRegions',
					title : 'Offline Regions',
					width : '50',
				},{
					field : 'failedRegions',
					title : 'Failed Regions',
					width : '50'
				},{
					field : 'splitRegions',
					title : 'Split Regions',
					width : '50'
				},{
					field : 'otherRegions',
					title : 'Other Regions',
					width : '50'
				},{
					field : 'description',
					title : '表描述',
					width : '200'
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